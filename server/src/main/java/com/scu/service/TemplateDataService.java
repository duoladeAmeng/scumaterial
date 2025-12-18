package com.scu.service;

import com.scu.dto.TemplateDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TemplateDataService {
    void saveTemplateDataSingle(List<TemplateDataDto> templateDataDtos);

    void saveTemplateDataBatch(MultipartFile file, Long templateId);
}
