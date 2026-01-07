package com.scu.service;

import com.scu.dto.TemplateDataConditionDto;
import com.scu.dto.TemplateDataDto;
import com.scu.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface TemplateDataService {

    // 添加模板数据单条方式
    void saveTemplateDataSingle(String data, List<MultipartFile> files);
    // 添加模板数据批量方式
    void saveTemplateDataBatch(MultipartFile excel, List<MultipartFile> files, Long templateId);

    // 获取所有模板数据
    List<LinkedHashMap<String, Object>> getAllTemplateData(Long templateId);
    // 获取所有已审核模板数据
    List<LinkedHashMap<String, Object>> getAllAuditedTemplateData(Long templateId);
    // 获取所有未审核模板数据
    List<LinkedHashMap<String, Object>> getAllUnAuditedTemplateData(Long templateId);
    //获取模板字段类型为文件的字段对应文件
    void getFile(HttpServletResponse  response, String fileId);

    // 根据条件获取模板数据
    List<LinkedHashMap<String, Object>> getTemplateDataByConditions(TemplateDataConditionDto templateDataConditionDto,boolean isAudited);

    // 根据公共字段获取所有模板数据
    List<LinkedHashMap<String, Object>> getTemplateDataByCommonField(Map<String, String> fieldVal);
}
