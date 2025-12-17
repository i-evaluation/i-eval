package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/schedules")
public class ProductionPlanAndTaskAssignmentController extends BasedController {

    // 57. 上传生产计划
    @PostMapping
    public String uploadProductionSchedule(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload production schedule...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("scheduleId", System.currentTimeMillis());
        result.put("planName", requestBody.getString("planName"));
        result.put("startDate", requestBody.getString("startDate"));
        result.put("endDate", requestBody.getString("endDate"));
        result.put("product", requestBody.getString("product"));
        result.put("quantity", requestBody.getInteger("quantity"));
        result.put("machines", requestBody.getJSONArray("machines"));
        result.put("status", "计划中");
        result.put("createdBy", requestBody.getString("createdBy"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload production schedule...");
        return result.toString();
    }

    // 58. 查询生产计划
    @GetMapping("/{scheduleId}")
    public String getProductionSchedule(@PathVariable Long scheduleId) throws InterruptedException {
        logger.info("Execute get production schedule...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (scheduleId == null || scheduleId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("scheduleId", scheduleId);
            result.put("planName", "九月生产计划");
            result.put("startDate", "2025-09-01T00:00:00Z");
            result.put("endDate", "2025-09-30T23:59:59Z");
            result.put("product", "零件A");
            result.put("quantity", 5000);
            com.alibaba.fastjson.JSONArray machines = new com.alibaba.fastjson.JSONArray();
            machines.add("MACHINE-001");
            machines.add("MACHINE-002");
            result.put("machines", machines);
            result.put("status", "进行中");
            result.put("createdBy", "生产主管");
            result.put("createdAt", "2025-08-25T10:00:00Z");
            logger.info("Success to get production schedule...");
        }
        return result.toString();
    }

    // 59. 查询指定日期生产计划
    @GetMapping("/date/{date}")
    public String getProductionScheduleByDate(@PathVariable String date) throws InterruptedException {
        logger.info("Execute get production schedule by date...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (date == null || date.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的日期");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("date", date);
            com.alibaba.fastjson.JSONArray schedules = new com.alibaba.fastjson.JSONArray();
            schedules.add(createScheduleObject(1L, "零件A生产", "2025-09-23T08:00:00Z", "2025-09-23T17:00:00Z", "MACHINE-001", "进行中"));
            schedules.add(createScheduleObject(2L, "零件B生产", "2025-09-23T08:00:00Z", "2025-09-23T17:00:00Z", "MACHINE-002", "进行中"));
            result.put("schedules", schedules);
            logger.info("Success to get production schedule by date...");
        }
        return result.toString();
    }

    // 60. 上传任务分配
    @PostMapping("/tasks")
    public String uploadTaskAssignment(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload task assignment...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("taskId", System.currentTimeMillis());
        result.put("scheduleId", requestBody.getString("scheduleId"));
        result.put("machineId", requestBody.getString("machineId"));
        result.put("operator", requestBody.getString("operator"));
        result.put("workpieceId", requestBody.getString("workpieceId"));
        result.put("quantity", requestBody.getInteger("quantity"));
        result.put("startTime", requestBody.getString("startTime"));
        result.put("endTime", requestBody.getString("endTime"));
        result.put("status", "待开始");
        result.put("assignedBy", requestBody.getString("assignedBy"));
        result.put("assignedAt", Instant.now());
        logger.info("Success to upload task assignment...");
        return result.toString();
    }

    // 61. 查询任务分配记录
    @GetMapping("/tasks/{taskId}")
    public String getTaskAssignment(@PathVariable Long taskId) throws InterruptedException {
        logger.info("Execute get task assignment...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (taskId == null || taskId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的任务ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("taskId", taskId);
            result.put("scheduleId", "SCHEDULE-001");
            result.put("machineId", "MACHINE-001");
            result.put("operator", "张工");
            result.put("workpieceId", "WORKPIECE-001");
            result.put("quantity", 100);
            result.put("startTime", "2025-09-23T08:00:00Z");
            result.put("endTime", "2025-09-23T12:00:00Z");
            result.put("status", "进行中");
            result.put("assignedBy", "生产主管");
            result.put("assignedAt", "2025-09-23T07:30:00Z");
            logger.info("Success to get task assignment...");
        }
        return result.toString();
    }

    // 62. 上传任务完成状态
    @PostMapping("/tasks/{taskId}/complete")
    public String updateTaskCompletion(@PathVariable Long taskId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update task completion...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        if (taskId == null || taskId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的任务ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("taskId", taskId);
            result.put("status", requestBody.getString("status"));
            result.put("completedQuantity", requestBody.getInteger("completedQuantity"));
            result.put("defectQuantity", requestBody.getInteger("defectQuantity"));
            result.put("actualEndTime", requestBody.getString("actualEndTime"));
            result.put("notes", requestBody.getString("notes"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update task completion...");
        }
        return result.toString();
    }

    // 63. 查询任务完成状态
    @GetMapping("/tasks/{taskId}/status")
    public String getTaskStatus(@PathVariable Long taskId) throws InterruptedException {
        logger.info("Execute get task status...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (taskId == null || taskId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的任务ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("taskId", taskId);
            result.put("status", "进行中");
            result.put("progress", 0.65);
            result.put("completedQuantity", 65);
            result.put("defectQuantity", 2);
            result.put("estimatedCompletionTime", "2025-09-23T11:30:00Z");
            logger.info("Success to get task status...");
        }
        return result.toString();
    }

    // 64. 查询设备任务
    @GetMapping("/tasks/machine/{machineId}")
    public String getMachineTasks(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine tasks...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("date", "2025-09-23");
            com.alibaba.fastjson.JSONArray tasks = new com.alibaba.fastjson.JSONArray();
            tasks.add(createTaskObject(1L, "零件A生产", "张工", "08:00-12:00", "进行中", 65));
            tasks.add(createTaskObject(2L, "零件B生产", "李工", "13:00-17:00", "待开始", 0));
            result.put("tasks", tasks);
            logger.info("Success to get machine tasks...");
        }
        return result.toString();
    }

    // 65. 查询操作员任务
    @GetMapping("/tasks/operator/{operator}")
    public String getOperatorTasks(@PathVariable String operator) throws InterruptedException {
        logger.info("Execute get operator tasks...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        if (operator == null || operator.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的操作员");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operator", operator);
            result.put("date", "2025-09-23");
            com.alibaba.fastjson.JSONArray tasks = new com.alibaba.fastjson.JSONArray();
            tasks.add(createOperatorTaskObject(1L, "MACHINE-001", "零件A生产", "08:00-12:00", "进行中", 65));
            tasks.add(createOperatorTaskObject(3L, "MACHINE-003", "零件C生产", "13:00-17:00", "待开始", 0));
            result.put("tasks", tasks);
            logger.info("Success to get operator tasks...");
        }
        return result.toString();
    }

    private com.alibaba.fastjson.JSONObject createScheduleObject(Long id, String name, String startTime, String endTime, String machineId, String status) {
        com.alibaba.fastjson.JSONObject schedule = new com.alibaba.fastjson.JSONObject();
        schedule.put("scheduleId", id);
        schedule.put("planName", name);
        schedule.put("startTime", startTime);
        schedule.put("endTime", endTime);
        schedule.put("machineId", machineId);
        schedule.put("status", status);
        return schedule;
    }

    private com.alibaba.fastjson.JSONObject createTaskObject(Long id, String name, String operator, String timeSlot, String status, Integer progress) {
        com.alibaba.fastjson.JSONObject task = new com.alibaba.fastjson.JSONObject();
        task.put("taskId", id);
        task.put("taskName", name);
        task.put("operator", operator);
        task.put("timeSlot", timeSlot);
        task.put("status", status);
        task.put("progress", progress);
        return task;
    }

    private com.alibaba.fastjson.JSONObject createOperatorTaskObject(Long id, String machineId, String name, String timeSlot, String status, Integer progress) {
        com.alibaba.fastjson.JSONObject task = new com.alibaba.fastjson.JSONObject();
        task.put("taskId", id);
        task.put("machineId", machineId);
        task.put("taskName", name);
        task.put("timeSlot", timeSlot);
        task.put("status", status);
        task.put("progress", progress);
        return task;
    }
}
