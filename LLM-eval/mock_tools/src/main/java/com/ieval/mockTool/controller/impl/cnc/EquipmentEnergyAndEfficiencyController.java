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
@RequestMapping("/cnc/process/efficiency")
public class EquipmentEnergyAndEfficiencyController extends BasedController {

    // 47. 上传能耗数据
    @PostMapping("/power")
    public String uploadPowerConsumption(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload power consumption...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("timestamp", requestBody.getString("timestamp"));
        result.put("powerConsumption", requestBody.getDouble("powerConsumption"));
        result.put("voltage", requestBody.getDouble("voltage"));
        result.put("current", requestBody.getDouble("current"));
        result.put("powerFactor", requestBody.getDouble("powerFactor"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload power consumption...");
        return result.toString();
    }

    // 48. 查询能耗数据
    @GetMapping("/power/{recordId}")
    public String getPowerConsumption(@PathVariable Long recordId) throws InterruptedException {
        logger.info("Execute get power consumption...");
        JSONObject result = new JSONObject();
        
        if (recordId == null || recordId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的记录ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("recordId", recordId);
            result.put("machineId", "MACHINE-001");
            result.put("timestamp", "2025-09-23T10:30:00Z");
            result.put("powerConsumption", 15.5);
            result.put("voltage", 380.0);
            result.put("current", 25.3);
            result.put("powerFactor", 0.92);
            logger.info("Success to get power consumption...");
        }
        return result.toString();
    }

    // 49. 查询总能耗
    @GetMapping("/power/machine/{machineId}/total")
    public String getTotalPowerConsumption(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get total power consumption...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONObject rangeObject=new JSONObject();
            rangeObject.put("start", "2025-09-23T00:00:00Z");
            rangeObject.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange",rangeObject);
            result.put("totalPowerConsumption", 124.5);
            result.put("averagePowerConsumption", 15.6);
            result.put("peakPowerConsumption", 18.2);
            result.put("peakTime", "2025-09-23T14:30:00Z");
            logger.info("Success to get total power consumption...");
        }
        return result.toString();
    }

    // 50. 上传运行效率
    @PostMapping("/efficiency")
    public String uploadEfficiencyData(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload efficiency data...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("timestamp", requestBody.getString("timestamp"));
        result.put("efficiency", requestBody.getDouble("efficiency"));
        result.put("utilizationRate", requestBody.getDouble("utilizationRate"));
        result.put("oee", requestBody.getDouble("oee"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload efficiency data...");
        return result.toString();
    }

    // 51. 查询运行效率
    @GetMapping("/efficiency/{recordId}")
    public String getEfficiencyData(@PathVariable Long recordId) throws InterruptedException {
        logger.info("Execute get efficiency data...");
        JSONObject result = new JSONObject();
        
        if (recordId == null || recordId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的记录ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("recordId", recordId);
            result.put("machineId", "MACHINE-001");
            result.put("timestamp", "2025-09-23T10:30:00Z");
            result.put("efficiency", 0.92);
            result.put("utilizationRate", 0.85);
            result.put("oee", 0.78);
            logger.info("Success to get efficiency data...");
        }
        return result.toString();
    }

    // 52. 查询平均效率
    @GetMapping("/efficiency/machine/{machineId}/average")
    public String getAverageEfficiency(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get average efficiency...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONObject rObjt=new JSONObject();
            rObjt.put("start", "2025-09-23T00:00:00Z");
            rObjt.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange", rObjt);
            result.put("averageEfficiency", 0.90);
            result.put("averageUtilizationRate", 0.83);
            result.put("averageOEE", 0.75);
            result.put("peakEfficiency", 0.95);
            result.put("peakTime", "2025-09-23T09:30:00Z");
            logger.info("Success to get average efficiency...");
        }
        return result.toString();
    }

    // 53. 上传停机记录
    @PostMapping("/downtime")
    public String uploadDowntimeRecord(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload downtime record...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("downtimeId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("startTime", requestBody.getString("startTime"));
        result.put("endTime", requestBody.getString("endTime"));
        result.put("duration", requestBody.getInteger("duration"));
        result.put("reason", requestBody.getString("reason"));
        result.put("category", requestBody.getString("category"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload downtime record...");
        return result.toString();
    }

    // 54. 查询停机记录
    @GetMapping("/downtime/{downtimeId}")
    public String getDowntimeRecord(@PathVariable Long downtimeId) throws InterruptedException {
        logger.info("Execute get downtime record...");
        JSONObject result = new JSONObject();
        
        if (downtimeId == null || downtimeId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的停机记录ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("downtimeId", downtimeId);
            result.put("machineId", "MACHINE-001");
            result.put("startTime", "2025-09-23T10:30:00Z");
            result.put("endTime", "2025-09-23T11:00:00Z");
            result.put("duration", 30);
            result.put("reason", "主轴温度过高");
            result.put("category", "设备故障");
            result.put("status", "已解决");
            logger.info("Success to get downtime record...");
        }
        return result.toString();
    }

    // 55. 查询设备所有停机记录
    @GetMapping("/downtime/machine/{machineId}")
    public String getMachineDowntimeRecords(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get machine downtime records...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONObject rObjt=new JSONObject();
            rObjt.put("start", "2025-09-23T00:00:00Z");
            rObjt.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange", rObjt);
            result.put("totalDowntime", 45);
            JSONArray recodrsArray=new JSONArray();
            recodrsArray.add(createDowntimeRecordObject(1L, "2025-09-23T10:30:00Z", "2025-09-23T11:00:00Z", 30, "主轴温度过高", "设备故障"));
            recodrsArray.add(createDowntimeRecordObject(2L, "2025-09-23T14:15:00Z", "2025-09-23T14:30:00Z", 15, "更换刀具", "计划性维护"));
            result.put("records",recodrsArray);
            logger.info("Success to get machine downtime records...");
        }
        return result.toString();
    }

    // 56. 查询综合效率报表
    @GetMapping("/report/machine/{machineId}")
    public String getEfficiencyReport(@PathVariable String machineId) throws InterruptedException {
        logger.info("Execute get efficiency report...");
        JSONObject result = new JSONObject();
        
        if (machineId == null || machineId.isEmpty()) {
            result.put("code", 400);
            result.put("message", "无效的设备ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("machineId", machineId);
            JSONObject rObjt=new JSONObject();
            rObjt.put("start", "2025-09-23T00:00:00Z");
            rObjt.put("end", "2025-09-23T23:59:59Z");
            result.put("dateRange", rObjt);
            JSONObject summObject=new JSONObject();
            summObject.put("oee", 0.78);
            summObject.put("availability", 0.92);
            summObject.put("performance", 0.85);
            summObject.put("quality", 0.99);
            summObject.put("totalPowerConsumption", 124.5);
            summObject.put("totalDowntime", 45);
            result.put("summary", summObject);
            JSONArray detailsArray=new JSONArray();
            detailsArray.add(createShiftEfficiencyObject("早班", 0.80, 0.95, 0.87, 0.97, 62.3, 20));
            detailsArray.add(createShiftEfficiencyObject("中班", 0.76, 0.89, 0.83, 0.99, 62.2, 25));
            result.put("details",detailsArray);
            logger.info("Success to get efficiency report...");
        }
        return result.toString();
    }

    private JSONObject createDowntimeRecordObject(Long id, String startTime, String endTime, Integer duration, String reason, String category) {
        JSONObject record = new JSONObject();
        record.put("downtimeId", id);
        record.put("startTime", startTime);
        record.put("endTime", endTime);
        record.put("duration", duration);
        record.put("reason", reason);
        record.put("category", category);
        record.put("status", "已解决");
        return record;
    }

    private JSONObject createShiftEfficiencyObject(String shift, Double oee, Double availability, Double performance, Double quality, Double powerConsumption, Integer downtime) {
        JSONObject record = new JSONObject();
        record.put("shift", shift);
        record.put("oee", oee);
        record.put("availability", availability);
        record.put("performance", performance);
        record.put("quality", quality);
        record.put("powerConsumption", powerConsumption);
        record.put("downtime", downtime);
        return record;
    }
}
