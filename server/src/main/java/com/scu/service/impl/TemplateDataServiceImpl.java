package com.scu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.scu.constant.TemplateDataStatusConstant;
import com.scu.dto.TemplateDataConditionDto;
import com.scu.dto.TemplateDataDto;
import com.scu.entity.FileMetaData;
import com.scu.entity.TemplateField;
import com.scu.enu.FieldDataTypeEnum;
import com.scu.mapper.TemplateDataMapper;
import com.scu.mapper.TemplateMapper;
import com.scu.result.Result;
import com.scu.service.FileMetaDataService;
import com.scu.service.TemplateDataService;
import com.scu.service.TemplateFieldService;
import com.scu.util.GridFsUtils;
import com.scu.util.TableOperator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TemplateDataServiceImpl implements TemplateDataService {

    @Autowired
    GridFsUtils gridFsUtils;

    @Autowired
    private TemplateFieldService templateFieldService;

    @Autowired
    private FileMetaDataService fileMetaDataService;
    @Autowired
    private TableOperator tableOperator;

    @Autowired
    private TemplateDataMapper templateDataMapper;


    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private TemplateMapper templateMapper;

    public TemplateDataServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    private boolean isFile(String fieldDataType){
        return fieldDataType.equals(FieldDataTypeEnum.FILE.getName())||
                fieldDataType.equals(FieldDataTypeEnum.PICTURE.getName())||
                fieldDataType.equals(FieldDataTypeEnum.TABLE.getName());

    }


    @Override
    @Transactional
    public void saveTemplateDataSingle(String dataJson, List<MultipartFile> files) {
        // 将 JSON 转换为 List<TemplateDataDto>
        ObjectMapper mapper = new ObjectMapper();
        List<TemplateDataDto> templateDataDtoList=null;
        try {
            templateDataDtoList= mapper.readValue(dataJson, new TypeReference<List<TemplateDataDto>>() {});
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        saveTemplateDataSingle(templateDataDtoList,files);
    }


    private void saveTemplateDataSingle(List<TemplateDataDto> templateDataDtoList, List<MultipartFile> files){
        // 获取第一个 templateId 作为基准
        Long templateId = templateDataDtoList.get(0).getTemplateId();
        // 构造动态表名
        String tableName = "template_data_" + templateId;
        // 校验并提取字段名与值
        Map<String, Object> columnValueMap = new LinkedHashMap<>();

        //建立文件名和数组下标的映射
        Map<String, Integer> fileIndexMap = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            fileIndexMap.put(files.get(i).getOriginalFilename(), i);
        }

        for (TemplateDataDto dto : templateDataDtoList) {
            String fieldName = dto.getFieldName();//字段名
            String fieldDataType = dto.getFieldDataType();
            //如果不是文件字段，直接保存字段值
            if(!isFile(fieldDataType)){
                columnValueMap.put(fieldName, dto.getFieldValue()); // 字段值
                continue;
            }
            // 如果是文件字段，处理文件并保存字段值
            //文件字段则 fieldValue 为文件名
            String fileName = (String) dto.getFieldValue();
            Integer fileIndex = fileIndexMap.get(fileName);
            MultipartFile multipartFile = files.get(fileIndex);
            try {
                //计算文件哈希
                String contentHash;
                contentHash = DigestUtils.sha256Hex(multipartFile.getInputStream());
                //查找文件哈希是否存在
                FileMetaData fileMetaData = fileMetaDataService.findByFileHash(contentHash);
                //存在
                if (fileMetaData!=null) {
                    columnValueMap.put(fieldName, fileMetaData.getFileMongoId());
                    continue;
                }
                //不存在
                //保存文件
                String fileMongoId = gridFsUtils.upload(fileName, multipartFile.getInputStream());
                //保存文件元数据
                columnValueMap.put(fieldName, fileMongoId);
                //保存文件哈希数据
                FileMetaData metaData = new FileMetaData();
                metaData.setFileMongoId(fileMongoId);
                metaData.setFileHash(contentHash);
                fileMetaDataService.save(metaData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 添加 status 字段,默认是未审核
        columnValueMap.put("status", TemplateDataStatusConstant.UNAUDITED);
        // 构造 SQL
        String columns = String.join(", ", columnValueMap.keySet());
        String placeholders = columnValueMap.keySet().stream().map(k -> "?").collect(Collectors.joining(", "));
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        // 提取参数值（顺序必须和 keySet() 一致）
        List<Object> args = new ArrayList<>(columnValueMap.values());

        // 执行插入
        jdbcTemplate.update(sql, args.toArray());
    }


    @Transactional
    @Override
    public void saveTemplateDataBatch(MultipartFile excel, List<MultipartFile> files, Long templateId) {
        List<List<TemplateDataDto>> templateDataDtoList_List = getTemplateDataDtoListFromExcel(excel, templateId);
        for (List<TemplateDataDto> dtoList: templateDataDtoList_List){
            saveTemplateDataSingle(dtoList,files);
        }

    }



    // 获取 Excel 中各个字段的数据类型
    private Map<String,String> getFieldDataTypeMapFromExcel(MultipartFile excel,Long templateId) {
        Map<String,String> fieldDataTypeMap = new HashMap<>();
        try {
            Workbook workbook = new XSSFWorkbook(excel.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            // 获取表头行
            Row headerRow = sheet.getRow(0);
            int lastCellNum = headerRow.getLastCellNum();

            for (int j = 0; j < lastCellNum; j++) {// 获取字段名
                String fieldName = headerRow.getCell(j).getStringCellValue();
                LambdaQueryWrapper<TemplateField> wrapper = Wrappers.lambdaQuery(TemplateField.class)
                        .likeRight(TemplateField::getFieldName, fieldName.split("\\(")[0])
                        .eq(TemplateField::getTemplateId, templateId)
                        ;
                System.out.println(fieldName.split("\\(")[0]);
                System.out.println(templateId);
                TemplateField templateField = templateFieldService.list(wrapper).get(0);
                fieldDataTypeMap.put(fieldName.split("\\(")[0], templateField.getDataType());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fieldDataTypeMap;
    }
        // 从 Excel 中获取 TemplateDataDtoList
    private List<List<TemplateDataDto>> getTemplateDataDtoListFromExcel(MultipartFile excel,Long templateId){

        Map<String,String> fieldDataTypeMap = getFieldDataTypeMapFromExcel(excel,templateId);


        List<List<TemplateDataDto>> templateDataDtoList_List=new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(excel.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // 获取表头行
            Row headerRow = sheet.getRow(0);
            int lastCellNum = headerRow.getLastCellNum();

            // 读取所有数据行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow == null) continue;
                List<TemplateDataDto> dtoList=new ArrayList<>();
                for (int j = 0; j < lastCellNum; j++) {
                    TemplateDataDto dto = new TemplateDataDto();
                    Cell cell = dataRow.getCell(j);
                    // 获取字段名
                    String fieldName = headerRow.getCell(j).getStringCellValue();
                    fieldName=fieldName.split("\\(")[0];
                    // 获取字段值
                    Object cellValue = getCellValue(cell);
                    dto.setTemplateId(templateId);
                    dto.setFieldName(fieldName);
                    dto.setFieldValue(cellValue);
                    dto.setFieldDataType(fieldDataTypeMap.get(fieldName));
                    dtoList.add(dto);
                }
                templateDataDtoList_List.add(dtoList);
            }
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException("批量导入模板数据失败", e);
        }
        return templateDataDtoList_List;
    }


    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * 获取所有模板数据
     * @param templateId
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> getAllTemplateData(Long templateId) {
        return templateDataMapper.getAllTemplateData(templateId);
    }
    /**
     * 获取所有已审核模板数据
     * @param templateId
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> getAllAuditedTemplateData(Long templateId) {
        return templateDataMapper.getAuditedTemplateData(templateId);
    }
    /**
     * 获取所有未审核模板数据
     * @param templateId
     * @return
     */

    /**
     * 获取所有未审核模板数据
     * @param templateId
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> getAllUnAuditedTemplateData(Long templateId) {
        return templateDataMapper.getUnAuditedTemplateData(templateId);
    }
    /**
     * 把模板数据的字段按创建模板时的字段排列
     * @param templateDataList
     * @return
     */
    private List<Map<String, Object>> getOrderedTemplateData(List<Map<String, Object>> templateDataList,Long templateId) {
        LambdaQueryWrapper<TemplateField> wrapper = Wrappers.lambdaQuery(TemplateField.class).eq(TemplateField::getTemplateId, templateId);
        // 获取创建模板时的字段
        List<String> orderFieldList = templateFieldService.list(wrapper).stream().map(templateField -> templateField.getFieldName()).toList();
        ArrayList<Map<String, Object>> ans = new ArrayList<>();
        for (Map<String, Object> templateData : templateDataList){
            Map<String, Object> orderedTemplateData = new HashMap<>();
            for (String fieldName : orderFieldList){
                if (templateData.containsKey(fieldName)){
                    orderedTemplateData.put(fieldName, templateData.get(fieldName));
                }
            }
        }
        return ans;
    }

    // 获取文件
    @Override
    public void getFile(HttpServletResponse response, String fileId) {
        try {
            // 1. 获取文件元数据
            GridFSFile fileMeta = gridFsUtils.getFileMetadataById(fileId);
            if (fileMeta == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File not found");
                return;
            }
            String filename = fileMeta.getFilename();
            if (filename == null) {
                filename = "unnamed_file";
            }

            // 2. 获取输入流
            InputStream inputStream = gridFsUtils.downloadById(fileId);
            if (inputStream == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("File stream is null");
                return;
            }

            // 3. 设置 Content-Type（可选增强）
            // GridFS 默认不存 contentType，但通过 metadata 扩展
            // 这里先用通用类型，或根据扩展名猜测
            String contentType = getContentType(filename);
            response.setContentType(contentType);

            // 4. 设置 Content-Disposition（支持中文文件名）
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                    encodeFileName(filename) + "\"; filename*=UTF-8''" + encodeFileName(filename));

            // 5. 流式写入响应
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
            response.getOutputStream().flush();

            inputStream.close(); // 注意：try-with-resources 更安全，但这里简单关闭

        } catch (IOException e) {
            log.error("下载文件失败, fileId: {}", fileId, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("File download error");
            } catch (IOException ignored) {}
            throw new RuntimeException("下载文件失败", e);
        }
    }



    // 辅助方法：根据文件扩展名设置 Content-Type（简化版）
    private String getContentType(String filename) {
        if (filename.endsWith(".pdf")) return "application/pdf";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".txt")) return "text/plain";
        if (filename.endsWith(".doc")) return "application/msword";
        if (filename.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        // 可继续扩展...
        return "application/octet-stream";
    }

    // 编码文件名（兼容 IE、Chrome、Firefox）
    private String encodeFileName(String fileName) {
        try {
            return java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e) {
            return fileName;
        }
    }

    // 获取模板根据条件数据
    @Override
    public List<LinkedHashMap<String, Object>> getTemplateDataByConditions(TemplateDataConditionDto templateDataConditionDto,boolean isAudited) {
        if(isAudited){
            templateDataConditionDto.getConditions().put("status",TemplateDataStatusConstant.AUDITED+"");
        }
       return   templateDataMapper.getTemplateDataByFieldCondation(templateDataConditionDto.getTemplateId(),templateDataConditionDto.getConditions());
    }

    @Override
    public List<LinkedHashMap<String, Object>> getTemplateDataByCommonField(Map<String, String> fieldVal) {
        List<LinkedHashMap<String, Object>> ans = new ArrayList<>();
        List<Long> templateIds = templateMapper.selectList(null).stream().map(template -> template.getId()).toList();
        for (Long templateId : templateIds){
            List<LinkedHashMap<String, Object>> data= templateDataMapper.getTemplateDataByFieldCondation(templateId, fieldVal);
            ans.addAll(data);
        }
        return ans;
    }


}