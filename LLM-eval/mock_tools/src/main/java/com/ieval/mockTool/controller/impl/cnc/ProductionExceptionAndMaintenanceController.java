package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/maintenance")
public class ProductionExceptionAndMaintenanceController extends BasedController {

    // 32. 上传生产异常
    @PostMapping("/exceptions")
    public String uploadException(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload exception...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("exceptionId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("exceptionType", requestBody.getString("exceptionType"));
        result.put("description", requestBody.getString("description"));
        result.put("severity", requestBody.getString("severity"));
        result.put("occurredAt", requestBody.getString("occurredAt"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload exception...");
        return result.toString();
    }

    // 33. 查询异常
    @GetMapping("/exceptions/{exceptionId}")
    public String getException(@PathVariable Long exceptionId) throws InterruptedException {
        logger.info("Execute get exception...");
        JSONObject result = new JSONObject();
        
        if (exceptionId == null || exceptionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的异常ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("exceptionId", exceptionId);
            result.put("machineId", "MACHINE-001");
            result.put("exceptionType", "设备故障");
            result.put("description", "主轴温度过高");
            result.put("severity", "高");
            result.put("occurredAt", "2025-09-23T10:30:00Z");
            result.put("status", "待处理");
            logger.info("Success to get exception...");
        }
        return result.toString();
    }

    // 34. 查询设备异常
    @GetMapping("/exceptions/machine/{machineId}")
    public String getMachineExceptions(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine exceptions...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONArray exceptions = new JSONArray();
            exceptions.add(createExceptionObject(1L, "主轴温度过高", "高", "2025-09-23T10:30:00Z"));
            exceptions.add(createExceptionObject(2L, "冷却液不足", "中", "2025-09-23T11:15:00Z"));
            result.put("exceptions", exceptions);
            logger.info("Success to get machine exceptions...");
        }
        return result.toString();
    }

    // 35. 上传维修记录
    @PostMapping("/repairs")
    public String uploadRepairRecord(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload repair record...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("repairId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("exceptionId", requestBody.getString("exceptionId"));
        result.put("repairType", requestBody.getString("repairType"));
        result.put("description", requestBody.getString("description"));
        result.put("technician", requestBody.getString("technician"));
        result.put("duration", requestBody.getInteger("duration"));
        result.put("repairedAt", requestBody.getString("repairedAt"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload repair record...");
        return result.toString();
    }

    // 36. 查询维修记录
    @GetMapping("/repairs/{repairId}")
    public String getRepairRecord(@PathVariable Long repairId) throws InterruptedException {
        logger.info("Execute get repair record...");
        JSONObject result = new JSONObject();
        
        if (repairId == null || repairId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的维修ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("repairId", repairId);
            result.put("machineId", "MACHINE-001");
            result.put("exceptionId", "EXCEPTION-001");
            result.put("repairType", "更换零件");
            result.put("description", "更换主轴轴承");
            result.put("technician", "张工");
            result.put("duration", 120);
            result.put("repairedAt", "2025-09-23T12:00:00Z");
            result.put("status", "已完成");
            logger.info("Success to get repair record...");
        }
        return result.toString();
    }

    // 37. 查询设备维修记录
    @GetMapping("/repairs/machine/{machineId}")
    public String getMachineRepairRecords(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine repair records...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONArray repairs = new JSONArray();
            repairs.add(createRepairRecordObject(1L, "更换主轴轴承", "张工", 120, "2025-09-23T12:00:00Z"));
            repairs.add(createRepairRecordObject(2L, "添加冷却液", "李工", 30, "2025-09-22T14:30:00Z"));
            result.put("repairs", repairs);
            logger.info("Success to get machine repair records...");
        }
        return result.toString();
    }

    // 38. 上传异常处理结果
    @PostMapping("/exceptions/{exceptionId}/resolution")
    public String uploadExceptionResolution(@PathVariable Long exceptionId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload exception resolution...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (exceptionId == null || exceptionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的异常ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("exceptionId", exceptionId);
            result.put("resolution", requestBody.getString("resolution"));
            result.put("resolvedBy", requestBody.getString("resolvedBy"));
            result.put("resolvedAt", requestBody.getString("resolvedAt"));
            result.put("status", "已解决");
            result.put("updatedAt", Instant.now());
            logger.info("Success to upload exception resolution...");
        }
        return result.toString();
    }

    // 39. 查询异常处理结果
    @GetMapping("/exceptions/{exceptionId}/resolution")
    public String getExceptionResolution(@PathVariable Long exceptionId) throws InterruptedException {
        logger.info("Execute get exception resolution...");
        JSONObject result = new JSONObject();
        
        if (exceptionId == null || exceptionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的异常ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("exceptionId", exceptionId);
            result.put("resolution", "更换主轴轴承并添加润滑剂");
            result.put("resolvedBy", "张工");
            result.put("resolvedAt", "2025-09-23T12:30:00Z");
            result.put("status", "已解决");
            logger.info("Success to get exception resolution...");
        }
        return result.toString();
    }

    // 40. 上传维修计划
    @PostMapping("/plans")
    public String uploadMaintenancePlan(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload maintenance plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("planId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("planType", requestBody.getString("planType"));
        result.put("description", requestBody.getString("description"));
        result.put("scheduledDate", requestBody.getString("scheduledDate"));
        result.put("technician", requestBody.getString("technician"));
        result.put("estimatedDuration", requestBody.getInteger("estimatedDuration"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload maintenance plan...");
        return result.toString();
    }

    // 41. 查询维修计划
    @GetMapping("/plans/{planId}")
    public String getMaintenancePlan(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get maintenance plan...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("machineId", "MACHINE-001");
            result.put("planType", "定期保养");
            result.put("description", "主轴轴承润滑和检查");
            result.put("scheduledDate", "2025-10-01T09:00:00Z");
            result.put("technician", "张工");
            result.put("estimatedDuration", 180);
            result.put("status", "计划中");
            logger.info("Success to get maintenance plan...");
        }
        return result.toString();
    }

    private JSONObject createExceptionObject(Long id, String description, String severity, String occurredAt) {
        JSONObject exception = new JSONObject();
        exception.put("exceptionId", id);
        exception.put("description", description);
        exception.put("severity", severity);
        exception.put("occurredAt", occurredAt);
        exception.put("status", "待处理");
        return exception;
    }

    private JSONObject createRepairRecordObject(Long id, String description, String technician, Integer duration, String repairedAt) {
        JSONObject repair = new JSONObject();
        repair.put("repairId", id);
        repair.put("description", description);
        repair.put("technician", technician);
        repair.put("duration", duration);
        repair.put("repairedAt", repairedAt);
        repair.put("status", "已完成");
        return repair;
    }
}
