package com.ieval.mockTool.controller.impl.travel;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/travel-plans-final")
public class TravelPlanFinalController extends BasedController {

    // ==================== 美食和餐厅功能 (方法101-115) ====================
    
    // 101. 获取餐厅推荐
    @GetMapping("/{planId}/restaurants/recommendations")
    public String getRestaurantRecommendations(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get restaurant recommendations...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray restaurants = new JSONArray();
            String[] names = {"米其林三星餐厅", "传统法式餐厅", "海鲜餐厅", "意大利餐厅", "日式料理"};
            double[] ratings = {4.9, 4.6, 4.5, 4.4, 4.3};
            String[] cuisines = {"法式", "法式", "海鲜", "意式", "日式"};
            double[] prices = {200, 80, 60, 50, 45};
            
            for (int i = 0; i < 5; i++) {
                restaurants.add(new JSONObject()
                    .fluentPut("name", names[i])
                    .fluentPut("cuisine", cuisines[i])
                    .fluentPut("rating", ratings[i])
                    .fluentPut("price", prices[i] + "欧元/人")
                    .fluentPut("address", destination + "餐厅地址" + (i + 1))
                    .fluentPut("recommended", i < 3)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("restaurants", restaurants);
            logger.info("Success to get restaurant recommendations...");
        }
        return result.toString();
    }

    // 102. 获取美食推荐
    @GetMapping("/{planId}/food/recommendations")
    public String getFoodRecommendations(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get food recommendations...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray foods = new JSONArray();
            String[] names = {"法式蜗牛", "鹅肝酱", "马卡龙", "可丽饼", "法式面包"};
            String[] types = {"开胃菜", "主菜", "甜点", "小食", "主食"};
            double[] prices = {25, 35, 8, 12, 5};
            
            for (int i = 0; i < 5; i++) {
                foods.add(new JSONObject()
                    .fluentPut("name", names[i])
                    .fluentPut("type", types[i])
                    .fluentPut("price", prices[i] + "欧元")
                    .fluentPut("description", names[i] + "是" + destination + "的特色美食")
                    .fluentPut("popularity", 90 - i * 5)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("foods", foods);
            logger.info("Success to get food recommendations...");
        }
        return result.toString();
    }

    // 103. 获取餐厅预订
    @PostMapping("/{planId}/restaurants/{restaurantId}/book")
    public String bookRestaurant(@PathVariable Long planId, @PathVariable Long restaurantId,
                               @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book restaurant...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || restaurantId == null || restaurantId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或餐厅ID");
        } else if (!requestBody.containsKey("reservationDate") || !requestBody.containsKey("guests")) {
            result.put("code", 400);
            result.put("message", "缺少预订日期或人数");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("restaurantId", restaurantId);
            result.put("reservationDate", requestBody.getString("reservationDate"));
            result.put("reservationTime", requestBody.getString("reservationTime"));
            result.put("guests", requestBody.getInteger("guests"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book restaurant...");
        }
        return result.toString();
    }

    // 104. 获取餐厅评价
    @GetMapping("/{planId}/restaurants/{restaurantId}/reviews")
    public String getRestaurantReviews(@PathVariable Long planId, @PathVariable Long restaurantId) throws InterruptedException {
        logger.info("Execute get restaurant reviews...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || restaurantId == null || restaurantId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或餐厅ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray reviews = new JSONArray();
            String[] usernames = {"美食家A", "游客B", "本地人C", "评论家D", "食客E"};
            double[] ratings = {5.0, 4.5, 4.0, 4.8, 4.2};
            String[] comments = {"非常美味！", "服务很好", "环境优雅", "值得推荐", "价格合理"};
            
            for (int i = 0; i < 5; i++) {
                reviews.add(new JSONObject()
                    .fluentPut("username", usernames[i])
                    .fluentPut("rating", ratings[i])
                    .fluentPut("comment", comments[i])
                    .fluentPut("date", "2023-06-2" + (5 + i))
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("restaurantId", restaurantId);
            result.put("reviews", reviews);
            result.put("averageRating", 4.5);
            logger.info("Success to get restaurant reviews...");
        }
        return result.toString();
    }

    // 105. 获取美食地图
    @GetMapping("/{planId}/food/map")
    public String getFoodMap(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get food map...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray foodSpots = new JSONArray();
            String[] names = {"美食街A", "市场B", "餐厅C", "咖啡厅D", "甜品店E"};
            double[] latitudes = {48.8566, 48.8576, 48.8586, 48.8596, 48.8606};
            double[] longitudes = {2.3522, 2.3532, 2.3542, 2.3552, 2.3562};
            
            for (int i = 0; i < 5; i++) {
                foodSpots.add(new JSONObject()
                    .fluentPut("name", names[i])
                    .fluentPut("latitude", latitudes[i])
                    .fluentPut("longitude", longitudes[i])
                    .fluentPut("type", i < 2 ? "市场" : i < 4 ? "餐厅" : "甜品")
                    .fluentPut("rating", 4.0 + i * 0.1)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("foodSpots", foodSpots);
            logger.info("Success to get food map...");
        }
        return result.toString();
    }

    // ==================== 社交和分享功能 (方法106-115) ====================
    
    // 106. 分享旅游计划
    @PostMapping("/{planId}/share")
    public String shareTravelPlan(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute share travel plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("shareMethod")) {
            result.put("code", 400);
            result.put("message", "缺少分享方式");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("shareMethod", requestBody.getString("shareMethod"));
            result.put("shareLink", "https://travel.example.com/share/" + generateRandomNum(12));
            result.put("shareCode", generateRandomNum(8));
            result.put("sharedAt", Instant.now());
            logger.info("Success to share travel plan...");
        }
        return result.toString();
    }

    // 107. 获取分享统计
    @GetMapping("/{planId}/share/statistics")
    public String getShareStatistics(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get share statistics...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("statistics", new JSONObject()
                .fluentPut("totalShares", 25)
                .fluentPut("totalViews", 150)
                .fluentPut("totalLikes", 45)
                .fluentPut("totalComments", 12)
                .fluentPut("shareRate", "85%")
            );
            logger.info("Success to get share statistics...");
        }
        return result.toString();
    }

    // 108. 添加评论
    @PostMapping("/{planId}/comments")
    public String addComment(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add comment...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("content")) {
            result.put("code", 400);
            result.put("message", "缺少评论内容");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("commentId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("content", requestBody.getString("content"));
            result.put("author", requestBody.getString("author"));
            result.put("createdAt", Instant.now());
            logger.info("Success to add comment...");
        }
        return result.toString();
    }

    // 109. 获取评论列表
    @GetMapping("/{planId}/comments")
    public String getComments(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get comments...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray comments = new JSONArray();
            String[] authors = {"用户A", "用户B", "用户C", "用户D", "用户E"};
            String[] contents = {"很棒的计划！", "学到了很多", "非常实用", "推荐给大家", "期待下次"};
            
            for (int i = 0; i < 5; i++) {
                comments.add(new JSONObject()
                    .fluentPut("commentId", generateRandomNum(8))
                    .fluentPut("author", authors[i])
                    .fluentPut("content", contents[i])
                    .fluentPut("createdAt", "2023-06-2" + (5 + i))
                    .fluentPut("likes", 10 + i * 2)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("comments", comments);
            logger.info("Success to get comments...");
        }
        return result.toString();
    }

    // 110. 点赞功能
    @PostMapping("/{planId}/like")
    public String likeTravelPlan(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute like travel plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("userId")) {
            result.put("code", 400);
            result.put("message", "缺少用户ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("userId", requestBody.getString("userId"));
            result.put("liked", true);
            result.put("totalLikes", 156);
            result.put("likedAt", Instant.now());
            logger.info("Success to like travel plan...");
        }
        return result.toString();
    }

    // ==================== 分析和统计功能 (方法111-120) ====================
    
    // 111. 获取费用分析
    @GetMapping("/{planId}/analytics/expenses")
    public String getExpenseAnalysis(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get expense analysis...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("expenseAnalysis", new JSONObject()
                .fluentPut("totalBudget", 15000)
                .fluentPut("usedBudget", 8500)
                .fluentPut("remainingBudget", 6500)
                .fluentPut("breakdown", new JSONObject()
                    .fluentPut("accommodation", 3000)
                    .fluentPut("transportation", 2500)
                    .fluentPut("meals", 2000)
                    .fluentPut("activities", 1000)
                )
                .fluentPut("dailyAverage", 850)
                .fluentPut("budgetUtilization", "57%")
            );
            logger.info("Success to get expense analysis...");
        }
        return result.toString();
    }

    // 112. 获取行程统计
    @GetMapping("/{planId}/analytics/itinerary")
    public String getItineraryStatistics(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get itinerary statistics...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("itineraryStats", new JSONObject()
                .fluentPut("totalDays", 10)
                .fluentPut("totalActivities", 25)
                .fluentPut("completedActivities", 18)
                .fluentPut("totalDistance", "150公里")
                .fluentPut("averageDailyActivities", 2.5)
                .fluentPut("completionRate", "72%")
            );
            logger.info("Success to get itinerary statistics...");
        }
        return result.toString();
    }

    // 113. 获取用户行为分析
    @GetMapping("/{planId}/analytics/behavior")
    public String getUserBehaviorAnalysis(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get user behavior analysis...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("behaviorAnalysis", new JSONObject()
                .fluentPut("totalViews", 45)
                .fluentPut("totalShares", 8)
                .fluentPut("totalLikes", 23)
                .fluentPut("totalComments", 12)
                .fluentPut("engagementRate", "68%")
                .fluentPut("popularFeatures", new JSONArray()
                    .fluentAdd("景点推荐")
                    .fluentAdd("路线规划")
                    .fluentAdd("费用计算")
                )
            );
            logger.info("Success to get user behavior analysis...");
        }
        return result.toString();
    }

    // 114. 获取满意度调查
    @GetMapping("/{planId}/analytics/satisfaction")
    public String getSatisfactionSurvey(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get satisfaction survey...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("satisfactionSurvey", new JSONObject()
                .fluentPut("overallRating", 4.6)
                .fluentPut("totalResponses", 25)
                .fluentPut("ratings", new JSONObject()
                    .fluentPut("5星", 15)
                    .fluentPut("4星", 7)
                    .fluentPut("3星", 2)
                    .fluentPut("2星", 1)
                    .fluentPut("1星", 0)
                )
                .fluentPut("satisfactionRate", "88%")
                .fluentPut("recommendationRate", "92%")
            );
            logger.info("Success to get satisfaction survey...");
        }
        return result.toString();
    }

    // 115. 获取综合报告
    @GetMapping("/{planId}/analytics/comprehensive-report")
    public String getComprehensiveReport(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get comprehensive report...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("comprehensiveReport", new JSONObject()
                .fluentPut("summary", "旅游计划执行良好")
                .fluentPut("totalCost", 8500)
                .fluentPut("totalActivities", 18)
                .fluentPut("satisfaction", 4.6)
                .fluentPut("recommendations", new JSONArray()
                    .fluentAdd("增加更多文化活动")
                    .fluentAdd("优化交通安排")
                    .fluentAdd("提前预订热门景点")
                )
                .fluentPut("generatedAt", Instant.now())
            );
            logger.info("Success to get comprehensive report...");
        }
        return result.toString();
    }

