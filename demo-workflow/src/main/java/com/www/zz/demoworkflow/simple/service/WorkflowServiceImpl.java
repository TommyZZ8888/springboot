
package com.www.zz.demoworkflow.simple.service;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.www.zz.demoworkflow.simple.domain.entity.*;
import com.www.zz.demoworkflow.simple.mapper.*;
import com.www.zz.demoworkflow.simple.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.www.zz.demoworkflow.simple.domain.entity.TaskInstanceEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowDefinitionEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowNodeEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowTransitionEntity;


/**
 * @Describtion: WorkflowService
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:13
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private WorkflowDefinitionMapper definitionMapper;
	@Autowired
	private WorkflowNodeMapper nodeMapper;
	@Autowired
	private WorkflowTransitionMapper transitionMapper;
	@Autowired
	private WorkflowInstanceMapper instanceMapper;
	@Autowired
	private TaskInstanceMapper taskMapper;
	@Autowired
	private WorkflowOperationLogMapper logMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	@Transactional
	public Integer createWorkflowDefinition(WorkflowDefinitionEntity definition) {
		definition.setCreatedAt(new Date());
		definition.setUpdatedAt(new Date());
		definition.setIsActive(true);
		definitionMapper.insert(definition);
		return definition.getId();
	}

	@Override
	@Transactional
	public void createWorkflowNode(WorkflowNodeEntity node) {
		node.setCreatedAt(new Date());
		node.setUpdatedAt(new Date());
		nodeMapper.insert(node);
	}

	@Override
	@Transactional
	public void createWorkflowTransition(WorkflowTransitionEntity transition) {
		transition.setCreatedAt(new Date());
		transition.setUpdatedAt(new Date());
		transitionMapper.insert(transition);
	}

	@Override
	@Transactional
	public Integer startWorkflow(Integer workflowId, String businessKey, String businessType, Integer starterId, Map<String, Object> variables) throws Exception {
		WorkflowInstanceEntity instance = new WorkflowInstanceEntity();
		instance.setWorkflowId(workflowId);
		instance.setBusinessKey(businessKey);
		instance.setBusinessType(businessType);
		instance.setStarterId(starterId);
		instance.setStatus("running");
		instance.setStartTime(new Date());
		instance.setCreatedAt(new Date());
		instance.setUpdatedAt(new Date());
		instance.setVariables(objectMapper.writeValueAsString(variables));
		instanceMapper.insert(instance);

		WorkflowNodeEntity startNode = nodeMapper.selectOne(new LambdaQueryWrapper<WorkflowNodeEntity>()
				.eq(WorkflowNodeEntity::getWorkflowId, workflowId)
				.eq(WorkflowNodeEntity::getNodeType, "start"));
		instance.setCurrentNodeId(startNode.getId());
		instanceMapper.updateById(instance);

		TaskInstanceEntity startTask = new TaskInstanceEntity();
		startTask.setInstanceId(instance.getId());
		startTask.setNodeId(startNode.getId());
		startTask.setTaskName("开始流程");
		startTask.setStatus("completed");
		startTask.setStartTime(new Date());
		startTask.setEndTime(new Date());
		startTask.setCreatedAt(new Date());
		startTask.setUpdatedAt(new Date());
		taskMapper.insert(startTask);

		WorkflowOperationLogEntity log = new WorkflowOperationLogEntity();
		log.setInstanceId(instance.getId());
		log.setTaskId(startTask.getId());
		log.setOperationType("start_process");
		log.setOperatorId(starterId);
		log.setOperationResult("started");
		log.setOperationTime(new Date());
		logMapper.insert(log);

		autoTransition(instance, variables);
		return instance.getId();
	}

	@Override
	@Transactional
	public void completeTask(Integer taskId, Integer operatorId, String status, String comments, Map<String, Object> formData) throws Exception {
		TaskInstanceEntity task = taskMapper.selectById(taskId);
		task.setStatus(status);
		task.setEndTime(new Date());
		task.setComments(comments);
		task.setOperationBy(operatorId);
		task.setFormData(objectMapper.writeValueAsString(formData));
		task.setUpdatedAt(new Date());
		taskMapper.updateById(task);

		WorkflowInstanceEntity instance = instanceMapper.selectById(task.getInstanceId());
		WorkflowOperationLogEntity log = new WorkflowOperationLogEntity();
		log.setInstanceId(instance.getId());
		log.setTaskId(taskId);
		log.setOperationType("complete_task");
		log.setOperatorId(operatorId);
		log.setOperationResult(status);
		log.setOperationTime(new Date());
		logMapper.insert(log);

		if ("rejected".equals(status)) {
			instance.setStatus("terminated");
			instance.setEndTime(new Date());
			instance.setUpdatedAt(new Date());
			instanceMapper.updateById(instance);
			return;
		}

		Map<String, Object> variables = objectMapper.readValue((String) instance.getVariables(), Map.class);
		autoTransition(instance, variables);
	}

	private void autoTransition(WorkflowInstanceEntity instance, Map<String, Object> variables) throws Exception {
		List<WorkflowTransitionEntity> transitions = transitionMapper.selectList(new LambdaQueryWrapper<WorkflowTransitionEntity>()
				.eq(WorkflowTransitionEntity::getWorkflowId, instance.getWorkflowId())
				.eq(WorkflowTransitionEntity::getFromNodeId, instance.getCurrentNodeId()));

		for (WorkflowTransitionEntity transition : transitions) {
			if (evaluateCondition(transition.getConditionExpression(), variables)) {
				WorkflowNodeEntity nextNode = nodeMapper.selectById(transition.getToNodeId());
				instance.setCurrentNodeId(nextNode.getId());
				instance.setUpdatedAt(new Date());
				instanceMapper.updateById(instance);

				if ("approval".equals(nextNode.getNodeType())) {
					TaskInstanceEntity task = new TaskInstanceEntity();
					task.setInstanceId(instance.getId());
					task.setNodeId(nextNode.getId());
					task.setTaskName(nextNode.getNodeName());
					task.setStatus("pending");
					task.setStartTime(new Date());
					task.setCreatedAt(new Date());
					task.setUpdatedAt(new Date());
					JsonNode config = objectMapper.readTree((String) nextNode.getConfig());
					task.setAssigneeId(config.get("assignee").asInt());
					taskMapper.insert(task);
				} else if ("end".equals(nextNode.getNodeType())) {
					instance.setStatus("completed");
					instance.setEndTime(new Date());
					instance.setUpdatedAt(new Date());
					instanceMapper.updateById(instance);
				}
				break;
			}
		}
	}

	private boolean evaluateCondition(String expression, Map<String, Object> variables) {
		if (expression == null || expression.isEmpty()) return true;
		Integer days = (Integer) variables.get("days");
		if (expression.equals("days > 3")) return days > 3;
		if (expression.equals("days <= 3")) return days <= 3;
		return false;
	}

	@Override
	public List<TaskInstanceEntity> getTasksByAssignee(Integer assigneeId) {
		return taskMapper.selectList(new LambdaQueryWrapper<TaskInstanceEntity>()
				.eq(TaskInstanceEntity::getAssigneeId, assigneeId)
				.eq(TaskInstanceEntity::getStatus, "pending"));
	}
}