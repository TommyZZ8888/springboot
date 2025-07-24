
package com.www.zz.demoworkflow.simple.service;

import com.www.zz.demoworkflow.simple.domain.entity.TaskInstanceEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowDefinitionEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowInstanceEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowNodeEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowTransitionEntity;

import java.util.List;
import java.util.Map;

/**
 * @Describtion: WorkflowService
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:13
 */
public interface WorkflowService {
	Integer createWorkflowDefinition(WorkflowDefinitionEntity definition);
	void createWorkflowNode(WorkflowNodeEntity node);
	void createWorkflowTransition(WorkflowTransitionEntity transition);
	Integer startWorkflow(Integer workflowId, String businessKey, String businessType, Integer starterId, Map<String, Object> variables) throws Exception;
	void completeTask(Integer taskId, Integer operatorId, String status, String comments, Map<String, Object> formData) throws Exception;
	List<TaskInstanceEntity> getTasksByAssignee(Integer assigneeId);
}
