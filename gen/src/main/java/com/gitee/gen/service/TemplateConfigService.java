package com.gitee.gen.service;

import com.gitee.gen.entity.TemplateConfig;
import com.gitee.gen.mapper.TemplateConfigMapper;
import com.gitee.gen.util.StringUtil;
import com.gitee.gen.util.TemplateMetaUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.solon.annotation.Db;
import org.noear.solon.annotation.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tanghc
 */
@Component
public class TemplateConfigService {

    @Db
    private TemplateConfigMapper templateConfigMapper;

    public List<TemplateConfig> listTemplate(List<Integer> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return templateConfigMapper.listTemplate(idList);
    }

    public TemplateConfig getById(int id) {
        return templateConfigMapper.getById(id);
    }

    public List<TemplateConfig> listAll() {
        return templateConfigMapper.listAll();
    }

    public void insert(TemplateConfig templateConfig) {
        String name = templateConfig.getName();
        TemplateConfig existObj = templateConfigMapper.getByName(name, templateConfig.getGroupId());
        if (existObj != null) {
            throw new RuntimeException("模板名称 "+ name +" 已存在");
        }
        templateConfig.setIsDeleted(0);
        templateConfigMapper.insert(templateConfig);
    }

    public void update(TemplateConfig templateConfig) {
        String name = templateConfig.getName();
        TemplateConfig existObj = templateConfigMapper.getByName(name, templateConfig.getGroupId());
        if (existObj != null && !Objects.equals(templateConfig.getId(), existObj.getId())) {
            throw new RuntimeException("模板名称 "+ name +" 已存在");
        }
        templateConfigMapper.updateIgnoreNull(templateConfig);
    }

    public void delete(TemplateConfig templateConfig) {
        templateConfigMapper.delete(templateConfig);
    }

    public List<TemplateConfig> listByGroupId(String groupId) {
        return templateConfigMapper.listByGroupId(groupId);
    }

    public void save(TemplateConfig templateConfig) {
        handleContent(templateConfig);
        String name = templateConfig.getName();
        TemplateConfig existObj = templateConfigMapper.getByName(name, templateConfig.getGroupId());
        if(existObj == null) {
            this.insert(templateConfig);
        } else {
            templateConfig.setId(existObj.getId());
            this.update(templateConfig);
        }
    }

    /**
     * 复制模板
     * @param templateConfig
     */
    public void copy(TemplateConfig templateConfig) {
        Integer id = templateConfig.getId();
        TemplateConfig templateConfigById = this.getById(id);
        if(templateConfigById == null){
            throw new RuntimeException("要复制的模板不存在");
        }
        templateConfigById.setId(null);
        templateConfigById.setName(templateConfig.getName());
        this.save(templateConfigById);
    }

    /**
     * 解析模板元信息, 即开始第一行是注释时
     * <p>
     * 格式: ## filename=#{xxx}.java, folder=entity
     */
    private void handleContent(TemplateConfig template) {
        String content = StringUtil.trimLeadingWhitespace(template.getContent());
        // 解析元信息
        Map<String, String> data = TemplateMetaUtils.parseMetaContent(content);
        if (StringUtils.isEmpty(template.getFileName())) {
            template.setFileName(data.get("filename"));
        }
        if (StringUtils.isEmpty(template.getFolder())) {
            template.setFolder(data.get("folder"));
        }
        // 设置默认值
        if (StringUtils.isEmpty(template.getFileName())) {
            template.setFileName(template.getName());
        }
        if (StringUtils.isEmpty(template.getFolder())) {
            template.setFolder(template.getName());
        }
    }
}
