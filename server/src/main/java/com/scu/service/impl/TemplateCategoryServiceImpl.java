package com.scu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.entity.TemplateCategory;
import com.scu.mapper.TemplateCategoryMapper;
import com.scu.service.TemplateCategoryService;
import org.springframework.stereotype.Service;

@Service
public class TemplateCategoryServiceImpl extends ServiceImpl<TemplateCategoryMapper, TemplateCategory> implements TemplateCategoryService {
}
