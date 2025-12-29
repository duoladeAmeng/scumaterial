package com.scu.mapper;


import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TemplateDataMapper {

    //获取所有已经审核的数据
    public List<Map<String,Object>> getAuditedTemplateData(@Param("templateId")Long templateId);
    //获取所有没有审核的数据
    public List<Map<String,Object>> getUnAuditedTemplateData(@Param("templateId")Long templateId);

    boolean updateStatusToPass(Long templateId,List<Long> templateDataId);
}
