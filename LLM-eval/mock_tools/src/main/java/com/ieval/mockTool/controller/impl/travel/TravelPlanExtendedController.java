package com.ieval.mockTool.controller.impl.travel;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/travel-plans-extended")
public class TravelPlanExtendedController extends BasedController {

    // ==================== 交通和导航功能 (方法61-80) ====================
    
    // 61. 获取路线规划
    @GetMapping("/{planId}/transport/route")
    public String getRoutePlan(@PathVariable Long planId, @RequestParam String origin,
                             @RequestParam String destination, @RequestParam(defaultValue = "driving") String mode) throws InterruptedException {
        logger.info("Execute get route plan...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("route", new JSONObject()
                .fluentPut("origin", origin)
                .fluentPut("destination", destination)
                .fluentPut("mode", mode)
                .fluentPut("distance", "15.5公里")
                .fluentPut("duration", "25分钟")
                .fluentPut("steps", new JSONArray()
                    .fluentAdd(new JSONObject()
                        .fluentPut("instruction", "从起点出发，向东行驶")
                        .fluentPut("distance", "2.1公里")
                        .fluentPut("duration", "5分钟")
                    )
                    .fluentAdd(new JSONObject()
                        .fluentPut("instruction", "右转进入主路")
                        .fluentPut("distance", "8.3公里")
                        .fluentPut("duration", "12分钟")
                    )
                    .fluentAdd(new JSONObject()
                        .fluentPut("instruction", "左转到达目的地")
                        .fluentPut("distance", "5.1公里")
                        .fluentPut("duration", "8分钟")
                    )
                )
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get route plan...");
        }
        return result.toString();
    }

    // 62. 获取实时交通信息
    @GetMapping("/{planId}/transport/traffic")
    public String getTrafficInfo(@PathVariable Long planId, @RequestParam String location) throws InterruptedException {
        logger.info("Execute get traffic info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (location == null || location.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "位置不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("location", location);
            result.put("trafficInfo", new JSONObject()
                .fluentPut("status", "轻度拥堵")
                .fluentPut("speed", "35公里/小时")
                .fluentPut("delay", "5分钟")
                .fluentPut("incidents", 0)
                .fluentPut("roadConditions", "良好")
            );
            result.put("updatedAt", Instant.now());
            logger.info("Success to get traffic info...");
        }
        return result.toString();
    }

