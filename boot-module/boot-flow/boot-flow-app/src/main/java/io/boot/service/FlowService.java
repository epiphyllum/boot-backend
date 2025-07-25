/**
 * Copyright (c) 2020 BootCloud All rights reserved.
 * <p>
 * https://www.bootcloud.io
 * <p>
 * 版权所有，侵权必究！
 */
package io.boot.service;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.boot.commons.security.user.SecurityUser;
import io.boot.commons.security.user.UserDetail;
import io.boot.commons.tools.exception.BootException;
import io.boot.commons.tools.page.PageData;
import io.boot.commons.tools.page.PageUtils;
import io.boot.dao.BpmDefinitionExtDao;
import io.boot.dao.BpmInstanceExtDao;
import io.boot.dto.HistoryDetailDTO;
import io.boot.dto.ProcessInstanceDTO;
import io.boot.dto.TaskDTO;
import io.boot.entity.BpmDefinitionExtEntity;
import io.boot.entity.BpmInstanceExtEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.flowable.task.api.DelegationState.PENDING;

/**
 * @author epiphyllum.zhou@gmail.com
 */
@Service
@AllArgsConstructor
public class FlowService {
    private final HistoryService historyService;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final ProcessEngineFactoryBean processEngine;
    private final UserFeignService userFeignService;
    private final BpmDefinitionExtDao bpmDefinitionExtDao;
    private final BpmInstanceExtDao bpmInstanceExtDao;
    private final IdentityService identityService;

    /**
     * 我的申请列表
     */
    public PageData<ProcessInstanceDTO> myPage(Map<String, Object> params) {
        List<ProcessInstanceDTO> userList = new ArrayList<>();

        //我的申请列表
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().
                startedBy(SecurityUser.getUserId().toString()).orderByProcessInstanceStartTime().desc();
        if (StringUtils.isNotBlank((String) params.get("processDefinitionName"))) {
            query.processDefinitionName((String) params.get("processDefinitionName"));
        }

        List<HistoricProcessInstance> historicList = query.listPage(PageUtils.getPageOffset(params), PageUtils.getPageLimit(params));
        historicList.forEach(historic -> {
            ProcessInstanceDTO dto = new ProcessInstanceDTO();
            dto.setProcessDefinitionId(historic.getProcessDefinitionId());
            dto.setProcessInstanceId(historic.getId());
            dto.setProcessDefinitionVersion(historic.getProcessDefinitionVersion());
            dto.setProcessDefinitionName(historic.getProcessDefinitionName());
            dto.setProcessDefinitionKey(historic.getProcessDefinitionKey());
            if (historic.getEndActivityId() != null) {
                dto.setEnded(true);
            } else {
                dto.setEnded(false);
            }
            dto.setEndTime(historic.getEndTime());
            dto.setStartTime(historic.getStartTime());
            dto.setBusinessKey(historic.getBusinessKey());
            dto.setCreateUserId(historic.getStartUserId());

            List<HistoricTaskInstance> historicTaskList = historyService.createHistoricTaskInstanceQuery().
                    processInstanceId(historic.getId()).orderByHistoricTaskInstanceEndTime().desc().list();
            if (historicTaskList.size() > 0) {
                HistoricTaskInstance historicTask = historicTaskList.iterator().next();
                dto.setTaskName(historicTask.getName());
            }

            userList.add(dto);
        });

        return new PageData<>(userList, query.count());
    }

    /**
     * 流程表单
     */
    public Map<String, String> form(String processDefinitionId) {
        BpmDefinitionExtEntity definitionExt = bpmDefinitionExtDao.selectOne(new LambdaQueryWrapper<BpmDefinitionExtEntity>().eq(BpmDefinitionExtEntity::getProcessDefinitionId, processDefinitionId));

        Map<String, String> result = new HashMap<>();
        result.put("formType", definitionExt.getFormType());
        result.put("formId", definitionExt.getFormId());
        result.put("formContent", definitionExt.getFormContent());

        return result;
    }

    public Map<String, Object> formInstanceVariables(String processInstanceId) {
        BpmInstanceExtEntity instanceExt = bpmInstanceExtDao.selectOne(new LambdaQueryWrapper<BpmInstanceExtEntity>().eq(BpmInstanceExtEntity::getProcessInstanceId, processInstanceId));

        if (instanceExt == null) {
            return new HashMap<>();
        } else {
            return instanceExt.getFormVariables();
        }
    }

    /**
     * 启动流程实例
     */
    @Transactional(rollbackFor = Exception.class)
    public String startInstance(String processDefinitionId, Map<String, Object> variables, boolean instanceExtFlag) {
        UserDetail user = SecurityUser.getUser();
        // 启动流程用户
        identityService.setAuthenticatedUserId(user.getId().toString());

        // 启动流程参数
        variables.put("startUserId", user.getId().toString());

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, null, variables);

