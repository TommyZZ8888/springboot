package com.www.zz.demoworkflow.simple.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.www.zz.demoworkflow.simple.domain.entity.TaskInstanceEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowDefinitionEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowNodeEntity;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowTransitionEntity;
import com.www.zz.demoworkflow.simple.mapper.WorkflowDefinitionMapper;
import com.www.zz.demoworkflow.simple.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @Describtion: WorkflowController
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:12
 */
@Controller // 使用 @Controller 而不是 @RestController，支持 Thymeleaf 渲染
@RequestMapping("/workflow")
public class WorkflowController {

	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private WorkflowDefinitionMapper definitionMapper; // 假设已定义此 Mapper

	@GetMapping
	public String workflowPage() {
		return "workflow"; // 返回 workflow.html
	}

	@GetMapping("/definitions")
	@ResponseBody // 返回 JSON 数据给前端
	public List<WorkflowDefinitionEntity> getDefinitions() {
		return definitionMapper.selectList(new LambdaQueryWrapper<WorkflowDefinitionEntity>()
				.eq(WorkflowDefinitionEntity::getIsActive, true));
	}

	@PostMapping("/definition")
	@ResponseBody
	public Integer createWorkflowDefinition(@RequestBody WorkflowDefinitionEntity definition) {
		return workflowService.createWorkflowDefinition(definition);
	}

	@PostMapping("/node")
	@ResponseBody
	public void createWorkflowNode(@RequestBody WorkflowNodeEntity node) {
		workflowService.createWorkflowNode(node);
	}

	@PostMapping("/transition")
	@ResponseBody
	public void createWorkflowTransition(@RequestBody WorkflowTransitionEntity transition) {
		workflowService.createWorkflowTransition(transition);
	}

	@PostMapping("/start")
	@ResponseBody
	public Integer startWorkflow(@RequestParam Integer workflowId, @RequestParam String businessKey,
	                             @RequestParam String businessType, @RequestParam Integer starterId,
	                             @RequestBody Map<String, Object> variables) throws Exception {
		return workflowService.startWorkflow(workflowId, businessKey, businessType, starterId, variables);
	}

	@PostMapping("/complete")
	@ResponseBody
	public void completeTask(@RequestParam Integer taskId, @RequestParam Integer operatorId,
	                         @RequestParam String status, @RequestParam String comments,
	                         @RequestBody Map<String, Object> formData) throws Exception {
		workflowService.completeTask(taskId, operatorId, status, comments, formData);
	}

	@GetMapping("/tasks")
	@ResponseBody
	public List<TaskInstanceEntity> getTasks(@RequestParam Integer assigneeId) {
		return workflowService.getTasksByAssignee(assigneeId);
	}
}