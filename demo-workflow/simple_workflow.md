关系图
workflow_definition
↑
workflow_node (1:N)
↑
workflow_transition (定义节点间流转关系)
↓
workflow_instance (1:N)
↑
task_instance (1:N)
↑
workflow_operation_log (1:N)

数据库表创建顺序：
workflow_definition → workflow_node → workflow_transition → workflow_instance → task_instance → workflow_operation_log
流程：
1. 工作流设计阶段
   1.1 创建工作流定义
   ```sql
   -- 在workflow_definition表中创建新流程
   INSERT INTO workflow_definition (name, description, created_by)
   VALUES ('请假审批流程', '员工请假申请审批流程', 1001);
   -- 假设返回的workflow_id为1
   ```
   1.2 定义流程节点
   ```sql
   -- 创建开始节点
   INSERT INTO workflow_node (workflow_id, node_key, node_name, node_type, position_x, position_y)
   VALUES (1, 'start', '开始', 'start', 100, 100);
   
   -- 创建部门审批节点
   INSERT INTO workflow_node (workflow_id, node_key, node_name, node_type, position_x, position_y, config)
   VALUES (1, 'dept_approval', '部门审批', 'approval', 300, 100,
   '{"assigneeType":"role","assignee":"dept_manager","formFields":["reason","days"]}');
   
   -- 创建HR审批节点(当请假天数>3天时需要)
   INSERT INTO workflow_node (workflow_id, node_key, node_name, node_type, position_x, position_y, config)
   VALUES (1, 'hr_approval', 'HR审批', 'approval', 300, 300,
   '{"assigneeType":"role","assignee":"hr","formFields":["reason","days","emergency_contact"]}');
   
   -- 创建结束节点
   INSERT INTO workflow_node (workflow_id, node_key, node_name, node_type, position_x, position_y)
   VALUES (1, 'end', '结束', 'end', 500, 200);


1.3 定义节点流转关系
```sql
-- 开始节点 → 部门审批节点
INSERT INTO workflow_transition (workflow_id, from_node_id, to_node_id, name)
VALUES (1, 1, 2, '提交申请');

-- 部门审批 → HR审批(当请假天数>3天)
INSERT INTO workflow_transition (workflow_id, from_node_id, to_node_id, name, condition_expression)
VALUES (1, 2, 3, '需要HR审批', 'days > 3');

-- 部门审批 → 结束(当请假天数≤3天)
INSERT INTO workflow_transition (workflow_id, from_node_id, to_node_id, name, condition_expression)
VALUES (1, 2, 4, '直接通过', 'days <= 3');

-- HR审批 → 结束
INSERT INTO workflow_transition (workflow_id, from_node_id, to_node_id, name)
VALUES (1, 3, 4, '审批完成');
```


2. 工作流执行阶段
   2.1 发起流程实例
```sql
   -- 员工张三(ID:1001)发起请假申请
   INSERT INTO workflow_instance (workflow_id, business_key, business_type, starter_id, variables)
   VALUES (1, 'LEAVE-20230701', 'leave_application', 1001,
   '{"days":5,"reason":"家庭旅行","start_date":"2023-07-10","end_date":"2023-07-14"}');
   -- 假设返回的instance_id为1

-- 系统自动创建第一个任务(开始节点自动完成)
INSERT INTO task_instance (instance_id, node_id, task_name, status, start_time, end_time)
VALUES (1, 1, '开始流程', 'completed', NOW(), NOW());

-- 系统自动创建部门审批任务
INSERT INTO task_instance (instance_id, node_id, task_name, assignee_id, status)
VALUES (1, 2, '部门审批', 2001, 'pending'); -- 假设部门经理ID为2001
```

2.2 处理审批任务
部门经理审批通过(请假天数>3，需流转到HR审批)
```sql

-- 更新任务状态
UPDATE task_instance
SET status = 'approved',
end_time = NOW(),
comments = '同意，但需HR确认',
operation_by = 2001,
form_data = '{"approval_result":"approved","comment":"同意，但需HR确认"}'
WHERE id = 2;

-- 系统自动创建HR审批任务
INSERT INTO task_instance (instance_id, node_id, task_name, assignee_id, status)
VALUES (1, 3, 'HR审批', 3001, 'pending'); -- 假设HR ID为3001

-- 更新流程实例当前节点
UPDATE workflow_instance
SET current_node_id = 3
WHERE id = 1;

-- 记录操作日志
INSERT INTO workflow_operation_log (instance_id, task_id, operation_type, operator_id, operation_result)
VALUES (1, 2, 'complete_task', 2001, 'approved');
HR审批通过

