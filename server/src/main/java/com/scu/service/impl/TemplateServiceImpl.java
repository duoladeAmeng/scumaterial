package com.scu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.constant.MessageConstant;
import com.scu.constant.TemplateStateConstant;
import com.scu.dto.TemplateDto;
import com.scu.entity.Template;
import com.scu.exception.TemplateExistException;
import com.scu.mapper.TemplateMapper;
import com.scu.service.TemplateService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {
    /**
     * 根据类别id获取模板
     * @param categoryId 类别id
     * @return 模板列表
     */
    @Override
    public List<Template> getTemplateByCategory(Integer categoryId) {
        LambdaQueryWrapper<Template> wrapper =
                Wrappers.lambdaQuery(Template.class)
                .eq(Template::getCategoryId, categoryId);
        return this.list(wrapper);
    }

    /**
     * 创建模板
     * @param templateDro 模板信息
     */
    @Override
    public void createTemplate(TemplateDto templateDro) {
        // 获取模板名, 如果模板名已存在则抛出异常
        String templateName = templateDro.getName();
        if (this.getOne(Wrappers.lambdaQuery(Template.class)
                .eq(Template::getName, templateName)) != null) {
            throw new TemplateExistException(MessageConstant.TEMPLATE_NAME_EXIST);
        }
        // 封装模板信息
        Template template = new Template();
        BeanUtil.copyProperties(templateDro,template);
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        // 设置模板状态 新创建的模板状态是未审核
        template.setState(TemplateStateConstant.UNAUDITED);
        // 保存模板
        this.save(template);
    }
}
