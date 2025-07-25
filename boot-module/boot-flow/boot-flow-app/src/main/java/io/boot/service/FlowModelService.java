/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.service;

import cn.hutool.core.util.StrUtil;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.page.PageUtils;
import io.boot.commons.tools.utils.JsonUtils;
import io.boot.dao.BpmDefinitionExtDao;
import io.boot.dto.BpmFormDTO;
import io.boot.dto.ModelDTO;
import io.boot.dto.ModelRequestDTO;
import io.boot.dto.ProcessDefinitionDTO;
import io.boot.entity.BpmDefinitionExtEntity;
import lombok.AllArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型管理
 *
 * @author epiphyllum.zhou@gmail.com
 */
@Service
@AllArgsConstructor
public class FlowModelService {
    private final RepositoryService repositoryService;
    private final BpmFormService bpmFormService;
    private final BpmDefinitionExtDao bpmDefinitionExtDao;

    public PageData<ModelDTO> page(Map<String, Object> params) {
        String key = (String) params.get("key");
        String name = (String) params.get("name");
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (StrUtil.isNotBlank(key)) {
            modelQuery.modelKey(key);
        }
        if (StrUtil.isNotBlank(name)) {
            modelQuery.modelNameLike("%" + name + "%");
        }

        List<Model> models = modelQuery.orderByCreateTime().desc()
                .listPage(PageUtils.getPageOffset(params), PageUtils.getPageLimit(params));

        List<ModelDTO> modelList = new ArrayList<>();
        models.forEach(model -> {
            ModelDTO modelDTO = new ModelDTO();
            modelDTO.setId(model.getId());
            modelDTO.setKey(model.getKey());
            modelDTO.setName(model.getName());
            modelDTO.setVersion(model.getVersion());
            modelDTO.setDeploymentId(model.getDeploymentId());
            modelDTO.setCreated(model.getCreateTime());
            modelDTO.setLastUpdated(model.getLastUpdateTime());

            if (StrUtil.isNotBlank(model.getMetaInfo())) {
                modelDTO.setMetaInfo(JsonUtils.parseObject(model.getMetaInfo(), Map.class));
            }

            if (StrUtil.isNotBlank(model.getDeploymentId())) {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).singleResult();

                ProcessDefinitionDTO processDefinitionDTO = new ProcessDefinitionDTO();
                processDefinitionDTO.setId(processDefinition.getId());
                processDefinitionDTO.setSuspended(processDefinition.isSuspended());
                modelDTO.setProcessDefinition(processDefinitionDTO);
            }

            modelList.add(modelDTO);
        });

        return new PageData<>(modelList, modelQuery.count());
    }

    @Transactional(rollbackFor = Exception.class)
    public String saveModel(ModelRequestDTO dto) {
        // 是否存在
        Model model = getModelByKey(dto.getKey());
        if (model != null) {
            throw new BootException("流程key已存在");
        }

        // 创建模型
        model = repositoryService.newModel();
        model.setKey(dto.getKey());
        model.setName(dto.getName());

        // 保存模型
        repositoryService.saveModel(model);

        // 保存BPMN XML
        repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(dto.getBpmnXml()));

        return model.getId();
    }

    public ModelDTO getModel(String id) {
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw new BootException("流程不存在");
        }

        ModelDTO modelDTO = new ModelDTO();
        modelDTO.setId(model.getId());
        modelDTO.setKey(model.getKey());
        modelDTO.setName(model.getName());

        // 拼接 bpmn XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(id);
        modelDTO.setBpmnXml(StrUtil.utf8Str(bpmnBytes));
        return modelDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateModel(ModelRequestDTO dto) {
        // 是否存在
        Model model = repositoryService.getModel(dto.getId());
        if (model == null) {
            throw new BootException("流程不存在");
        }

        // 修改模型
        if (StrUtil.isNotBlank(dto.getKey())) {
            model.setKey(dto.getKey());
        }

        if (StrUtil.isNotBlank(dto.getName())) {
            model.setName(dto.getName());
        }
        if (StrUtil.isNotBlank(dto.getFormId())) {
            Map<String, String> metaInfo = new HashMap<>();
            metaInfo.put("formType", dto.getFormType());
            metaInfo.put("formId", dto.getFormId());
            metaInfo.put("formName", dto.getFormName());
            model.setMetaInfo(JsonUtils.toJsonString(metaInfo));
        }
        repositoryService.saveModel(model);

        // 保存BPMN XML
        if (StrUtil.isNotBlank(dto.getBpmnXml())) {
            repositoryService.addModelEditorSource(model.getId(), StrUtil.utf8Bytes(dto.getBpmnXml()));
        }

    }

    private Model getModelByKey(String key) {
        return repositoryService.createModelQuery().modelKey(key).singleResult();
    }

    /**
     * 部署
     *
     * @param modelId 模型ID
     */
    public String deploymentByModelId(String modelId) {
        Model model = repositoryService.getModel(modelId);
        if (model == null) {
            throw new BootException("流程不存在");
        }

        if (StrUtil.isBlank(model.getMetaInfo())) {
            throw new BootException("未配置流程表单");
        }

        Map<String, String> metaInfo = JsonUtils.parseObject(model.getMetaInfo(), Map.class);
        String formType = metaInfo.get("formType");
        String formId = metaInfo.get("formId");
        if (StrUtil.isBlank(formId)) {
            throw new BootException("表单不存在，请先配置流程表单");
        }

        String formContent = "";
        if (StrUtil.equalsIgnoreCase(formType, "0")) {
            BpmFormDTO bpmForm = bpmFormService.get(Long.parseLong(formId));
            if (bpmForm == null) {
                throw new BootException("流程表单不存在");
            }

            formContent = bpmForm.getContent();
        }

        // 拼接 bpmn XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        Deployment deployment = repositoryService.createDeployment()
                .name(model.getName())
                .addBytes(model.getKey() + ".bpmn20.xml", bpmnBytes).deploy();

        // 获取 ProcessDefinition
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        // 新增 流程定义扩展
        BpmDefinitionExtEntity definitionExt = new BpmDefinitionExtEntity();
        definitionExt.setModelId(modelId);
        definitionExt.setProcessDefinitionId(processDefinition.getId());
        definitionExt.setFormType(formType);
        definitionExt.setFormId(formId);
        definitionExt.setFormContent(formContent);
        bpmDefinitionExtDao.insert(definitionExt);

        // update deploymentId
        model.setDeploymentId(deployment.getId());
        repositoryService.saveModel(model);

        return deployment.getId();
    }

    public void deleteModel(String id) {
        // 是否存在
        Model model = repositoryService.getModel(id);
        if (model == null) {
            throw new BootException("流程不存在");
        }

        // 删除
        repositoryService.deleteModel(id);
        if (StrUtil.isNotBlank(model.getDeploymentId())) {
            repositoryService.deleteDeployment(model.getDeploymentId(), true);
        }

    }
}
