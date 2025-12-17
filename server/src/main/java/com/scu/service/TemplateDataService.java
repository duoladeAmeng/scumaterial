package com.scu.service;

import com.scu.dto.TemplateDataDto;

import java.util.List;

public interface TemplateDataService {
    void saveTemplateData(List<TemplateDataDto> templateDataDtos);
}
