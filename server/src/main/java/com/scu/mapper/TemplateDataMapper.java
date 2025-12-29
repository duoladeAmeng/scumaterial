package com.scu.mapper;


import java.util.List;
import java.util.Map;

public interface TemplateDataMapper {
    public List<Map<String,Object>> getAuditedTemplateData(Long templateId);
}
