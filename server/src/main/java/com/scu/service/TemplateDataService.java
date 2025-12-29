package com.scu.service;

import com.scu.dto.TemplateDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TemplateDataService {

    // 添加模板数据单条方式
    void saveTemplateDataSingle(String data, List<MultipartFile> files);
    // 添加模板数据批量方式
    void saveTemplateDataBatch(MultipartFile excel, List<MultipartFile> files, Long templateId);

    // 获取所有已审核模板数据
    List<TemplateDataDto> getAllAuditedTemplateData(Long templateId);
    // 获取所有未审核模板数据
    List<TemplateDataDto> getAllUnAuditedTemplateData(Long templateId);
}
