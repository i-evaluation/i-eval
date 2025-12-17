package com.ieval.mockTool.controller.impl.cnc;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process")
public class ProcessingLogAndReportController extends BasedController {

    // 42. 上传加工日志
    @PostMapping("/logs")
    public String uploadProcessingLog(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload processing log...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("logId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("operator", requestBody.getString("operator"));
        result.put("workpieceId", requestBody.getString("workpieceId"));
        result.put("processType", requestBody.getString("processType"));
        result.put("startTime", requestBody.getString("startTime"));
        result.put("endTime", requestBody.getString("endTime"));
        result.put("parameters", requestBody.getJSONObject("parameters"));
        result.put("status", requestBody.getString("status"));
        result.put("notes", requestBody.getString("notes"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload processing log...");
        return result.toString();
    }

    // 43. 查询加工日志
    @GetMapping("/logs/{logId}")
    public String getProcessingLog(@PathVariable Long logId) throws InterruptedException {
        logger.info("Execute get processing log...");
        JSONObject result = new JSONObject();
        
        if (logId == null || logId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的日志ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("logId", logId);
            result.put("machineId", "MACHINE-001");
            result.put("operator", "张工");
            result.put("workpieceId", "WORKPIECE-001");
            result.put("processType", "铣削");
            result.put("startTime", "2025-09-23T08:00:00Z");
            result.put("endTime", "2025-09-23T10:30:00Z");
            JSONObject parameters = new JSONObject();
            parameters.put("spindleSpeed", 12000);
            parameters.put("feedRate", 500);
            parameters.put("cuttingDepth", 2.5);
            result.put("parameters", parameters);
            result.put("status", "完成");
            result.put("notes", "加工过程正常");
            logger.info("Success to get processing log...");
        }
        return result.toString();
    }

    // 44. 上传生产报表
    @PostMapping("/reports")
    public String uploadProductionReport(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload production report...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("reportId", System.currentTimeMillis());
        result.put("reportType", requestBody.getString("reportType"));
        result.put("dateRange", requestBody.getJSONObject("dateRange"));
        result.put("machines", requestBody.getJSONArray("machines"));
        result.put("summary", requestBody.getJSONObject("summary"));
        result.put("details", requestBody.getJSONArray("details"));
        result.put("generatedBy", requestBody.getString("generatedBy"));
        result.put("generatedAt", Instant.now());
        logger.info("Success to upload production report...");
        return result.toString();
    }

    // 45. 查询生产报表
    @GetMapping("/reports/{reportId}")
    public String getProductionReport(@PathVariable Long reportId) throws InterruptedException {
        logger.info("Execute get production report...");
        JSONObject result = new JSONObject();
        
        if (reportId == null || reportId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的报表ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("reportId", reportId);
            result.put("reportType", "日报");
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange",dateRange);
            JSONArray  machines=new JSONArray();
            machines.add("MACHINE-001");
            machines.add("MACHINE-002");
            result.put("machines",machines);
            JSONObject summary = new JSONObject();
            summary.put("totalWorkpieces", 120);
            summary.put("completedWorkpieces", 115);
            summary.put("defectRate", 0.042);
            summary.put("totalOperatingTime", 18.5);
            result.put("summary", summary);
            JSONArray details=new JSONArray();
            details.add(createMachineReportObject("MACHINE-001", 60, 58, 0.033, 9.2));
            details.add(createMachineReportObject("MACHINE-002", 60, 57, 0.050, 9.3));
            result.put("details", details);
            result.put("generatedBy", "系统管理员");
            result.put("generatedAt", "2025-09-23T23:45:00Z");
            logger.info("Success to get production report...");
        }
        return result.toString();
    }

    // 46. 查询设备生产报表
    @GetMapping("/reports/machine/{machineId}")
    public String getMachineProductionReport(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine production report...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            result.put("reportType", "设备日报");
            JSONObject dateRange=new JSONObject();
            dateRange.put("start", "2025-09-23T00:00:00Z");
            dateRange.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange", dateRange);
            JSONObject summary=new JSONObject();
            summary.put("totalWorkpieces", 60);
            summary.put("completedWorkpieces", 58);
            summary.put("defectRate", 0.033);
            summary.put("totalOperatingTime", 9.2);
            summary.put("efficiency", 0.92);
            result.put("summary", summary);
            JSONArray details=new JSONArray();
            details.add(createShiftReportObject("早班", 30, 29, 0.033, 4.6));
            details.add(createShiftReportObject("中班", 30, 29, 0.033, 4.6));
            result.put("details", details);
            logger.info("Success to get machine production report...");
        }
        return result.toString();
    }

    private JSONObject createMachineReportObject(String machineId, Integer total, Integer completed, Double defectRate, Double operatingTime) {
        JSONObject report = new JSONObject();
        report.put("machineId", machineId);
        report.put("totalWorkpieces", total);
        report.put("completedWorkpieces", completed);
        report.put("defectRate", defectRate);
        report.put("totalOperatingTime", operatingTime);
        return report;
    }

    private JSONObject createShiftReportObject(String shift, Integer total, Integer completed, Double defectRate, Double operatingTime) {
        JSONObject report = new JSONObject();
        report.put("shift", shift);
        report.put("totalWorkpieces", total);
        report.put("completedWorkpieces", completed);
        report.put("defectRate", defectRate);
        report.put("operatingTime", operatingTime);
        return report;
    }
}
