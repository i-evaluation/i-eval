package com.ieval.mockTool.controller.impl.airconditioner;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/api/air-conditioner")
public class AirConditionerController extends BasedController {

    @PostMapping("/update")
    public String updateAirConditioner(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update air conditioner...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (reqBody == null || reqBody.trim().length() == 0) {
            result.put("code", 401);
            result.put("message", "请求参数不能为空");
        } else if (!requestBody.containsKey("deviceId") ||!requestBody.containsKey("powerStatus")) {
            result.put("code", 401);
            result.put("message", "请求参数异常：缺少必要参数deviceId、userId、mode或powerStatus");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 201);
            
            // 基础必需参数
            result.put("deviceId", requestBody.getString("deviceId"));
            result.put("powerStatus", requestBody.getString("powerStatus"));
            
            
            // 可选参数 - 根据传入参数动态添加
            if (requestBody.containsKey("mode")) {
                result.put("mode", requestBody.getString("mode"));
            } else {
                result.put("mode", "auto");
            }

            if (requestBody.containsKey("temperature")) {
                result.put("temperature", requestBody.getInteger("temperature"));
            } else {
                result.put("temperature", 26);
            }
            
            if (requestBody.containsKey("fanSpeed")) {
                result.put("fanSpeed", requestBody.getString("fanSpeed"));
            } else {
                result.put("fanSpeed", "auto");
            }
            
            if (requestBody.containsKey("swing")) {
                result.put("swing", requestBody.getBoolean("swing"));
            } else {
                result.put("swing", false);
            }
            
            if (requestBody.containsKey("ecoMode")) {
                result.put("ecoMode", requestBody.getBoolean("ecoMode"));
            } else {
                result.put("ecoMode", false);
            }
            
            if (requestBody.containsKey("sleepMode")) {
                result.put("sleepMode", requestBody.getBoolean("sleepMode"));
            } else {
                result.put("sleepMode", false);
            }
            
            if (requestBody.containsKey("timerOn")) {
                result.put("timerOn", requestBody.getInteger("timerOn"));
            } else {
                result.put("timerOn", null);
            }
            
            if (requestBody.containsKey("timerOff")) {
                result.put("timerOff", requestBody.getInteger("timerOff"));
            } else {
                result.put("timerOff", null);
            }
            
            if (requestBody.containsKey("humidity")) {
                result.put("humidity", requestBody.getInteger("humidity"));
            } else {
                result.put("humidity", 50);
            }
            
            if (requestBody.containsKey("airQuality")) {
                result.put("airQuality", requestBody.getString("airQuality"));
            } else {
                result.put("airQuality", "good");
            }
            
            if (requestBody.containsKey("filterStatus")) {
                result.put("filterStatus", requestBody.getString("filterStatus"));
            } else {
                result.put("filterStatus", "normal");
            }
            
            if (requestBody.containsKey("lock")) {
                result.put("lock", requestBody.getBoolean("lock"));
            } else {
                result.put("lock", false);
            }
            
            if (requestBody.containsKey("mute")) {
                result.put("mute", requestBody.getBoolean("mute"));
            } else {
                result.put("mute", false);
            }
            
            if (requestBody.containsKey("display")) {
                result.put("display", requestBody.getBoolean("display"));
            } else {
                result.put("display", true);
            }
            
            if (requestBody.containsKey("healthMode")) {
                result.put("healthMode", requestBody.getBoolean("healthMode"));
            } else {
                result.put("healthMode", false);
            }
            
            if (requestBody.containsKey("windDirection")) {
                result.put("windDirection", requestBody.getString("windDirection"));
            } else {
                result.put("windDirection", "auto");
            }
            
            if (requestBody.containsKey("freshAir")) {
                result.put("freshAir", requestBody.getBoolean("freshAir"));
            } else {
                result.put("freshAir", false);
            }
            
            if (requestBody.containsKey("uvSterilize")) {
                result.put("uvSterilize", requestBody.getBoolean("uvSterilize"));
            } else {
                result.put("uvSterilize", false);
            }
            
            if (requestBody.containsKey("energySave")) {
                result.put("energySave", requestBody.getBoolean("energySave"));
            } else {
                result.put("energySave", false);
            }
            
            if (requestBody.containsKey("autoClean")) {
                result.put("autoClean", requestBody.getBoolean("autoClean"));
            } else {
                result.put("autoClean", false);
            }
            
            if (requestBody.containsKey("smartEye")) {
                result.put("smartEye", requestBody.getBoolean("smartEye"));
            } else {
                result.put("smartEye", false);
            }
            
            if (requestBody.containsKey("turboMode")) {
                result.put("turboMode", requestBody.getBoolean("turboMode"));
            } else {
                result.put("turboMode", false);
            }
            
            if (requestBody.containsKey("quietMode")) {
                result.put("quietMode", requestBody.getBoolean("quietMode"));
            } else {
                result.put("quietMode", false);
            }
            
            if (requestBody.containsKey("temperatureUnit")) {
                result.put("temperatureUnit", requestBody.getString("temperatureUnit"));
            } else {
                result.put("temperatureUnit", "celsius");
            }
            
            if (requestBody.containsKey("breezeMode")) {
                result.put("breezeMode", requestBody.getBoolean("breezeMode"));
            } else {
                result.put("breezeMode", false);
            }
            
            if (requestBody.containsKey("selfDiagnosis")) {
                result.put("selfDiagnosis", requestBody.getBoolean("selfDiagnosis"));
            } else {
                result.put("selfDiagnosis", false);
            }
            
            if (requestBody.containsKey("remoteControl")) {
                result.put("remoteControl", requestBody.getBoolean("remoteControl"));
            } else {
                result.put("remoteControl", true);
            }
            
            if (requestBody.containsKey("voiceControl")) {
                result.put("voiceControl", requestBody.getBoolean("voiceControl"));
            } else {
                result.put("voiceControl", false);
            }
            
            if (requestBody.containsKey("wifiStatus")) {
                result.put("wifiStatus", requestBody.getString("wifiStatus"));
            } else {
                result.put("wifiStatus", "connected");
            }
            
            if (requestBody.containsKey("firmwareVersion")) {
                result.put("firmwareVersion", requestBody.getString("firmwareVersion"));
            } else {
                result.put("firmwareVersion", "1.0.0");
            }
            
            // 时间戳
            result.put("updatedAt", Instant.now());
            
            // 统计信息
            result.put("operationCount", 1);
            result.put("lastMaintenanceDate", null);
            result.put("totalRunningHours", 0);
            
            logger.info("Success to update air conditioner with " + requestBody.size() + " parameters...");
        }
        return result.toString();
    }
    
    @PostMapping("/status")
    public String getAirConditionerStatus(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute get air conditioner status...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (reqBody == null || reqBody.trim().length() == 0) {
            result.put("code", 401);
            result.put("message", "请求参数不能为空");
        } else if (!requestBody.containsKey("deviceId")) {
            result.put("code", 401);
            result.put("message", "请求参数异常：缺少必要参数deviceId");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            
            // 设备基础信息
            result.put("deviceId", requestBody.getString("deviceId"));
            result.put("deviceName", "智能空调");
            result.put("deviceType", "air_conditioner");
            result.put("brand", "格力");
            result.put("model", "KFR-35GW");
            
            // 当前状态
            result.put("powerStatus", "on");
            result.put("mode", "cool");
            result.put("temperature", 26);
            result.put("fanSpeed", "auto");
            result.put("swing", false);
            result.put("ecoMode", false);
            result.put("sleepMode", false);
            
            // 环境信息
            result.put("roomTemperature", 28);
            result.put("humidity", 55);
            result.put("airQuality", "good");
            result.put("filterStatus", "normal");
            
            // 功能状态
            result.put("lock", false);
            result.put("mute", false);
            result.put("display", true);
            result.put("healthMode", false);
            result.put("windDirection", "auto");
            result.put("freshAir", false);
            result.put("uvSterilize", false);
            result.put("energySave", false);
            result.put("autoClean", false);
            result.put("smartEye", false);
            result.put("turboMode", false);
            result.put("quietMode", false);
            result.put("breezeMode", false);
            result.put("selfDiagnosis", false);
            result.put("remoteControl", true);
            result.put("voiceControl", false);
            
            // 网络信息
            result.put("wifiStatus", "connected");
            result.put("signalStrength", 85);
            result.put("firmwareVersion", "1.2.3");
            result.put("lastUpdateTime", "2024-01-15 10:30:00");
            
            // 时间戳
            result.put("timestamp", Instant.now());
            
            // 统计信息
            result.put("totalRunningHours", 1250);
            result.put("operationCount", 156);
            result.put("lastMaintenanceDate", "2024-01-01");
            result.put("nextMaintenanceDate", "2024-04-01");
            
            logger.info("Success to get air conditioner status...");
        }
        return result.toString();
    }
    
    @PostMapping("/set")
    public String setAirConditioner(@RequestBody String reqBody){
        logger.info("Execute set air conditioner user feedback...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
                    
        //String param2k = generateLargeParameter(2048); // 2k大小的参数值
            
        result.put("userFeedBack", requestBody.getString("feedback"));
        //result.put("records", param2k);
        logger.info("Success to set air conditioner user feedback, "+ requestBody.getString("feedback").length());
        return result.toString();
    }

    @PostMapping("/performance")
    public String getAirConditionerPerformance(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute get air conditioner performance...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (reqBody == null || reqBody.trim().length() == 0) {
            result.put("code", 401);
            result.put("message", "请求参数不能为空");
        } else if (!requestBody.containsKey("deviceId")) {
            result.put("code", 401);
            result.put("message", "请求参数异常：缺少必要参数deviceId");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            
            // 基础设备信息
            result.put("deviceId", requestBody.getString("deviceId"));
            result.put("timestamp", Instant.now());
            
            // 获取长度控制参数
            int lengthMode = 1; // 默认1k长度
            if (requestBody.containsKey("lengthMode")) {
                lengthMode = requestBody.getInteger("lengthMode");
            }
            
            // 核心性能参数
            result.put("powerConsumption", 1200 + (int)(Math.random() * 200)); // 功耗 (W)
            result.put("coolingCapacity", 3500 + (int)(Math.random() * 500)); // 制冷量 (W)
            result.put("heatingCapacity", 4000 + (int)(Math.random() * 600)); // 制热量 (W)
            result.put("energyEfficiencyRatio", 3.2 + Math.random() * 0.5); // 能效比
            result.put("noiseLevel", 35 + (int)(Math.random() * 10)); // 噪音水平 (dB)
            result.put("airFlowRate", 400 + (int)(Math.random() * 100)); // 风量 (m³/h)
            result.put("compressorFrequency", 30 + (int)(Math.random() * 40)); // 压缩机频率 (Hz)
            result.put("fanSpeedRPM", 800 + (int)(Math.random() * 400)); // 风扇转速 (RPM)
            result.put("evaporatorTemp", 5 + Math.random() * 3); // 蒸发器温度 (°C)
            result.put("condenserTemp", 45 + Math.random() * 5); // 冷凝器温度 (°C)
            result.put("refrigerantPressure", 2.5 + Math.random() * 0.5); // 制冷剂压力 (MPa)
            result.put("voltage", 220 + (int)(Math.random() * 10)); // 电压 (V)
            result.put("current", 5.5 + Math.random() * 1.0); // 电流 (A)
            result.put("powerFactor", 0.85 + Math.random() * 0.1); // 功率因数
            
            // 如果lengthMode为0，添加基础数据以达到1k长度
            if (lengthMode == 0) {
                // 基础状态参数
                result.put("status", "running");
                result.put("mode", "cool");
                result.put("temperature", 26);
                result.put("fanSpeed", "auto");
                result.put("swing", false);
                result.put("ecoMode", false);
                result.put("sleepMode", false);
                result.put("healthMode", false);
                result.put("freshAir", false);
                result.put("uvSterilize", false);
                result.put("autoClean", false);
                result.put("smartEye", false);
                result.put("turboMode", false);
                result.put("quietMode", false);
                result.put("breezeMode", false);
                result.put("selfDiagnosis", false);
                result.put("remoteControl", true);
                result.put("voiceControl", false);
                result.put("wifiStatus", "connected");
                result.put("signalStrength", 85);
                result.put("firmwareVersion", "1.2.3");
                result.put("lastUpdateTime", "2024-01-15 10:30:00");
                result.put("totalRunningHours", 1250);
                result.put("operationCount", 156);
                result.put("lastMaintenanceDate", "2024-01-01");
                result.put("nextMaintenanceDate", "2024-04-01");
                result.put("avgDailyConsumption", 8.5);
                result.put("monthlyConsumption", 255);
                result.put("efficiencyTrend", "stable");
                result.put("performanceScore", 85);
                result.put("energySavingLevel", "high");
                result.put("comfortLevel", 90);
                result.put("predictedMaintenanceDate", "2024-04-15");
                result.put("componentHealthScore", 88);
                result.put("compressorHealth", 90);
                result.put("fanHealth", 85);
                result.put("sensorHealth", 95);
                result.put("totalEnergySaved", 1250);
                result.put("co2Reduction", 625);
                result.put("costSavings", 750);
                result.put("operationEfficiency", 92);
                result.put("usagePattern", "regular");
                result.put("optimizationSuggestions", "建议在夜间使用节能模式");
                result.put("peakUsageTime", "14:00-16:00");
                result.put("recommendedTemp", 26);
                result.put("energyOptimizationLevel", "medium");
            }
            
            // 如果lengthMode为1，添加更多详细参数以达到2k长度
            if (lengthMode == 1) {
                // 环境参数
                result.put("outdoorTemp", 32 + (int)(Math.random() * 8)); // 室外温度
                result.put("indoorTemp", 26 + (int)(Math.random() * 4)); // 室内温度
                result.put("humidity", 50 + (int)(Math.random() * 20)); // 湿度 (%)
                result.put("airQualityIndex", 80 + (int)(Math.random() * 20)); // 空气质量指数
                result.put("co2Level", 400 + (int)(Math.random() * 100)); // CO2浓度 (ppm)
                result.put("pm25Level", 20 + (int)(Math.random() * 30)); // PM2.5浓度 (μg/m³)
                
                // 运行状态参数
                result.put("runningTime", 1250 + (int)(Math.random() * 100)); // 运行时间 (小时)
                result.put("startupCount", 156 + (int)(Math.random() * 20)); // 启动次数
                result.put("errorCount", (int)(Math.random() * 5)); // 错误次数
                result.put("maintenanceCount", 3 + (int)(Math.random() * 3)); // 维护次数
                result.put("filterLife", 85 + (int)(Math.random() * 15)); // 滤网寿命 (%)
                result.put("uvLampLife", 70 + (int)(Math.random() * 30)); // UV灯寿命 (%)
                
                // 高级功能参数
                result.put("smartEyeStatus", Math.random() > 0.5); // 智能眼状态
                result.put("autoCleanStatus", Math.random() > 0.3); // 自清洁状态
                result.put("freshAirIntake", 50 + (int)(Math.random() * 30)); // 新风量 (%)
                result.put("heatRecoveryEfficiency", 60 + (int)(Math.random() * 20)); // 热回收效率 (%)
                result.put("wifiSignalStrength", 80 + (int)(Math.random() * 20)); // WiFi信号强度 (%)
                result.put("firmwareVersion", "1.2.3"); // 固件版本
                result.put("lastUpdateTime", "2024-01-15 10:30:00"); // 最后更新时间
                
                // 性能分析数据
                result.put("avgDailyConsumption", 8.5 + Math.random() * 2.0); // 日均耗电量 (kWh)
                result.put("monthlyConsumption", 255 + (int)(Math.random() * 60)); // 月耗电量 (kWh)
                result.put("efficiencyTrend", "stable"); // 效率趋势
                result.put("performanceScore", 85 + (int)(Math.random() * 15)); // 性能评分
                result.put("energySavingLevel", "high"); // 节能等级
                result.put("comfortLevel", 90 + (int)(Math.random() * 10)); // 舒适度评分
                
                // 预测性维护数据
                result.put("predictedMaintenanceDate", "2024-04-15"); // 预测维护日期
                result.put("componentHealthScore", 88 + (int)(Math.random() * 12)); // 组件健康评分
                result.put("compressorHealth", 90 + (int)(Math.random() * 10)); // 压缩机健康度
                result.put("fanHealth", 85 + (int)(Math.random() * 15)); // 风扇健康度
                result.put("sensorHealth", 95 + (int)(Math.random() * 5)); // 传感器健康度
                
                // 历史统计数据
                result.put("totalEnergySaved", 1250 + (int)(Math.random() * 500)); // 总节能 (kWh)
                result.put("co2Reduction", 625 + (int)(Math.random() * 250)); // CO2减排 (kg)
                result.put("costSavings", 750 + (int)(Math.random() * 300)); // 成本节约 (元)
                result.put("operationEfficiency", 92 + (int)(Math.random() * 8)); // 运行效率 (%)
                
                // 智能分析结果
                result.put("usagePattern", "regular"); // 使用模式
                result.put("optimizationSuggestions", "建议在夜间使用节能模式"); // 优化建议
                result.put("peakUsageTime", "14:00-16:00"); // 高峰使用时间
                result.put("recommendedTemp", 26); // 推荐温度
                result.put("energyOptimizationLevel", "medium"); // 能耗优化等级
                
                // 添加更多详细数据以确保达到2k长度
                result.put("deviceStatus", "running");
                result.put("operatingMode", "cool");
                result.put("targetTemperature", 26);
                result.put("fanSpeedSetting", "auto");
                result.put("swingFunction", false);
                result.put("ecoModeEnabled", false);
                result.put("sleepModeEnabled", false);
                result.put("healthModeEnabled", false);
                result.put("freshAirFunction", false);
                result.put("uvSterilization", false);
                result.put("autoCleanFunction", false);
                result.put("smartEyeFunction", false);
                result.put("turboModeEnabled", false);
                result.put("quietModeEnabled", false);
                result.put("breezeModeEnabled", false);
                result.put("selfDiagnosisEnabled", false);
                result.put("remoteControlEnabled", true);
                result.put("voiceControlEnabled", false);
                result.put("wifiConnectionStatus", "connected");
                result.put("signalStrength", 85);
                result.put("deviceFirmwareVersion", "1.2.3");
                result.put("lastSystemUpdate", "2024-01-15 10:30:00");
                result.put("totalOperatingHours", 1250);
                result.put("totalOperationCount", 156);
                result.put("lastMaintenanceDate", "2024-01-01");
                result.put("nextMaintenanceDate", "2024-04-01");
                result.put("averageDailyConsumption", 8.5);
                result.put("monthlyEnergyConsumption", 255);
                result.put("efficiencyTrendAnalysis", "stable");
                result.put("overallPerformanceScore", 85);
                result.put("energySavingLevel", "high");
                result.put("userComfortLevel", 90);
                result.put("maintenancePredictionDate", "2024-04-15");
                result.put("overallComponentHealth", 88);
                result.put("compressorHealthStatus", 90);
                result.put("fanMotorHealthStatus", 85);
                result.put("sensorHealthStatus", 95);
                result.put("totalEnergySavings", 1250);
                result.put("totalCO2Reduction", 625);
                result.put("totalCostSavings", 750);
                result.put("overallOperationEfficiency", 92);
                result.put("userUsagePattern", "regular");
                result.put("systemOptimizationSuggestions", "建议在夜间使用节能模式");
                result.put("peakUsageTimeWindow", "14:00-16:00");
                result.put("recommendedTemperatureSetting", 26);
                result.put("energyOptimizationLevel", "medium");
            }
            
            logger.info("Success to get air conditioner performance with lengthMode: " + lengthMode);
        }
        String resultStr=result.toJSONString();
        logger.info("length: "+resultStr.length()+"  , "+resultStr);
        return result.toString();
    }
    
    /**
     * 生成指定大小的参数值
     * @param size 参数值大小（字节数）
     * @return 生成的参数值字符串
     */
    private String generateLargeParameter(int size) {
        StringBuilder sb = new StringBuilder();
        
        // 添加一些有意义的文本内容
        String baseText = "空调性能监控数据：设备运行状态正常，温度控制精确，能耗表现优秀，用户满意度高，系统稳定性良好，维护记录完整，故障率低，响应速度快，智能化程度高，节能效果显著";
        
        // 重复基础文本直到达到指定大小
        while (sb.length() < size) {
            sb.append(baseText);
            if (sb.length() < size) {
                sb.append("|"); // 添加分隔符
            }
        }
        
        // 如果超出大小，截取到指定长度
        if (sb.length() > size) {
            return sb.substring(0, size);
        }
        
        return sb.toString();
    }

}
