package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;
@RestController
@RequestMapping("/cnc/process/operators")
public class OperatorManagementController extends BasedController {

    // 91. 上传操作员信息
    @PostMapping
    public String uploadOperatorInfo(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload operator info...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("operatorId", System.currentTimeMillis());
        result.put("name", requestBody.getString("name"));
        result.put("employeeId", requestBody.getString("employeeId"));
        result.put("department", requestBody.getString("department"));
        result.put("position", requestBody.getString("position"));
        result.put("skills", requestBody.getJSONArray("skills"));
        result.put("certifications", requestBody.getJSONArray("certifications"));
        result.put("contact", requestBody.getJSONObject("contact"));
        result.put("status", "在职");
        result.put("createdAt", Instant.now());
        logger.info("Success to upload operator info...");
        return result.toString();
    }

    // 92. 查询操作员信息
    @GetMapping("/{operatorId}")
    public String getOperatorInfo(@PathVariable Long operatorId) throws InterruptedException {
        logger.info("Execute get operator info...");
        JSONObject result = new JSONObject();
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            result.put("name", "张工");
            result.put("employeeId", "EMP001");
            result.put("department", "生产部");
            result.put("position", "高级操作员");
            JSONArray skillsArray=new JSONArray();
            skillsArray.add("CNC编程");
            skillsArray.add("设备操作");
            skillsArray.add("质量检测");
            result.put("skills", skillsArray);

            JSONArray certifications=new JSONArray();
            certifications.add("CNC操作证书");
            certifications.add("安全培训证书");
            result.put("certifications", certifications);
            JSONObject contact= new JSONObject();
            contact.put("phone", "13800138000");
            contact.put("email", "zhanggong@example.com");
            result.put("contact",contact);
            result.put("status", "在职");
            result.put("joinDate", "2020-05-15");
            logger.info("Success to get operator info...");
        }
        return result.toString();
    }

    // 93. 查询所有操作员
    @GetMapping
    public String getAllOperators() throws InterruptedException {
        logger.info("Execute get all operators...");
        JSONObject result = new JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        JSONArray operators=new JSONArray();
        operators.add(createOperatorObject(1L, "张工", "EMP001", "高级操作员", "在职"));
        operators.add(createOperatorObject(2L, "李工", "EMP002", "中级操作员", "在职"));
        operators.add(createOperatorObject(3L, "王工", "EMP003", "初级操作员", "在职"));
        result.put("operators", operators);
        logger.info("Success to get all operators...");
        return result.toString();
    }

    // 94. 查询操作员任务记录
    @GetMapping("/{operatorId}/tasks")
    public String getOperatorTaskRecords(@PathVariable Long operatorId) throws InterruptedException {
        logger.info("Execute get operator task records...");
        JSONObject result = new JSONObject();
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange", dateRange);
            JSONArray tasks=new JSONArray();
            tasks.add(createOperatorTaskRecordObject(1L, "MACHINE-001", "零件A生产", "08:00-12:00", "完成", 100));
            tasks.add(createOperatorTaskRecordObject(2L, "MACHINE-002", "零件B生产", "13:00-17:00", "进行中", 65));
            result.put("tasks", tasks);
            logger.info("Success to get operator task records...");
        }
        return result.toString();
    }

    // 95. 查询操作员操作日志
    @GetMapping("/{operatorId}/logs")
    public String getOperatorLogs(@PathVariable Long operatorId) throws InterruptedException {
        logger.info("Execute get operator logs...");
        JSONObject result = new JSONObject();
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange",dateRange);
            JSONArray  logs=new JSONArray();
            logs.add(createOperatorLogObject("2025-09-23T08:00:00Z", "登录系统", "MACHINE-001"));
            logs.add(createOperatorLogObject("2025-09-23T08:05:00Z", "启动加工程序", "MACHINE-001"));
            logs.add(createOperatorLogObject("2025-09-23T12:00:00Z", "完成生产任务", "MACHINE-001"));
            result.put("logs",logs);
            logger.info("Success to get operator logs...");
        }
        return result.toString();
    }

    // 96. 查询操作员绩效数据
    @GetMapping("/{operatorId}/performance")
    public String getOperatorPerformance(@PathVariable Long operatorId) throws InterruptedException {
        logger.info("Execute get operator performance...");
        JSONObject result = new JSONObject();
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange",dateRange);
            JSONObject performance=new JSONObject();
            performance.put("totalTasks", 45);
            performance.put("completedTasks", 43);
            performance.put("completionRate", 0.96);
            performance.put("averageEfficiency", 0.92);
            performance.put("totalWorkpieces", 2150);
            performance.put("defectRate", 0.015);
            performance.put("totalOperatingTime", 180.5);
            result.put("performance", performance);
            logger.info("Success to get operator performance...");
        }
        return result.toString();
    }

    // 97. 上传操作员告警信息
    @PostMapping("/{operatorId}/alerts")
    public String uploadOperatorAlert(@PathVariable Long operatorId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload operator alert...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            result.put("alertId", System.currentTimeMillis());
            result.put("alertType", requestBody.getString("alertType"));
            result.put("severity", requestBody.getString("severity"));
            result.put("message", requestBody.getString("message"));
            result.put("machineId", requestBody.getString("machineId"));
            result.put("timestamp", Instant.now());
            result.put("status", "未处理");
            logger.info("Success to upload operator alert...");
        }
        return result.toString();
    }

    // 98. 查询操作员告警信息
    @GetMapping("/{operatorId}/alerts")
    public String getOperatorAlerts(@PathVariable Long operatorId) throws InterruptedException {
        logger.info("Execute get operator alerts...");
        JSONObject result = new JSONObject();
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            JSONArray alters=new JSONArray();
            alters.add(createOperatorAlertObject(1L, "操作错误", "高", "未按规程操作设备", "MACHINE-001", "2025-09-23T10:30:00Z", "未处理"));
            alters.add(createOperatorAlertObject(2L, "安全警告", "中", "未佩戴安全装备", "MACHINE-002", "2025-09-23T11:15:00Z", "已处理"));
            result.put("alerts",alters);
            logger.info("Success to get operator alerts...");
        }
        return result.toString();
    }

    // 99. 上传培训记录
    @PostMapping("/{operatorId}/training")
    public String uploadTrainingRecord(@PathVariable Long operatorId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload training record...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            result.put("trainingId", System.currentTimeMillis());
            result.put("trainingName", requestBody.getString("trainingName"));
            result.put("trainingType", requestBody.getString("trainingType"));
            result.put("trainingDate", requestBody.getString("trainingDate"));
            result.put("duration", requestBody.getInteger("duration"));
            result.put("instructor", requestBody.getString("instructor"));
            result.put("result", requestBody.getString("result"));
            result.put("certificate", requestBody.getString("certificate"));
            result.put("createdAt", Instant.now());
            logger.info("Success to upload training record...");
        }
        return result.toString();
    }

    // 100. 查询培训记录
    @GetMapping("/{operatorId}/training")
    public String getTrainingRecords(@PathVariable Long operatorId) throws InterruptedException {
        logger.info("Execute get training records...");
        JSONObject result = new JSONObject();
        
        if (operatorId == null || operatorId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的操作员ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("operatorId", operatorId);
            JSONArray trainings=new JSONArray();
            trainings.add(createTrainingRecordObject(1L, "CNC高级操作培训", "技能提升", "2025-08-15", 8, "王教练", "优秀", "CERT-001"));
            trainings.add(createTrainingRecordObject(2L, "安全生产培训", "安全培训", "2025-07-20", 4, "李教练", "合格", "CERT-002"));
            result.put("trainings", trainings);
            logger.info("Success to get training records...");
        }
        return result.toString();
    }

    private JSONObject createOperatorObject(Long id, String name, String employeeId, String position, String status) {
        JSONObject operator = new JSONObject();
        operator.put("operatorId", id);
        operator.put("name", name);
        operator.put("employeeId", employeeId);
        operator.put("position", position);
        operator.put("status", status);
        return operator;
    }

    private JSONObject createOperatorTaskRecordObject(Long id, String machineId, String taskName, String timeSlot, String status, Integer progress) {
        JSONObject task = new JSONObject();
        task.put("taskId", id);
        task.put("machineId", machineId);
        task.put("taskName", taskName);
        task.put("timeSlot", timeSlot);
        task.put("status", status);
        task.put("progress", progress);
        return task;
    }

    private JSONObject createOperatorLogObject(String timestamp, String action, String machineId) {
        JSONObject log = new JSONObject();
        log.put("timestamp", timestamp);
        log.put("action", action);
        log.put("machineId", machineId);
        return log;
    }

    private JSONObject createOperatorAlertObject(Long id, String type, String severity, String message, String machineId, String timestamp, String status) {
        JSONObject alert = new JSONObject();
        alert.put("alertId", id);
        alert.put("alertType", type);
        alert.put("severity", severity);
        alert.put("message", message);
        alert.put("machineId", machineId);
        alert.put("timestamp", timestamp);
        alert.put("status", status);
        return alert;
    }

    private JSONObject createTrainingRecordObject(Long id, String name, String type, String date, Integer duration, String instructor, String result, String certificate) {
        JSONObject training = new JSONObject();
        training.put("trainingId", id);
        training.put("trainingName", name);
        training.put("trainingType", type);
        training.put("trainingDate", date);
        training.put("duration", duration);
        training.put("instructor", instructor);
        training.put("result", result);
        training.put("certificate", certificate);
        return training;
    }
}