        if (instanceExtFlag) {
            // 流程实例扩展表
            BpmInstanceExtEntity instanceExt = new BpmInstanceExtEntity();
            instanceExt.setProcessInstanceId(processInstance.getProcessInstanceId());
            instanceExt.setProcessDefinitionId(processDefinitionId);
            instanceExt.setFormVariables(variables);
            bpmInstanceExtDao.insert(instanceExt);
        }

        return processInstance.getProcessInstanceId();
    }

    /**
     * 已办任务
     */
    public PageData<ProcessInstanceDTO> donePage(Map<String, Object> params) {
        List<ProcessInstanceDTO> doneList = new ArrayList<>();

        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery().taskAssignee(SecurityUser.getUserId().toString()).finished()
                .includeProcessVariables().orderByHistoricTaskInstanceEndTime().desc();
        if (StringUtils.isNotBlank((String) params.get("processDefinitionName"))) {
            query.processDefinitionNameLike("%" + params.get("processDefinitionName") + "%");
        }
        //已办任务列表
        List<HistoricTaskInstance> historicList = query.listPage(PageUtils.getPageOffset(params), PageUtils.getPageLimit(params));
        historicList.forEach(historic -> {
            ProcessInstanceDTO dto = new ProcessInstanceDTO();
            dto.setProcessDefinitionId(historic.getProcessDefinitionId());
            dto.setTaskName(historic.getName());
            dto.setProcessInstanceId(historic.getProcessInstanceId());
            dto.setStartTime(historic.getCreateTime());
            dto.setEndTime(historic.getEndTime());

            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().
                    processInstanceId(historic.getProcessInstanceId()).singleResult();
            dto.setBusinessKey(instance.getBusinessKey());
            dto.setProcessDefinitionKey(instance.getProcessDefinitionKey());
            dto.setProcessDefinitionName(instance.getProcessDefinitionName());
            dto.setProcessDefinitionVersion(instance.getProcessDefinitionVersion());

            String userName = userFeignService.getUsernameById(instance.getStartUserId());
            if (StringUtils.isNotBlank(userName)) {
                dto.setStartUserName(userName);
            }

            doneList.add(dto);
        });

        return new PageData<>(doneList, query.count());
    }

    /**
     * 待办任务
     */
    public PageData<TaskDTO> todoPage(Map<String, Object> params) {
        //待办任务列表
        TaskQuery query = taskService.createTaskQuery().taskCandidateOrAssigned(SecurityUser.getUserId().toString()).active();
        if (StringUtils.isNotBlank((String) params.get("definitionName"))) {
            query.processDefinitionNameLike("%" + params.get("definitionName") + "%");
        }
        query.orderByTaskCreateTime().desc();
        List<Task> taskList = query.listPage(PageUtils.getPageOffset(params), PageUtils.getPageLimit(params));
        List<TaskDTO> todoList = new ArrayList<>();
        taskList.forEach(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setTaskId(task.getId());
            dto.setTaskName(task.getName());
            dto.setActivityId(task.getExecutionId());
            dto.setAssignee(task.getAssignee());
            dto.setProcessDefinitionId(task.getProcessDefinitionId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setOwner(task.getOwner());
            dto.setCreateTime(task.getCreateTime());
            dto.setDueDate(task.getDueDate());

            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            dto.setProcessDefinitionName(processDefinition.getName());
            dto.setProcessDefinitionKey(processDefinition.getKey());

            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            dto.setStartTime(processInstance.getStartTime());
            dto.setBusinessKey(processInstance.getBusinessKey());

            String userName = userFeignService.getUsernameById(processInstance.getStartUserId());
            if (StringUtils.isNotBlank(userName)) {
                dto.setStartUserName(userName);
            }

            todoList.add(dto);
        });

        return new PageData<>(todoList, query.count());
    }

    public void diagramImage(String processInstanceId, HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(processInstanceId)) {
            return;
        }

        //当前节点ID
        String processDefinitionId;
        //是否结束
        boolean finish = historyService.createHistoricProcessInstanceQuery().finished().processInstanceId(processInstanceId).count() > 0;
        if (finish) {
            HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = instance.getProcessDefinitionId();
        } else {
            ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            processDefinitionId = instance.getProcessDefinitionId();
        }

        List<String> activityIds = new ArrayList<>();
        List<String> flowList = new ArrayList<>();
        List<HistoricActivityInstance> highLightedActivityList = historyService.createHistoricActivityInstanceQuery().
                processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        for (HistoricActivityInstance instance : highLightedActivityList) {
            String activityType = instance.getActivityType();
            if (activityType.equals("sequenceFlow") || activityType.equals("exclusiveGateway")) {
                flowList.add(instance.getActivityId());
            } else if (activityType.equals("userTask") || activityType.equals("startEvent") || activityType.equals("endEvent")) {
                activityIds.add(instance.getActivityId());
            }
        }

        //流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engConf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engConf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flowList, engConf.getActivityFontName(),
                engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);

        response.setHeader("Content-Type", "image/png");
        response.setHeader("Cache-Control", "no-store, no-cache");
        IOUtils.copy(in, response.getOutputStream());
    }

    /**
     * 获取流转详情列表
     *
     * @param processInstanceId 流程实例ID
     */
    public List<HistoryDetailDTO> historicTaskList(String processInstanceId) {
        List<HistoryDetailDTO> actList = new ArrayList<>();

        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().orderByHistoricActivityInstanceEndTime().asc().list();

        for (HistoricActivityInstance instance : historicActivityInstanceList) {
            if (instance.getEndTime() != null) {
                //显示开始节点和结束节点，并且执行人不为空的任务
                if (StringUtils.isNotBlank(instance.getAssignee())
                        || BpmnXMLConstants.ELEMENT_EVENT_START.equals(instance.getActivityType())
                        || BpmnXMLConstants.ELEMENT_EVENT_END.equals(instance.getActivityType())) {

                    HistoryDetailDTO flow = new HistoryDetailDTO();
                    flow.setActivityName(instance.getActivityName());
                    flow.setStartTime(instance.getStartTime());
                    flow.setEndTime(instance.getEndTime());
                    flow.setDurationInSeconds(DateUtil.formatBetween(instance.getDurationInMillis(), BetweenFormatter.Level.SECOND));

                    //获取流程发起人名称
                    if (BpmnXMLConstants.ELEMENT_EVENT_START.equals(instance.getActivityType())) {
                        List<HistoricProcessInstance> processInstanceList = historyService.createHistoricProcessInstanceQuery().
                                processInstanceId(processInstanceId).orderByProcessInstanceStartTime().asc().list();
                        if (processInstanceList.size() > 0) {
                            String startUserId = processInstanceList.get(0).getStartUserId();
                            if (StringUtils.isNotBlank(startUserId)) {

                                String userName = userFeignService.getUsernameById(startUserId);
                                if (StringUtils.isNotBlank(userName)) {
                                    flow.setAssignee(instance.getAssignee());
                                    flow.setAssigneeName(userName);
                                }
                            }
                        }
                    }

                    //获取任务执行人名称
                    if (StringUtils.isNotBlank(instance.getAssignee())) {
                        String userName = userFeignService.getUsernameById(instance.getAssignee());
                        if (StringUtils.isNotBlank(userName)) {
                            flow.setAssignee(instance.getAssignee());
                            flow.setAssigneeName(userName);
                        }
                    }

                    //获取意见评论内容
                    if (StringUtils.isNotBlank(instance.getTaskId())) {
                        List<Comment> commentList = taskService.getTaskComments(instance.getTaskId());
                        if (commentList.size() > 0) {
                            flow.setComment(commentList.get(0).getFullMessage());
                        }
                    }

                    actList.add(flow);
                }
            }
        }

        return actList;
    }


    /**
     * 委托任务
     */
    public void delegate(String taskId, String ownerId, String userId) {
        //委托人为任务的拥有者
        taskService.setOwner(taskId, ownerId);

        taskService.delegateTask(taskId, userId);
    }

    /**
     * 完成任务
     */
    public void complete(String taskId, String comment, boolean pass) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        //添加意见
        if (StringUtils.isNotBlank(comment)) {
            taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        }

        //流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("pass", pass);

        //是否存在委托任务
        if (StringUtils.isNotBlank(task.getOwner())) {
            if (task.getDelegationState().equals(PENDING)) {
                //被委托的流程，需要先resolved，才能提交任务
                taskService.resolveTask(taskId);
            }
        } else if (StringUtils.isBlank(task.getAssignee())) {
            //未签收，则先签收任务
            taskService.claim(taskId, SecurityUser.getUserId().toString());
        }

        //提交任务
        taskService.complete(taskId, variables);
    }

    /**
     * 驳回任务
     *
     * @param taskId 任务ID
     * @param reason 原因
     */
    public void reject(String taskId, String reason) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new BootException("流程任务不存在");
        }

        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if (instance == null) {
            throw new BootException("流程实例不存在");
        }

        // 未签收，则先签收任务
        if (StringUtils.isBlank(task.getAssignee())) {
            taskService.claim(taskId, SecurityUser.getUserId().toString());
        }

        taskService.addComment(task.getId(), instance.getProcessInstanceId(), "comment", "驳回：" + reason);

        BpmnModel bpmnModel = this.repositoryService.getBpmnModel(instance.getProcessDefinitionId());
        Process process = bpmnModel.getMainProcess();

        List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class);
        String endId = endNodes.get(0).getId();


        List<Execution> executions = runtimeService.createExecutionQuery().parentId(instance.getProcessInstanceId()).list();
        List<String> executionIds = new ArrayList<>();
        executions.forEach(execution -> executionIds.add(execution.getId()));


        runtimeService.createChangeActivityStateBuilder()
                .moveExecutionsToSingleActivityId(executionIds, endId)
                .changeState();
    }

}
