package com.ieval.mockTool.controller.impl.cnc;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/monitor/machines")
public class ProcessingMonitoringController extends BasedController {

    // 71. 查询设备运行状态
    @GetMapping("/{machineId}/status")
    public String getMachineStatus(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine status...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("status", "运行中");
            result.put("mode", "自动");
            result.put("currentProgram", "PROGRAM-001");
            result.put("startTime", "2025-09-23T08:00:00Z");
            result.put("runningTime", 2.5);
            result.put("estimatedCompletionTime", "2025-09-23T12:00:00Z");
            result.put("progress", 0.65);
            logger.info("Success to get machine status...");
        }
        return result.toString();
    }

    // 72. 上传运行参数
    @PostMapping("/{machineId}/parameters")
    public String uploadOperatingParameters(@PathVariable String machineId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload operating parameters...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("timestamp", Instant.now());
            result.put("parameters", requestBody.getJSONObject("parameters"));
            result.put("uploadedBy", requestBody.getString("uploadedBy"));
            logger.info("Success to upload operating parameters...");
        }
        return result.toString();
    }

    // 73. 查询运行参数
    @GetMapping("/{machineId}/parameters")
    public String getOperatingParameters(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get operating parameters...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("timestamp", "2025-09-23T10:30:00Z");
            JSONObject parameters=new JSONObject();
            parameters.put("spindleSpeed", 12000);
            parameters.put("feedRate", 500);
            parameters.put("cuttingDepth", 2.5);
            parameters.put("coolantFlow", 5.0);
            parameters.put("toolNumber", 3);
            result.put("parameters", parameters);
            logger.info("Success to get operating parameters...");
        }
        return result.toString();
    }

    // 74. 查询运行历史
    @GetMapping("/{machineId}/history")
    public String getMachineHistory(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine history...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange",dateRange);
            JSONArray history=new JSONArray();
            history.add(createHistoryRecordObject("08:00-12:00", "运行中", "PROGRAM-001", 4.0, 100));
            history.add(createHistoryRecordObject("13:00-17:00", "运行中", "PROGRAM-002", 4.0, 95));
            result.put("history", history);
            logger.info("Success to get machine history...");
        }
        return result.toString();
    }

    // 75. 上传设备告警
    @PostMapping("/{machineId}/alerts")
    public String uploadMachineAlert(@PathVariable String machineId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload machine alert...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("alertId", System.currentTimeMillis());
            result.put("alertType", requestBody.getString("alertType"));
            result.put("severity", requestBody.getString("severity"));
            result.put("message", requestBody.getString("message"));
            result.put("timestamp", Instant.now());
            result.put("status", "未处理");
            logger.info("Success to upload machine alert...");
        }
        return result.toString();
    }

    // 76. 查询设备告警
    @GetMapping("/{machineId}/alerts")
    public String getMachineAlerts(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine alerts...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONArray alerts=new JSONArray();
            alerts.add(createAlertObject(1L, "温度告警", "高", "主轴温度超过阈值", "2025-09-23T10:30:00Z", "未处理"));
            alerts.add(createAlertObject(2L, "振动告警", "中", "主轴振动异常", "2025-09-23T11:15:00Z", "已处理"));
            result.put("alerts", alerts);
            logger.info("Success to get machine alerts...");
        }
        return result.toString();
    }

    // 77. 查询设备日志
    @GetMapping("/{machineId}/logs")
    public String getMachineLogs(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine logs...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange", dateRange);
            JSONArray logs=new JSONArray();
            logs.add(createLogObject("2025-09-23T08:00:00Z", "系统启动", "信息"));
            logs.add(createLogObject("2025-09-23T10:30:00Z", "主轴温度过高", "警告"));
            logs.add(createLogObject("2025-09-23T11:15:00Z", "主轴振动异常", "警告"));
            result.put("logs", logs);
            logger.info("Success to get machine logs...");
        }
        return result.toString();
    }

    // 78. 上传温度信息
    @PostMapping("/{machineId}/temperature")
    public String uploadTemperatureData(@PathVariable String machineId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload temperature data...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("timestamp", Instant.now());
            result.put("spindleTemperature", requestBody.getDouble("spindleTemperature"));
            result.put("motorTemperature", requestBody.getDouble("motorTemperature"));
            result.put("ambientTemperature", requestBody.getDouble("ambientTemperature"));
            logger.info("Success to upload temperature data...");
        }
        return result.toString();
    }

    // 79. 查询温度信息
    @GetMapping("/{machineId}/temperature")
    public String getTemperatureData(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get temperature data...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("timestamp", "2025-09-23T10:30:00Z");
            result.put("spindleTemperature", 45.2);
            result.put("motorTemperature", 38.5);
            result.put("ambientTemperature", 25.0);
            logger.info("Success to get temperature data...");
        }
        return result.toString();
    }

    // 80. 上传振动数据
    @PostMapping("/{machineId}/vibration")
    public String uploadVibrationData(@PathVariable String machineId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload vibration data...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("timestamp", Instant.now());
            result.put("spindleVibration", requestBody.getDouble("spindleVibration"));
            result.put("motorVibration", requestBody.getDouble("motorVibration"));
            result.put("frameVibration", requestBody.getDouble("frameVibration"));
            logger.info("Success to upload vibration data...");
        }
        return result.toString();
    }

    // 81. 查询振动数据
    @GetMapping("/{machineId}/vibration")
    public String getVibrationData(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get vibration data...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("timestamp", "2025-09-23T10:30:00Z");
            result.put("spindleVibration", 0.05);
            result.put("motorVibration", 0.03);
            result.put("frameVibration", 0.02);
            logger.info("Success to get vibration data...");
        }
        return result.toString();
    }

    private JSONObject createHistoryRecordObject(String timeSlot, String status, String program, Double duration, Integer progress) {
        JSONObject record = new JSONObject();
        record.put("timeSlot", timeSlot);
        record.put("status", status);
        record.put("program", program);
        record.put("duration", duration);
        record.put("progress", progress);
        return record;
    }

    private JSONObject createAlertObject(Long id, String type, String severity, String message, String timestamp, String status) {
        JSONObject alert = new JSONObject();
        alert.put("alertId", id);
        alert.put("alertType", type);
        alert.put("severity", severity);
        alert.put("message", message);
        alert.put("timestamp", timestamp);
        alert.put("status", status);
        return alert;
    }

    private JSONObject createLogObject(String timestamp, String message, String level) {
        JSONObject log = new JSONObject();
        log.put("timestamp", timestamp);
        log.put("message", message);
        log.put("level", level);
        return log;
    }
}