    // ==================== 紧急和安全功能 (方法116-125) ====================
    
    // 116. 获取紧急联系方式
    @GetMapping("/{planId}/emergency/contacts")
    public String getEmergencyContacts(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get emergency contacts...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("emergencyContacts", new JSONObject()
                .fluentPut("police", "17")
                .fluentPut("fire", "18")
                .fluentPut("medical", "15")
                .fluentPut("touristHotline", "+33-1-49-52-53-54")
                .fluentPut("embassy", "+33-1-47-23-36-77")
                .fluentPut("insurance", "+33-1-42-86-83-00")
            );
            logger.info("Success to get emergency contacts...");
        }
        return result.toString();
    }

    // 117. 获取安全提醒
    @GetMapping("/{planId}/safety/alerts")
    public String getSafetyAlerts(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get safety alerts...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray alerts = new JSONArray();
            alerts.add(new JSONObject()
                .fluentPut("type", "安全提醒")
                .fluentPut("level", "中等")
                .fluentPut("message", "注意保管好个人物品")
                .fluentPut("area", "旅游景点")
            );
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("alerts", alerts);
            logger.info("Success to get safety alerts...");
        }
        return result.toString();
    }

    // 118. 获取医疗信息
    @GetMapping("/{planId}/medical/info")
    public String getMedicalInfo(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get medical info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("medicalInfo", new JSONObject()
                .fluentPut("hospitals", new JSONArray()
                    .fluentAdd("巴黎医院A")
                    .fluentAdd("巴黎医院B")
                )
                .fluentPut("pharmacies", new JSONArray()
                    .fluentAdd("24小时药房")
                    .fluentAdd("市中心药房")
                )
                .fluentPut("emergencyNumber", "15")
                .fluentPut("insuranceRequired", true)
            );
            logger.info("Success to get medical info...");
        }
        return result.toString();
    }

    // 119. 获取保险信息
    @GetMapping("/{planId}/insurance/info")
    public String getInsuranceInfo(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get insurance info...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("insuranceInfo", new JSONObject()
                .fluentPut("policyNumber", "POL" + generateRandomNum(8))
                .fluentPut("coverage", "全面保障")
                .fluentPut("validUntil", "2023-12-31")
                .fluentPut("emergencyContact", "+33-1-42-86-83-00")
                .fluentPut("coverageAmount", 100000)
            );
            logger.info("Success to get insurance info...");
        }
        return result.toString();
    }

    // 120. 获取安全建议
    @GetMapping("/{planId}/safety/recommendations")
    public String getSafetyRecommendations(@PathVariable Long planId, @RequestParam String destination) throws InterruptedException {
        logger.info("Execute get safety recommendations...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("safetyRecommendations", new JSONArray()
                .fluentAdd("保管好护照和重要证件")
                .fluentAdd("避免携带大量现金")
                .fluentAdd("注意周围环境")
                .fluentAdd("遵守当地法律法规")
                .fluentAdd("保持与家人联系")
            );
            logger.info("Success to get safety recommendations...");
        }
        return result.toString();
    }

    // ==================== AI智能推荐功能 (方法121-125) ====================
    
    // 121. 获取AI推荐行程
    @GetMapping("/{planId}/ai/recommended-itinerary")
    public String getAIRecommendedItinerary(@PathVariable Long planId, @RequestParam String destination,
                                          @RequestParam int days, @RequestParam String interests) throws InterruptedException {
        logger.info("Execute get AI recommended itinerary...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(3);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("days", days);
            result.put("interests", interests);
            result.put("aiItinerary", new JSONObject()
                .fluentPut("title", "AI智能推荐行程")
                .fluentPut("description", "基于您的兴趣和偏好生成的个性化行程")
                .fluentPut("totalCost", 1200)
                .fluentPut("satisfaction", 4.8)
                .fluentPut("generatedAt", Instant.now())
            );
            logger.info("Success to get AI recommended itinerary...");
        }
        return result.toString();
    }

    // 122. 获取个性化推荐
    @GetMapping("/{planId}/ai/personalized-recommendations")
    public String getPersonalizedRecommendations(@PathVariable Long planId, @RequestParam String userId) throws InterruptedException {
        logger.info("Execute get personalized recommendations...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("userId", userId);
            result.put("personalizedRecommendations", new JSONObject()
                .fluentPut("attractions", new JSONArray()
                    .fluentAdd("埃菲尔铁塔")
                    .fluentAdd("卢浮宫")
                )
                .fluentPut("restaurants", new JSONArray()
                    .fluentAdd("米其林餐厅")
                    .fluentAdd("当地特色餐厅")
                )
                .fluentPut("activities", new JSONArray()
                    .fluentAdd("塞纳河游船")
                    .fluentAdd("法式烹饪课")
                )
                .fluentPut("confidence", "85%")
            );
            logger.info("Success to get personalized recommendations...");
        }
        return result.toString();
    }

    // 123. 获取智能预算建议
    @GetMapping("/{planId}/ai/budget-suggestion")
    public String getAIBudgetSuggestion(@PathVariable Long planId, @RequestParam String destination,
                                      @RequestParam int days, @RequestParam String budgetLevel) throws InterruptedException {
        logger.info("Execute get AI budget suggestion...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("days", days);
            result.put("budgetLevel", budgetLevel);
            result.put("aiBudgetSuggestion", new JSONObject()
                .fluentPut("suggestedBudget", 1500)
                .fluentPut("breakdown", new JSONObject()
                    .fluentPut("accommodation", 600)
                    .fluentPut("meals", 400)
                    .fluentPut("activities", 300)
                    .fluentPut("transportation", 200)
                )
                .fluentPut("savings", new JSONArray()
                    .fluentAdd("选择经济型酒店")
                    .fluentAdd("使用公共交通")
                    .fluentAdd("寻找免费活动")
                )
                .fluentPut("confidence", "90%")
            );
            logger.info("Success to get AI budget suggestion...");
        }
        return result.toString();
    }

    // 124. 获取智能路线优化
    @GetMapping("/{planId}/ai/route-optimization")
    public String getAIRouteOptimization(@PathVariable Long planId, @RequestParam String attractions) throws InterruptedException {
        logger.info("Execute get AI route optimization...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(3);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractions", attractions);
            result.put("aiRouteOptimization", new JSONObject()
                .fluentPut("optimizedRoute", new JSONArray()
                    .fluentAdd("埃菲尔铁塔")
                    .fluentAdd("塞纳河游船")
                    .fluentAdd("卢浮宫")
                    .fluentAdd("香榭丽舍大街")
                )
                .fluentPut("totalTime", "8小时")
                .fluentPut("totalDistance", "12公里")
                .fluentPut("efficiency", "95%")
                .fluentPut("savings", "节省2小时")
            );
            logger.info("Success to get AI route optimization...");
        }
        return result.toString();
    }

    // 125. 获取智能旅行助手
    @GetMapping("/{planId}/ai/travel-assistant")
    public String getAITravelAssistant(@PathVariable Long planId, @RequestParam String query) throws InterruptedException {
        logger.info("Execute get AI travel assistant...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (query == null || query.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "查询内容不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("query", query);
            result.put("aiResponse", new JSONObject()
                .fluentPut("answer", "根据您的问题，我建议...")
                .fluentPut("suggestions", new JSONArray()
                    .fluentAdd("建议1")
                    .fluentAdd("建议2")
                    .fluentAdd("建议3")
                )
                .fluentPut("confidence", "88%")
                .fluentPut("relatedTopics", new JSONArray()
                    .fluentAdd("相关话题1")
                    .fluentAdd("相关话题2")
                )
            );
            logger.info("Success to get AI travel assistant...");
        }
        return result.toString();
    }
}
