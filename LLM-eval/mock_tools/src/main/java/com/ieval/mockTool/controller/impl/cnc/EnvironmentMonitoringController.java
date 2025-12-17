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

import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/environment")
public class EnvironmentMonitoringController extends BasedController {

    // 129. 上传环境温度数据
    @PostMapping("/temperature")
    public String uploadEnvironmentTemperature(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload environment temperature...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("temperature", requestBody.getDouble("temperature"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload environment temperature...");
        return result.toString();
    }

    // 130. 查询环境温度数据
    @GetMapping("/temperature")
    public String getEnvironmentTemperature() throws InterruptedException {
        logger.info("Execute get environment temperature...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("temperature", 25.5);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        result.put("status", "正常");
        logger.info("Success to get environment temperature...");
        return result.toString();
    }

    // 131. 上传环境湿度数据
    @PostMapping("/humidity")
    public String uploadEnvironmentHumidity(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload environment humidity...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("humidity", requestBody.getDouble("humidity"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload environment humidity...");
        return result.toString();
    }

    // 132. 查询环境湿度数据
    @GetMapping("/humidity")
    public String getEnvironmentHumidity() throws InterruptedException {
        logger.info("Execute get environment humidity...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("humidity", 65.0);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        result.put("status", "正常");
        logger.info("Success to get environment humidity...");
        return result.toString();
    }

    // 133. 上传空气质量数据
    @PostMapping("/air-quality")
    public String uploadAirQuality(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload air quality...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("pm25", requestBody.getDouble("pm25"));
        result.put("pm10", requestBody.getDouble("pm10"));
        result.put("co2", requestBody.getDouble("co2"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload air quality...");
        return result.toString();
    }

    // 134. 查询空气质量数据
    @GetMapping("/air-quality")
    public String getAirQuality() throws InterruptedException {
        logger.info("Execute get air quality...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("pm25", 35.0);
        result.put("pm10", 50.0);
        result.put("co2", 450.0);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        result.put("status", "良好");
        logger.info("Success to get air quality...");
        return result.toString();
    }

    // 135. 上传环境振动数据
    @PostMapping("/vibration")
    public String uploadEnvironmentVibration(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload environment vibration...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("vibration", requestBody.getDouble("vibration"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload environment vibration...");
        return result.toString();
    }

    // 136. 查询环境振动数据
    @GetMapping("/vibration")
    public String getEnvironmentVibration() throws InterruptedException {
        logger.info("Execute get environment vibration...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("vibration", 0.02);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        result.put("status", "正常");
        logger.info("Success to get environment vibration...");
        return result.toString();
    }

    // 137. 上传环境噪声数据
    @PostMapping("/noise")
    public String uploadEnvironmentNoise(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload environment noise...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("noise", requestBody.getDouble("noise"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload environment noise...");
        return result.toString();
    }

    // 138. 查询环境噪声数据
    @GetMapping("/noise")
    public String getEnvironmentNoise() throws InterruptedException {
        logger.info("Execute get environment noise...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("noise", 75.0);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        result.put("status", "正常");
        logger.info("Success to get environment noise...");
        return result.toString();
    }

    // 139. 上传光照数据
    @PostMapping("/light")
    public String uploadEnvironmentLight(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload environment light...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("illuminance", requestBody.getDouble("illuminance"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload environment light...");
        return result.toString();
    }

    // 140. 查询光照数据
    @GetMapping("/light")
    public String getEnvironmentLight() throws InterruptedException {
        logger.info("Execute get environment light...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("illuminance", 500.0);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        result.put("status", "正常");
        logger.info("Success to get environment light...");
        return result.toString();
    }

    // 141. 上传环境电能消耗
    @PostMapping("/power")
    public String uploadEnvironmentPower(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload environment power...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("recordId", System.currentTimeMillis());
        result.put("location", requestBody.getString("location"));
        result.put("powerConsumption", requestBody.getDouble("powerConsumption"));
        result.put("timestamp", Instant.now());
        logger.info("Success to upload environment power...");
        return result.toString();
    }

    // 142. 查询环境电能消耗
    @GetMapping("/power")
    public String getEnvironmentPower() throws InterruptedException {
        logger.info("Execute get environment power...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("powerConsumption", 1250.0);
        result.put("timestamp", "2025-09-23T10:30:00Z");
        logger.info("Success to get environment power...");
        return result.toString();
    }

    // 143. 查询环境监控汇总
    @GetMapping("/summary")
    public String getEnvironmentSummary() throws InterruptedException {
        logger.info("Execute get environment summary...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("location", "生产车间");
        result.put("timestamp", "2025-09-23T10:30:00Z");
        com.alibaba.fastjson.JSONObject temperatureObject = new com.alibaba.fastjson.JSONObject();
        temperatureObject.put("value", 25.5);
        temperatureObject.put("status", "正常");
        temperatureObject.put("unit", "°C");

        com.alibaba.fastjson.JSONObject humidityObject = new com.alibaba.fastjson.JSONObject();
        humidityObject.put("value", 65.0);
        humidityObject.put("status", "正常");
        humidityObject.put("unit", "%");

        com.alibaba.fastjson.JSONObject airQualityObject = new com.alibaba.fastjson.JSONObject();
        airQualityObject.put("pm25", 35.0);
        airQualityObject.put("pm10", 50.0);
        airQualityObject.put("co2", 450.0);
        airQualityObject.put("status", "良好");

        com.alibaba.fastjson.JSONObject vibrationObject = new com.alibaba.fastjson.JSONObject();
        vibrationObject.put("value", 0.02);
        vibrationObject.put("status", "正常");
        vibrationObject.put("unit", "mm/s");

        com.alibaba.fastjson.JSONObject noiseObject = new com.alibaba.fastjson.JSONObject();
        noiseObject.put("value", 75.0);
        noiseObject.put("status", "正常");
        noiseObject.put("unit", "dB");

        com.alibaba.fastjson.JSONObject lightObject = new com.alibaba.fastjson.JSONObject();
        lightObject.put("value", 500.0);
        lightObject.put("status", "正常");
        lightObject.put("unit", "lux");

        com.alibaba.fastjson.JSONObject powerObject = new com.alibaba.fastjson.JSONObject();
        powerObject.put("value", 1250.0);
        powerObject.put("unit", "kWh");

        com.alibaba.fastjson.JSONObject summaryObject = new com.alibaba.fastjson.JSONObject();
        summaryObject.put("temperature", temperatureObject);
        summaryObject.put("humidity", humidityObject);
        summaryObject.put("airQuality", airQualityObject);
        summaryObject.put("vibration", vibrationObject);
        summaryObject.put("noise", noiseObject);
        summaryObject.put("light", lightObject);
        summaryObject.put("power", powerObject);

        result.put("summary", summaryObject);
        logger.info("Success to get environment summary...");
        return result.toString();
    }

    // 144. 查询环境告警信息
    @GetMapping("/alerts")
    public String getEnvironmentAlerts() throws InterruptedException {
        logger.info("Execute get environment alerts...");
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        com.alibaba.fastjson.JSONArray infoArray=new com.alibaba.fastjson.JSONArray();
        infoArray.add(createEnvironmentAlertObject(1L, "温度告警", "中", "车间温度超过阈值", "2025-09-23T10:30:00Z", "未处理"));
        infoArray.add(createEnvironmentAlertObject(2L, "噪声告警", "低", "车间噪声接近上限", "2025-09-23T11:15:00Z", "已处理"));
        result.put("alerts",infoArray);
        logger.info("Success to get environment alerts...");
        return result.toString();
    }

    private com.alibaba.fastjson.JSONObject createEnvironmentAlertObject(Long id, String type, String severity, String message, String timestamp, String status) {
        com.alibaba.fastjson.JSONObject alert = new com.alibaba.fastjson.JSONObject();
        alert.put("alertId", id);
        alert.put("alertType", type);
        alert.put("severity", severity);
        alert.put("message", message);
        alert.put("timestamp", timestamp);
        alert.put("status", status);
        return alert;
    }
}
