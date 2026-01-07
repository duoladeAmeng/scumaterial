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
    //批量审核通过
    boolean updateStatusToPass(Long templateId,List<Long> templateDataId);
    //根据模板id,字段条件获取数据
    public List<LinkedHashMap<String,Object>> getTemplateDataByFieldCondation(@Param("templateId")Long templateId,@Param("condations")Map<String,String> condations);

}
