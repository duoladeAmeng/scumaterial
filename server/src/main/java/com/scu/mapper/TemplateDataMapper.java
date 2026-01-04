package com.scu.mapper;


import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface TemplateDataMapper {

    //获取所有数据
    public List<LinkedHashMap<String,Object>> getAllTemplateData(@Param("templateId")Long templateId);
    //获取所有已经审核的数据
    public List<LinkedHashMap<String,Object>> getAuditedTemplateData(@Param("templateId")Long templateId);
    //获取所有没有审核的数据
    public List<LinkedHashMap<String,Object>> getUnAuditedTemplateData(@Param("templateId")Long templateId);



    boolean updateStatusToPass(Long templateId,List<Long> templateDataId);
}
