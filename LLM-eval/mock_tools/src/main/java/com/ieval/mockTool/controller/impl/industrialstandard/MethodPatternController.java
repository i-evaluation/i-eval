package com.ieval.mockTool.controller.impl.industrialstandard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/api/method")
public class MethodPatternController extends BasedController {

    private final Map<String, JSONObject> productionOrders = new ConcurrentHashMap<>();
    private final Map<String, JSONObject> devices = new ConcurrentHashMap<>();

    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static JSONObject ok(String message, JSONObject data) {
        JSONObject resp = new JSONObject();
        resp.put("code", 0);
        resp.put("message", message);
        if (data != null) resp.put("data", data);
        return resp;
    }

    private static JSONObject err(String message) {
        JSONObject resp = new JSONObject();
        resp.put("code", -1);
        resp.put("message", message);
        return resp;
    }

    @PostMapping("/update_production_order")
    public JSONObject updateProductionOrder(@RequestBody String reqBody) {
        JSONObject body = safeParse(reqBody);
        String orderId = body.getString("order_id");
        if (orderId == null || orderId.trim().isEmpty()) {
            return err("'order_id' is required");
        }

        JSONObject record = productionOrders.getOrDefault(orderId, new JSONObject());
        record.put("order_id", orderId);
        record.putIfAbsent("created_at", now());
        record.put("status", "in_progress");
        record.put("updated_at", now());
        productionOrders.put(orderId, record);

        logger.info("update_production_order -> {}", record.toJSONString());
        return ok("Production order updated", record);
    }

    @PostMapping("/update_device")
    public JSONObject updateDevice(@RequestBody String reqBody) {
        JSONObject body = safeParse(reqBody);
        String deviceId = body.getString("device_id");
        if (deviceId == null || deviceId.trim().isEmpty()) {
            return err("'device_id' is required");
        }

        JSONObject device = devices.getOrDefault(deviceId, new JSONObject());
        device.put("device_id", deviceId);
        device.putIfAbsent("created_at", now());
        device.put("status", "running");
        device.put("updated_at", now());
        devices.put(deviceId, device);

        logger.info("update_device -> {}", device.toJSONString());
        return ok("Device updated", device);
    }

    @PostMapping("/create_quality_check")
    public JSONObject createQualityCheck(@RequestBody String reqBody) {
    	
        JSONObject body = safeParse(reqBody);
        String orderId = body.getString("order_id");
        if (orderId == null || orderId.trim().isEmpty()) {
            return err("'order_id' is required");
        }
        
        // 模拟约5秒处理时间
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        JSONObject check = new JSONObject();
        int n = body.containsKey("_n") ? body.getIntValue("_n") : 1; // 简单示例ID，可按需替换
        check.put("id", String.format("qc_%06d", n));
        check.put("order_id", orderId);
        check.put("check_item", "auto-check");
        check.put("result", "pass");
        check.put("created_at", now());

        logger.info("create_quality_check -> {}", check.toJSONString());
        return ok("Quality check created", check);
    }

    private static JSONObject safeParse(String reqBody) {
        try {
            return JSONObject.parseObject(reqBody == null ? "{}" : reqBody);
        } catch (Exception e) {
            return new JSONObject();
        }
    }
}