    // 63. 获取公共交通信息
    @GetMapping("/{planId}/transport/public")
    public String getPublicTransport(@PathVariable Long planId, @RequestParam String origin,
                                   @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get public transport...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray options = new JSONArray();
            
            // 地铁选项
            options.add(new JSONObject()
                .fluentPut("type", "地铁")
                .fluentPut("line", "1号线")
                .fluentPut("duration", "20分钟")
                .fluentPut("cost", "2.5欧元")
                .fluentPut("transfers", 0)
                .fluentPut("frequency", "3-5分钟")
            );
            
            // 公交选项
            options.add(new JSONObject()
                .fluentPut("type", "公交")
                .fluentPut("line", "42路")
                .fluentPut("duration", "35分钟")
                .fluentPut("cost", "1.8欧元")
                .fluentPut("transfers", 1)
                .fluentPut("frequency", "8-12分钟")
            );
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("options", options);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get public transport...");
        }
        return result.toString();
    }

    // 64. 获取停车信息
    @GetMapping("/{planId}/transport/parking")
    public String getParkingInfo(@PathVariable Long planId, @RequestParam String location) throws InterruptedException {
        logger.info("Execute get parking info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (location == null || location.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "位置不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray parkingSpots = new JSONArray();
            
            for (int i = 0; i < 3; i++) {
                parkingSpots.add(new JSONObject()
                    .fluentPut("name", "停车场" + (i + 1))
                    .fluentPut("address", location + "停车场" + (i + 1) + "号")
                    .fluentPut("distance", (i + 1) * 200 + "米")
                    .fluentPut("price", "3.5欧元/小时")
                    .fluentPut("availability", i == 0 ? "充足" : i == 1 ? "一般" : "紧张")
                    .fluentPut("type", i == 0 ? "地下" : i == 1 ? "地面" : "路边")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("location", location);
            result.put("parkingSpots", parkingSpots);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get parking info...");
        }
        return result.toString();
    }

    // 65. 获取出租车信息
    @GetMapping("/{planId}/transport/taxi")
    public String getTaxiInfo(@PathVariable Long planId, @RequestParam String origin,
                            @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get taxi info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("taxiInfo", new JSONObject()
                .fluentPut("estimatedFare", "25-35欧元")
                .fluentPut("duration", "15-20分钟")
                .fluentPut("distance", "8.5公里")
                .fluentPut("baseFare", "2.5欧元")
                .fluentPut("perKmRate", "1.8欧元")
                .fluentPut("waitingTime", "5-10分钟")
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get taxi info...");
        }
        return result.toString();
    }

    // 66. 获取共享单车信息
    @GetMapping("/{planId}/transport/bike-sharing")
    public String getBikeSharingInfo(@PathVariable Long planId, @RequestParam String location) throws InterruptedException {
        logger.info("Execute get bike sharing info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (location == null || location.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "位置不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray bikeStations = new JSONArray();
            
            for (int i = 0; i < 3; i++) {
                bikeStations.add(new JSONObject()
                    .fluentPut("name", "自行车站" + (i + 1))
                    .fluentPut("address", location + "自行车站" + (i + 1))
                    .fluentPut("distance", (i + 1) * 150 + "米")
                    .fluentPut("availableBikes", 8 - i * 2)
                    .fluentPut("availableDocks", 5 + i)
                    .fluentPut("price", "1欧元/30分钟")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("location", location);
            result.put("bikeStations", bikeStations);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get bike sharing info...");
        }
        return result.toString();
    }

    // 67. 获取步行路线
    @GetMapping("/{planId}/transport/walking")
    public String getWalkingRoute(@PathVariable Long planId, @RequestParam String origin,
                                @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get walking route...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("walkingRoute", new JSONObject()
                .fluentPut("distance", "1.2公里")
                .fluentPut("duration", "15分钟")
                .fluentPut("difficulty", "简单")
                .fluentPut("elevation", "平缓")
                .fluentPut("safety", "安全")
                .fluentPut("landmarks", new JSONArray()
                    .fluentAdd("中央公园")
                    .fluentAdd("历史博物馆")
                    .fluentAdd("购物中心")
                )
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get walking route...");
        }
        return result.toString();
    }

    // 68. 获取航班信息
    @GetMapping("/{planId}/transport/flights")
    public String getFlightInfo(@PathVariable Long planId, @RequestParam String origin,
                              @RequestParam String destination, @RequestParam String date) throws InterruptedException {
        logger.info("Execute get flight info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null || date == null) {
            result.put("code", 400);
            result.put("message", "起点、终点和日期不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray flights = new JSONArray();
            String[] airlines = {"中国国际航空", "东方航空", "南方航空", "海南航空"};
            String[] flightNumbers = {"CA1234", "MU5678", "CZ9012", "HU3456"};
            String[] times = {"08:00", "14:30", "19:45", "23:15"};
            double[] prices = {3500, 4200, 3800, 3200};
            
            for (int i = 0; i < 4; i++) {
                flights.add(new JSONObject()
                    .fluentPut("airline", airlines[i])
                    .fluentPut("flightNumber", flightNumbers[i])
                    .fluentPut("departureTime", times[i])
                    .fluentPut("arrivalTime", String.format("%02d:%02d", 
                        Integer.parseInt(times[i].split(":")[0]) + 2,
                        Integer.parseInt(times[i].split(":")[1])))
                    .fluentPut("duration", "2小时30分钟")
                    .fluentPut("price", prices[i])
                    .fluentPut("class", "经济舱")
                    .fluentPut("status", "正常")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("date", date);
            result.put("flights", flights);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get flight info...");
        }
        return result.toString();
    }

    // 69. 获取火车信息
    @GetMapping("/{planId}/transport/trains")
    public String getTrainInfo(@PathVariable Long planId, @RequestParam String origin,
                             @RequestParam String destination, @RequestParam String date) throws InterruptedException {
        logger.info("Execute get train info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null || date == null) {
            result.put("code", 400);
            result.put("message", "起点、终点和日期不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray trains = new JSONArray();
            String[] trainNumbers = {"G1234", "D5678", "K9012", "T3456"};
            String[] times = {"07:30", "12:15", "16:45", "21:20"};
            double[] prices = {180, 120, 85, 95};
            
            for (int i = 0; i < 4; i++) {
                trains.add(new JSONObject()
                    .fluentPut("trainNumber", trainNumbers[i])
                    .fluentPut("departureTime", times[i])
                    .fluentPut("arrivalTime", String.format("%02d:%02d", 
                        Integer.parseInt(times[i].split(":")[0]) + 3,
                        Integer.parseInt(times[i].split(":")[1])))
                    .fluentPut("duration", "3小时15分钟")
                    .fluentPut("price", prices[i])
                    .fluentPut("class", i < 2 ? "一等座" : "二等座")
                    .fluentPut("status", "正常")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("date", date);
            result.put("trains", trains);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get train info...");
        }
        return result.toString();
    }

    // 70. 获取长途汽车信息
    @GetMapping("/{planId}/transport/bus")
    public String getBusInfo(@PathVariable Long planId, @RequestParam String origin,
                           @RequestParam String destination, @RequestParam String date) throws InterruptedException {
        logger.info("Execute get bus info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null || date == null) {
            result.put("code", 400);
            result.put("message", "起点、终点和日期不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray buses = new JSONArray();
            String[] companies = {"长途客运公司A", "长途客运公司B", "长途客运公司C"};
            String[] times = {"09:00", "15:30", "20:00"};
            double[] prices = {65, 58, 72};
            
            for (int i = 0; i < 3; i++) {
                buses.add(new JSONObject()
                    .fluentPut("company", companies[i])
                    .fluentPut("departureTime", times[i])
                    .fluentPut("arrivalTime", String.format("%02d:%02d", 
                        Integer.parseInt(times[i].split(":")[0]) + 4,
                        Integer.parseInt(times[i].split(":")[1])))
                    .fluentPut("duration", "4小时30分钟")
                    .fluentPut("price", prices[i])
                    .fluentPut("type", "豪华大巴")
                    .fluentPut("status", "正常")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("date", date);
            result.put("buses", buses);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get bus info...");
        }
        return result.toString();
    }

    // 71. 获取轮渡信息
    @GetMapping("/{planId}/transport/ferry")
    public String getFerryInfo(@PathVariable Long planId, @RequestParam String origin,
                             @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get ferry info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray ferries = new JSONArray();
            String[] times = {"08:30", "12:00", "16:30", "20:00"};
            double[] prices = {25, 25, 25, 25};
            
            for (int i = 0; i < 4; i++) {
                ferries.add(new JSONObject()
                    .fluentPut("departureTime", times[i])
                    .fluentPut("arrivalTime", String.format("%02d:%02d", 
                        Integer.parseInt(times[i].split(":")[0]) + 1,
                        Integer.parseInt(times[i].split(":")[1])))
                    .fluentPut("duration", "1小时15分钟")
                    .fluentPut("price", prices[i])
                    .fluentPut("type", "客轮")
                    .fluentPut("capacity", "200人")
                    .fluentPut("status", "正常")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("ferries", ferries);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get ferry info...");
        }
        return result.toString();
    }

    // 72. 获取交通费用估算
    @GetMapping("/{planId}/transport/cost-estimate")
    public String getTransportCostEstimate(@PathVariable Long planId, @RequestParam String origin,
                                         @RequestParam String destination, @RequestParam String mode) throws InterruptedException {
        logger.info("Execute get transport cost estimate...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null || mode == null) {
            result.put("code", 400);
            result.put("message", "起点、终点和交通方式不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("mode", mode);
            result.put("costEstimate", new JSONObject()
                .fluentPut("baseCost", getBaseCostByMode(mode))
                .fluentPut("distanceCost", "5.2欧元")
                .fluentPut("timeCost", "2.1欧元")
                .fluentPut("totalCost", getTotalCostByMode(mode))
                .fluentPut("currency", "欧元")
                .fluentPut("validUntil", Instant.now().plus(1, ChronoUnit.HOURS))
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport cost estimate...");
        }
        return result.toString();
    }

    // 73. 获取交通时间估算
    @GetMapping("/{planId}/transport/time-estimate")
    public String getTransportTimeEstimate(@PathVariable Long planId, @RequestParam String origin,
                                         @RequestParam String destination, @RequestParam String mode) throws InterruptedException {
        logger.info("Execute get transport time estimate...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null || mode == null) {
            result.put("code", 400);
            result.put("message", "起点、终点和交通方式不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("mode", mode);
            result.put("timeEstimate", new JSONObject()
                .fluentPut("normalTime", getNormalTimeByMode(mode))
                .fluentPut("trafficDelay", "5分钟")
                .fluentPut("totalTime", getTotalTimeByMode(mode))
                .fluentPut("confidence", "85%")
                .fluentPut("lastUpdated", Instant.now())
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport time estimate...");
        }
        return result.toString();
    }

    // 74. 获取交通拥堵预测
    @GetMapping("/{planId}/transport/congestion-forecast")
    public String getCongestionForecast(@PathVariable Long planId, @RequestParam String route,
                                      @RequestParam String time) throws InterruptedException {
        logger.info("Execute get congestion forecast...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (route == null || time == null) {
            result.put("code", 400);
            result.put("message", "路线和时间不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray forecast = new JSONArray();
            String[] timeSlots = {"08:00-09:00", "12:00-13:00", "17:00-18:00", "20:00-21:00"};
            String[] levels = {"严重拥堵", "轻度拥堵", "严重拥堵", "畅通"};
            int[] delays = {15, 5, 20, 0};
            
            for (int i = 0; i < 4; i++) {
                forecast.add(new JSONObject()
                    .fluentPut("timeSlot", timeSlots[i])
                    .fluentPut("congestionLevel", levels[i])
                    .fluentPut("expectedDelay", delays[i] + "分钟")
                    .fluentPut("speed", delays[i] > 10 ? "15公里/小时" : delays[i] > 5 ? "25公里/小时" : "40公里/小时")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("route", route);
            result.put("time", time);
            result.put("forecast", forecast);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get congestion forecast...");
        }
        return result.toString();
    }

    // 75. 获取交通替代方案
    @GetMapping("/{planId}/transport/alternatives")
    public String getTransportAlternatives(@PathVariable Long planId, @RequestParam String origin,
                                         @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get transport alternatives...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray alternatives = new JSONArray();
            String[] modes = {"地铁", "公交", "出租车", "共享单车", "步行"};
            String[] durations = {"20分钟", "35分钟", "15分钟", "25分钟", "45分钟"};
            double[] costs = {2.5, 1.8, 25, 3, 0};
            String[] descriptions = {"最快最经济", "经济实惠", "最舒适", "环保健康", "免费锻炼"};
            
            for (int i = 0; i < 5; i++) {
                alternatives.add(new JSONObject()
                    .fluentPut("mode", modes[i])
                    .fluentPut("duration", durations[i])
                    .fluentPut("cost", costs[i] + "欧元")
                    .fluentPut("description", descriptions[i])
                    .fluentPut("recommendation", i == 0 ? "推荐" : i == 1 ? "备选" : "可选")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("alternatives", alternatives);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport alternatives...");
        }
        return result.toString();
    }

    // 76. 获取交通实时状态
    @GetMapping("/{planId}/transport/real-time-status")
    public String getRealTimeTransportStatus(@PathVariable Long planId, @RequestParam String route) throws InterruptedException {
        logger.info("Execute get real-time transport status...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (route == null || route.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "路线不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("route", route);
            result.put("realTimeStatus", new JSONObject()
                .fluentPut("currentSpeed", "28公里/小时")
                .fluentPut("trafficLevel", "轻度拥堵")
                .fluentPut("incidents", 0)
                .fluentPut("roadWorks", 1)
                .fluentPut("weatherImpact", "无影响")
                .fluentPut("lastUpdated", Instant.now())
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get real-time transport status...");
        }
        return result.toString();
    }

    // 77. 获取交通历史数据
    @GetMapping("/{planId}/transport/history")
    public String getTransportHistory(@PathVariable Long planId, @RequestParam String route,
                                    @RequestParam String startDate, @RequestParam String endDate) throws InterruptedException {
        logger.info("Execute get transport history...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (route == null || startDate == null || endDate == null) {
            result.put("code", 400);
            result.put("message", "路线、开始日期和结束日期不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray history = new JSONArray();
            String[] dates = {"2023-06-25", "2023-06-26", "2023-06-27", "2023-06-28", "2023-06-29"};
            String[] conditions = {"畅通", "轻度拥堵", "严重拥堵", "畅通", "轻度拥堵"};
            int[] avgSpeeds = {45, 25, 15, 40, 30};
            
            for (int i = 0; i < 5; i++) {
                history.add(new JSONObject()
                    .fluentPut("date", dates[i])
                    .fluentPut("condition", conditions[i])
                    .fluentPut("averageSpeed", avgSpeeds[i] + "公里/小时")
                    .fluentPut("peakHourDelay", avgSpeeds[i] < 20 ? "20分钟" : avgSpeeds[i] < 30 ? "10分钟" : "5分钟")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("route", route);
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            result.put("history", history);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport history...");
        }
        return result.toString();
    }

    // 78. 获取交通优化建议
    @GetMapping("/{planId}/transport/optimization")
    public String getTransportOptimization(@PathVariable Long planId, @RequestParam String origin,
                                         @RequestParam String destination, @RequestParam String preferredTime) throws InterruptedException {
        logger.info("Execute get transport optimization...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null || preferredTime == null) {
            result.put("code", 400);
            result.put("message", "起点、终点和偏好时间不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("preferredTime", preferredTime);
            result.put("optimization", new JSONObject()
                .fluentPut("bestTime", "09:30")
                .fluentPut("bestMode", "地铁")
                .fluentPut("estimatedDuration", "18分钟")
                .fluentPut("estimatedCost", "2.5欧元")
                .fluentPut("savings", "节省12分钟，节省15欧元")
                .fluentPut("recommendations", new JSONArray()
                    .fluentAdd("避开早高峰时段")
                    .fluentAdd("选择地铁出行")
                    .fluentAdd("提前5分钟出发")
                )
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport optimization...");
        }
        return result.toString();
    }

    // 79. 获取交通费用对比
    @GetMapping("/{planId}/transport/cost-comparison")
    public String getTransportCostComparison(@PathVariable Long planId, @RequestParam String origin,
                                           @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get transport cost comparison...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray comparison = new JSONArray();
            String[] modes = {"地铁", "公交", "出租车", "共享单车", "自驾"};
            double[] costs = {2.5, 1.8, 25, 3, 8};
            String[] durations = {"20分钟", "35分钟", "15分钟", "25分钟", "18分钟"};
            String[] pros = {"快速准时", "经济实惠", "舒适便捷", "环保健康", "灵活自由"};
            String[] cons = {"拥挤", "较慢", "昂贵", "体力消耗", "停车难"};
            
            for (int i = 0; i < 5; i++) {
                comparison.add(new JSONObject()
                    .fluentPut("mode", modes[i])
                    .fluentPut("cost", costs[i] + "欧元")
                    .fluentPut("duration", durations[i])
                    .fluentPut("pros", pros[i])
                    .fluentPut("cons", cons[i])
                    .fluentPut("rating", 5 - i)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("comparison", comparison);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport cost comparison...");
        }
        return result.toString();
    }

    // 80. 获取交通综合报告
    @GetMapping("/{planId}/transport/comprehensive-report")
    public String getTransportComprehensiveReport(@PathVariable Long planId, @RequestParam String origin,
                                                @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get transport comprehensive report...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (origin == null || destination == null) {
            result.put("code", 400);
            result.put("message", "起点和终点不能为空");
        } else {
            TimeUnit.SECONDS.sleep(3);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("origin", origin);
            result.put("destination", destination);
            result.put("comprehensiveReport", new JSONObject()
                .fluentPut("summary", new JSONObject()
                    .fluentPut("distance", "8.5公里")
                    .fluentPut("bestMode", "地铁")
                    .fluentPut("bestTime", "09:30")
                    .fluentPut("totalCost", "2.5欧元")
                    .fluentPut("totalDuration", "18分钟")
                )
                .fluentPut("alternatives", 5)
                .fluentPut("trafficConditions", "良好")
                .fluentPut("weatherImpact", "无影响")
                .fluentPut("recommendations", new JSONArray()
                    .fluentAdd("建议使用地铁出行")
                    .fluentAdd("避开早高峰时段")
                    .fluentAdd("预留5分钟缓冲时间")
                )
                .fluentPut("lastUpdated", Instant.now())
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get transport comprehensive report...");
        }
        return result.toString();
    }

    // 辅助方法
    private double getBaseCostByMode(String mode) {
        switch (mode.toLowerCase()) {
            case "taxi": return 2.5;
            case "metro": return 2.0;
            case "bus": return 1.8;
            case "bike": return 1.0;
            case "walking": return 0.0;
            default: return 2.0;
        }
    }

    private double getTotalCostByMode(String mode) {
        switch (mode.toLowerCase()) {
            case "taxi": return 25.0;
            case "metro": return 2.5;
            case "bus": return 1.8;
            case "bike": return 3.0;
            case "walking": return 0.0;
            default: return 2.5;
        }
    }

    private String getNormalTimeByMode(String mode) {
        switch (mode.toLowerCase()) {
            case "taxi": return "15分钟";
            case "metro": return "20分钟";
            case "bus": return "35分钟";
            case "bike": return "25分钟";
            case "walking": return "45分钟";
            default: return "20分钟";
        }
    }

    private String getTotalTimeByMode(String mode) {
        switch (mode.toLowerCase()) {
            case "taxi": return "20分钟";
            case "metro": return "25分钟";
            case "bus": return "40分钟";
            case "bike": return "30分钟";
            case "walking": return "50分钟";
            default: return "25分钟";
        }
    }
}
