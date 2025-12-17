package com.ieval.mockTool.controller.impl.travel;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/travel-plans/basic")
public class TravelPlanBasicController extends BasedController {

    // 1. 创建旅游计划
    @PostMapping
    public String createTravelPlan(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute create travel plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (reqBody == null || reqBody.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "请求参数不能为空");
        } else if (!requestBody.containsKey("destination") || !requestBody.containsKey("startDate")) {
            result.put("code", 400);
            result.put("message", "缺少必要参数: destination或startDate");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("planId", generateRandomNum(8));
            
            // 返回完整的旅游计划参数
            result.put("destination", requestBody.getString("destination") != null ? 
                      requestBody.getString("destination") : "日本, 东京");
            result.put("startDate", requestBody.getString("startDate") != null ? 
                      requestBody.getString("startDate") : "2024-11-20");
            result.put("endDate", requestBody.getString("endDate") != null ? 
                      requestBody.getString("endDate") : "2024-11-27");
            result.put("budget", requestBody.getDouble("budget") != null ? 
                      requestBody.getDouble("budget") : 25000.00);
            result.put("travelers", requestBody.getInteger("travelers") != null ? 
                      requestBody.getInteger("travelers") : 2);
            result.put("tripTheme", requestBody.getString("tripTheme") != null ? 
                      requestBody.getString("tripTheme") : "文化探索与美食之旅");
            
            // 旅行者年龄数组
            JSONArray travelerAges = new JSONArray();
            if (requestBody.containsKey("travelerAges") && requestBody.getJSONArray("travelerAges") != null) {
                travelerAges = requestBody.getJSONArray("travelerAges");
            } else {
                travelerAges.add(28);
                travelerAges.add(30);
            }
            result.put("travelerAges", travelerAges);
            
            result.put("preferredAccommodationType", requestBody.getString("preferredAccommodationType") != null ? 
                      requestBody.getString("preferredAccommodationType") : "酒店");
            result.put("accommodationStarRating", requestBody.getInteger("accommodationStarRating") != null ? 
                      requestBody.getInteger("accommodationStarRating") : 4);
            result.put("preferredTransportation", requestBody.getString("preferredTransportation") != null ? 
                      requestBody.getString("preferredTransportation") : "新干线 + 地铁");
            result.put("includeFlight", requestBody.getBoolean("includeFlight") != null ? 
                      requestBody.getBoolean("includeFlight") : true);
            result.put("departureAirport", requestBody.getString("departureAirport") != null ? 
                      requestBody.getString("departureAirport") : "PEK");
            result.put("flightClass", requestBody.getString("flightClass") != null ? 
                      requestBody.getString("flightClass") : "经济舱");
            result.put("dailyBudgetPerPerson", requestBody.getDouble("dailyBudgetPerPerson") != null ? 
                      requestBody.getDouble("dailyBudgetPerPerson") : 800.00);
            
            // 必游景点数组
            JSONArray mustVisitAttractions = new JSONArray();
            if (requestBody.containsKey("mustVisitAttractions") && requestBody.getJSONArray("mustVisitAttractions") != null) {
                mustVisitAttractions = requestBody.getJSONArray("mustVisitAttractions");
            } else {
                mustVisitAttractions.add("浅草寺");
                mustVisitAttractions.add("东京塔");
                mustVisitAttractions.add("涩谷十字路口");
            }
            result.put("mustVisitAttractions", mustVisitAttractions);
            
            // 偏好活动数组
            JSONArray preferredActivities = new JSONArray();
            if (requestBody.containsKey("preferredActivities") && requestBody.getJSONArray("preferredActivities") != null) {
                preferredActivities = requestBody.getJSONArray("preferredActivities");
            } else {
                preferredActivities.add("参观博物馆");
                preferredActivities.add("品尝当地美食");
                preferredActivities.add("购物");
            }
            result.put("preferredActivities", preferredActivities);
            
            // 食物偏好数组
            JSONArray foodPreferences = new JSONArray();
            if (requestBody.containsKey("foodPreferences") && requestBody.getJSONArray("foodPreferences") != null) {
                foodPreferences = requestBody.getJSONArray("foodPreferences");
            } else {
                foodPreferences.add("日式料理");
                foodPreferences.add("拉面");
                foodPreferences.add("海鲜");
            }
            result.put("foodPreferences", foodPreferences);
            
            result.put("dietaryRestrictions", requestBody.getString("dietaryRestrictions") != null ? 
                      requestBody.getString("dietaryRestrictions") : "无");
            result.put("travelPace", requestBody.getString("travelPace") != null ? 
                      requestBody.getString("travelPace") : "适中");
            result.put("includeGuide", requestBody.getBoolean("includeGuide") != null ? 
                      requestBody.getBoolean("includeGuide") : false);
            result.put("languagePreference", requestBody.getString("languagePreference") != null ? 
                      requestBody.getString("languagePreference") : "中文");
            result.put("specialRequests", requestBody.getString("specialRequests") != null ? 
                      requestBody.getString("specialRequests") : "希望安排一天自由活动");
            result.put("emergencyContactName", requestBody.getString("emergencyContactName") != null ? 
                      requestBody.getString("emergencyContactName") : "张三");
            result.put("emergencyContactPhone", requestBody.getString("emergencyContactPhone") != null ? 
                      requestBody.getString("emergencyContactPhone") : "+86 138 0013 8000");
            result.put("travelInsurance", requestBody.getBoolean("travelInsurance") != null ? 
                      requestBody.getBoolean("travelInsurance") : true);
            result.put("isFlexibleWithDates", requestBody.getBoolean("isFlexibleWithDates") != null ? 
                      requestBody.getBoolean("isFlexibleWithDates") : true);
            result.put("flexibleDateRange", requestBody.getInteger("flexibleDateRange") != null ? 
                      requestBody.getInteger("flexibleDateRange") : 3);
            result.put("currency", requestBody.getString("currency") != null ? 
                      requestBody.getString("currency") : "CNY");
            result.put("userId", requestBody.getString("userId") != null ? 
                      requestBody.getString("userId") : "user12345");
            result.put("notes", requestBody.getString("notes") != null ? 
                      requestBody.getString("notes") : "这是我们第一次去日本，希望行程不要太赶。");
            
            result.put("createdAt", Instant.now());
            logger.info("Success to create travel plan...");
        }
        return result.toString();
    }

    // 2. 获取旅游计划详情
    @GetMapping("/{planId}")
    public String getTravelPlan(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get travel plan...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", "日本, 东京");
            result.put("startDate", "2024-11-20");
            result.put("endDate", "2024-11-27");
            result.put("budget", 25000.00);
            result.put("travelers", 2);
            result.put("status", "confirmed");
            result.put("createdAt", Instant.now());
            logger.info("Success to get travel plan...");
        }
        return result.toString();
    }

    // 3. 更新旅游计划
    @PutMapping("/{planId}")
    public String updateTravelPlan(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update travel plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (reqBody == null || reqBody.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "请求参数不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", requestBody.getString("destination"));
            result.put("startDate", requestBody.getString("startDate"));
            result.put("endDate", requestBody.getString("endDate"));
            result.put("budget", requestBody.getDouble("budget"));
            result.put("travelers", requestBody.getInteger("travelers"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update travel plan...");
        }
        return result.toString();
    }

    // 4. 删除旅游计划
    @DeleteMapping("/{planId}")
    public String deleteTravelPlan(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute delete travel plan...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "旅游计划删除成功");
            result.put("planId", planId);
            result.put("deletedAt", Instant.now());
            logger.info("Success to delete travel plan...");
        }
        return result.toString();
    }

    // 5. 获取旅游计划列表
    @GetMapping
    public String getTravelPlans(@RequestParam(defaultValue = "1") int page, 
                                @RequestParam(defaultValue = "10") int size) throws InterruptedException {
        logger.info("Execute get travel plans list...");
        JSONObject result = new JSONObject();
        
        if (page <= 0 || size <= 0) {
            result.put("code", 400);
            result.put("message", "无效的分页参数");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray plans = new JSONArray();
            
            // 预定义一些数据来增加内容长度
            String[] destinations = {"巴黎", "东京", "纽约", "伦敦", "悉尼", "罗马", "巴塞罗那", "阿姆斯特丹", "维也纳", "布拉格", 
                                   "柏林", "马德里", "里斯本", "哥本哈根", "斯德哥尔摩", "赫尔辛基", "奥斯陆", "雷克雅未克", "都柏林", "爱丁堡"};
            String[] descriptions = {"这是一个充满浪漫气息的城市，拥有丰富的历史文化和美食。", 
                                   "现代化与传统完美融合的都市，拥有独特的文化魅力。",
                                   "世界金融中心，充满活力和机遇的大都市。",
                                   "历史悠久的文化名城，拥有众多世界级博物馆和建筑。",
                                   "阳光明媚的海滨城市，拥有美丽的海滩和自然风光。",
                                   "古罗马帝国的中心，拥有丰富的历史遗迹和艺术珍品。",
                                   "充满艺术气息的城市，高迪的建筑作品令人叹为观止。",
                                   "运河纵横的美丽城市，拥有独特的建筑风格和艺术氛围。",
                                   "音乐之都，拥有丰富的音乐历史和文化遗产。",
                                   "童话般的城市，拥有美丽的建筑和浪漫的氛围。"};
            String[] activities = {"参观博物馆", "品尝当地美食", "购物", "观光", "体验当地文化", "摄影", "徒步旅行", "乘坐游船", "观看表演", "参观历史遗迹"};
            
            for (int i = 0; i < size; i++) {
                JSONObject plan = new JSONObject();
                plan.put("planId", generateRandomNum(8));
                plan.put("destination", destinations[i % destinations.length]);
                plan.put("startDate", "2023-07-" + String.format("%02d", 10 + (i % 20)));
                plan.put("endDate", "2023-07-" + String.format("%02d", 15 + (i % 20)));
                plan.put("budget", 10000 + i * 1000);
                plan.put("travelers", 1 + (i % 3));
                plan.put("status", i % 2 == 0 ? "confirmed" : "pending");
                plan.put("description", descriptions[i % descriptions.length]);
                plan.put("duration", 3 + (i % 7) + "天");
                plan.put("accommodation", "酒店" + (i % 5 + 1) + "星级");
                plan.put("transportation", i % 2 == 0 ? "飞机" : "火车");
                plan.put("weather", "晴天，温度" + (20 + i % 15) + "°C");
                plan.put("currency", i % 3 == 0 ? "欧元" : (i % 3 == 1 ? "美元" : "日元"));
                plan.put("language", i % 2 == 0 ? "英语" : "当地语言");
                plan.put("timezone", "UTC+" + (i % 12));
                plan.put("emergencyContact", "紧急联系电话：" + generateRandomNum(11));
                plan.put("insurance", "已购买旅游保险");
                plan.put("visa", i % 2 == 0 ? "需要签证" : "免签");
                plan.put("vaccination", "建议接种疫苗");
                plan.put("localTransport", "地铁、公交、出租车");
                plan.put("wifi", "酒店和公共场所提供免费WiFi");
                plan.put("powerAdapter", "需要转换插头");
                plan.put("localTime", "当地时间：" + (8 + i % 12) + ":00");
                plan.put("sunsetTime", "日落时间：" + (18 + i % 3) + ":30");
                plan.put("sunriseTime", "日出时间：" + (6 + i % 2) + ":00");
                plan.put("popularSpots", new JSONArray()
                    .fluentAdd(activities[i % activities.length])
                    .fluentAdd(activities[(i + 1) % activities.length])
                    .fluentAdd(activities[(i + 2) % activities.length])
                );
                plan.put("localCuisine", "当地特色美食：" + destinations[i % destinations.length] + "特色菜");
                plan.put("shoppingTips", "购物建议：在" + destinations[i % destinations.length] + "可以购买当地特产");
                plan.put("culturalTips", "文化提示：尊重当地文化传统和习俗");
                plan.put("safetyTips", "安全提示：注意个人财物安全，避免夜间单独外出");
                plan.put("budgetBreakdown", new JSONObject()
                    .fluentPut("accommodation", 3000 + i * 200)
                    .fluentPut("food", 2000 + i * 150)
                    .fluentPut("transportation", 1500 + i * 100)
                    .fluentPut("activities", 2000 + i * 200)
                    .fluentPut("shopping", 1000 + i * 100)
                    .fluentPut("miscellaneous", 500 + i * 50)
                );
                plan.put("packingList", new JSONArray()
                    .fluentAdd("护照和签证")
                    .fluentAdd("身份证")
                    .fluentAdd("现金和信用卡")
                    .fluentAdd("手机和充电器")
                    .fluentAdd("相机")
                    .fluentAdd("衣物")
                    .fluentAdd("洗漱用品")
                    .fluentAdd("常用药品")
                );
                plan.put("checklist", new JSONObject()
                    .fluentPut("booked", true)
                    .fluentPut("packed", i % 2 == 0)
                    .fluentPut("insured", true)
                    .fluentPut("vaccinated", i % 3 == 0)
                    .fluentPut("informed", true)
                );
                plans.add(plan);
            }
            
            result.put("code", 200);
            result.put("data", plans);
            result.put("page", page);
            result.put("size", size);
            result.put("total", 50);
            result.put("message", "成功获取旅游计划列表");
            result.put("timestamp", Instant.now());
            logger.info("Success to get travel plans list...");
        }
        return result.toString();
    }

    // 6. 添加旅游活动
    @PostMapping("/{planId}/activities")
    public String addActivity(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add activity to travel plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("activityName") || !requestBody.containsKey("activityDate")) {
            result.put("code", 400);
            result.put("message", "缺少活动名称或活动日期");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("activityId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("activityName", requestBody.getString("activityName"));
            result.put("activityDate", requestBody.getString("activityDate"));
            result.put("activityTime", requestBody.getString("activityTime"));
            result.put("location", requestBody.getString("location"));
            result.put("description", requestBody.getString("description"));
            result.put("cost", requestBody.getDouble("cost"));
            result.put("status", "scheduled");
            result.put("createdAt", Instant.now());
            logger.info("Success to add activity...");
        }
        return result.toString();
    }

    // 7. 获取旅游活动列表
    @GetMapping("/{planId}/activities")
    public String getActivities(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get activities...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray activities = new JSONArray();
            String[] activityNames = {"参观埃菲尔铁塔", "卢浮宫游览", "塞纳河游船", "蒙马特高地", "香榭丽舍大街"};
            String[] locations = {"埃菲尔铁塔", "卢浮宫", "塞纳河", "蒙马特高地", "香榭丽舍大街"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject activity = new JSONObject();
                activity.put("activityId", generateRandomNum(8));
                activity.put("planId", planId);
                activity.put("activityName", activityNames[i]);
                activity.put("activityDate", "2023-07-0" + (1 + i));
                activity.put("activityTime", "10:00");
                activity.put("location", locations[i]);
                activity.put("description", activityNames[i] + "的详细描述");
                activity.put("cost", 50 + i * 20);
                activity.put("status", "scheduled");
                activities.add(activity);
            }
            
            result.put("code", 200);
            result.put("data", activities);
            result.put("planId", planId);
            logger.info("Success to get activities...");
        }
        return result.toString();
    }

    // 8. 更新旅游活动
    @PutMapping("/{planId}/activities/{activityId}")
    public String updateActivity(@PathVariable Long planId, @PathVariable Long activityId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update activity...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || activityId == null || activityId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或活动ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("activityId", activityId);
            result.put("planId", planId);
            result.put("activityName", requestBody.getString("activityName"));
            result.put("activityDate", requestBody.getString("activityDate"));
            result.put("activityTime", requestBody.getString("activityTime"));
            result.put("location", requestBody.getString("location"));
            result.put("description", requestBody.getString("description"));
            result.put("cost", requestBody.getDouble("cost"));
            result.put("status", requestBody.getString("status"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update activity...");
        }
        return result.toString();
    }

    // 9. 删除旅游活动
    @DeleteMapping("/{planId}/activities/{activityId}")
    public String deleteActivity(@PathVariable Long planId, @PathVariable Long activityId) throws InterruptedException {
        logger.info("Execute delete activity...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || activityId == null || activityId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或活动ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "活动删除成功");
            result.put("activityId", activityId);
            result.put("planId", planId);
            result.put("deletedAt", Instant.now());
            logger.info("Success to delete activity...");
        }
        return result.toString();
    }

    // 10. 添加旅游提醒
    @PostMapping("/{planId}/reminders")
    public String addReminder(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add reminder...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("reminderTitle") || !requestBody.containsKey("reminderTime")) {
            result.put("code", 400);
            result.put("message", "缺少提醒标题或提醒时间");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("reminderId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("reminderTitle", requestBody.getString("reminderTitle"));
            result.put("reminderTime", requestBody.getString("reminderTime"));
            result.put("reminderType", requestBody.getString("reminderType"));
            result.put("description", requestBody.getString("description"));
            result.put("isCompleted", false);
            result.put("createdAt", Instant.now());
            logger.info("Success to add reminder...");
        }
        return result.toString();
    }
}