-- 更新HR任务状态
UPDATE task_instance
SET status = 'approved',
end_time = NOW(),
comments = '已确认行程安排',
operation_by = 3001,
form_data = '{"approval_result":"approved","comment":"已确认行程安排"}'
WHERE id = 3;

-- 系统自动完成流程
UPDATE workflow_instance
SET status = 'completed',
current_node_id = 4,
end_time = NOW()
WHERE id = 1;

-- 记录操作日志
INSERT INTO workflow_operation_log (instance_id, task_id, operation_type, operator_id, operation_result)
VALUES (1, 3, 'complete_task', 3001, 'approved');
```

3. 查询与监控
   3.1 查询用户待办任务
  ```sql
   sql
   SELECT ti.*, wi.business_key, wi.business_type, wn.node_name, wd.name as workflow_name
   FROM task_instance ti
   JOIN workflow_instance wi ON ti.instance_id = wi.id
   JOIN workflow_node wn ON ti.node_id = wn.id
   JOIN workflow_definition wd ON wi.workflow_id = wd.id
   WHERE ti.assignee_id = 2001 AND ti.status = 'pending';
   ```

3.2 查询流程实例详情
```sql
-- 基本实例信息
   SELECT * FROM workflow_instance WHERE id = 1;

-- 关联的任务列表
SELECT ti.*, wn.node_name, wn.node_type
FROM task_instance ti
JOIN workflow_node wn ON ti.node_id = wn.id
WHERE ti.instance_id = 1
ORDER BY ti.start_time;

-- 操作日志
SELECT * FROM workflow_operation_log WHERE instance_id = 1 ORDER BY operation_time;
```

3.3 流程统计报表
  ```sql 
-- 各流程实例数量统计
SELECT wd.name, COUNT(wi.id) as instance_count,
SUM(CASE WHEN wi.status = 'completed' THEN 1 ELSE 0 END) as completed_count
FROM workflow_definition wd
LEFT JOIN workflow_instance wi ON wd.id = wi.workflow_id
GROUP BY wd.id, wd.name;

-- 任务平均处理时间
SELECT wn.node_name, AVG(TIMESTAMPDIFF(MINUTE, ti.start_time, ti.end_time)) as avg_minutes
FROM task_instance ti
JOIN workflow_node wn ON ti.node_id = wn.id
WHERE ti.end_time IS NOT NULL
GROUP BY wn.id, wn.node_name;
```

4. 异常处理流程
   4.1 审批驳回
  ```sql 
   -- 部门经理驳回申请
   UPDATE task_instance
   SET status = 'rejected',
   end_time = NOW(),
   comments = '旺季期间不允许超过3天假期',
   operation_by = 2001,
   form_data = '{"approval_result":"rejected","comment":"旺季期间不允许超过3天假期"}'
   WHERE id = 2;

-- 流程终止
UPDATE workflow_instance
SET status = 'terminated',
end_time = NOW()
WHERE id = 1;

-- 记录日志
INSERT INTO workflow_operation_log (instance_id, task_id, operation_type, operator_id, operation_result)
VALUES (1, 2, 'complete_task', 2001, 'rejected');
```


4.2 重新提交申请
```sql
-- 创建新实例(更新请假天数为2天)
INSERT INTO workflow_instance (workflow_id, business_key, business_type, starter_id, variables)
VALUES (1, 'LEAVE-20230701-R', 'leave_application', 1001,
'{"days":2,"reason":"家庭旅行","start_date":"2023-07-10","end_date":"2023-07-11"}');

-- 后续流程与首次提交类似...
```
5. 扩展功能实现
   5.1 会签(多人审批)
```sql
   -- 在节点配置中添加会签设置
   UPDATE workflow_node
   SET config = JSON_SET(config, '$.multiUser', true, '$.completeCondition', 'all')
   WHERE id = 2;

-- 创建多个审批任务
INSERT INTO task_instance (instance_id, node_id, task_name, assignee_id, status)
VALUES
(1, 2, '部门审批-经理', 2001, 'pending'),
(1, 2, '部门审批-副经理', 2002, 'pending');
```
5.2 动态指定审批人
```sql
-- 使用变量指定审批人
UPDATE workflow_node
SET config = JSON_SET(config, '$.assigneeType', 'variable', '$.assignee', 'next_approver')
WHERE id = 2;

-- 发起实例时指定审批人
INSERT INTO workflow_instance (workflow_id, business_key, starter_id, variables)
VALUES (1, 'LEAVE-20230702', 1001,
'{"days":3,"reason":"病假","next_approver":2003}');
```