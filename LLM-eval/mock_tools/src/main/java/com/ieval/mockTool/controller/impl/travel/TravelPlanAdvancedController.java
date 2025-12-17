package com.ieval.mockTool.controller.impl.travel;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/travel-plans-advanced")
public class TravelPlanAdvancedController extends BasedController {

    // ==================== 景点和活动功能 (方法81-100) ====================
    
    // 81. 获取景点推荐
    @GetMapping("/{planId}/attractions/recommendations")
    public String getAttractionRecommendations(@PathVariable Long planId, @RequestParam String destination,
                                             @RequestParam(defaultValue = "all") String category) throws InterruptedException {
        logger.info("Execute get attraction recommendations...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (destination == null || destination.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "目的地不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray attractions = new JSONArray();
            String[] names = {"埃菲尔铁塔", "卢浮宫", "凡尔赛宫", "塞纳河", "蒙马特高地"};
            String[] categories = {"地标建筑", "博物馆", "历史建筑", "自然景观", "文化区"};
            double[] ratings = {4.8, 4.7, 4.6, 4.5, 4.4};
            int[] visitTimes = {2, 4, 3, 1, 2};
            
            for (int i = 0; i < 5; i++) {
                if ("all".equals(category) || categories[i].contains(category)) {
                    attractions.add(new JSONObject()
                        .fluentPut("name", names[i])
                        .fluentPut("category", categories[i])
                        .fluentPut("rating", ratings[i])
                        .fluentPut("visitTime", visitTimes[i] + "小时")
                        .fluentPut("price", "25欧元")
                        .fluentPut("description", names[i] + "是" + destination + "的著名景点")
                        .fluentPut("recommended", i < 3)
                    );
                }
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("category", category);
            result.put("attractions", attractions);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction recommendations...");
        }
        return result.toString();
    }

    // 82. 获取景点详情
    @GetMapping("/{planId}/attractions/{attractionId}")
    public String getAttractionDetails(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction details...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("details", new JSONObject()
                .fluentPut("name", "埃菲尔铁塔")
                .fluentPut("description", "巴黎的标志性建筑，高324米")
                .fluentPut("address", "战神广场，巴黎")
                .fluentPut("openingHours", "09:30-23:45")
                .fluentPut("price", "25欧元")
                .fluentPut("rating", 4.8)
                .fluentPut("visitTime", "2-3小时")
                .fluentPut("bestTime", "日落时分")
                .fluentPut("tips", new JSONArray()
                    .fluentAdd("提前预订门票")
                    .fluentAdd("避开旅游高峰期")
                    .fluentAdd("带好相机")
                )
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction details...");
        }
        return result.toString();
    }

    // 83. 获取活动推荐
    @GetMapping("/{planId}/activities/recommendations")
    public String getActivityRecommendations(@PathVariable Long planId, @RequestParam String destination,
                                           @RequestParam(defaultValue = "all") String type) throws InterruptedException {
        logger.info("Execute get activity recommendations...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (destination == null || destination.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "目的地不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray activities = new JSONArray();
            String[] names = {"塞纳河游船", "卢浮宫导览", "法式烹饪课", "自行车城市游", "葡萄酒品鉴"};
            String[] types = {"观光", "文化", "体验", "运动", "美食"};
            double[] prices = {15, 35, 80, 25, 45};
            String[] durations = {"1小时", "2小时", "3小时", "2小时", "1.5小时"};
            
            for (int i = 0; i < 5; i++) {
                if ("all".equals(type) || types[i].equals(type)) {
                    activities.add(new JSONObject()
                        .fluentPut("name", names[i])
                        .fluentPut("type", types[i])
                        .fluentPut("price", prices[i] + "欧元")
                        .fluentPut("duration", durations[i])
                        .fluentPut("rating", 4.5 + i * 0.1)
                        .fluentPut("description", names[i] + "是" + destination + "的热门活动")
                        .fluentPut("recommended", i < 3)
                    );
                }
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("destination", destination);
            result.put("type", type);
            result.put("activities", activities);
            result.put("generatedAt", Instant.now());
            logger.info("Success to get activity recommendations...");
        }
        return result.toString();
    }

    // 84. 预订景点门票
    @PostMapping("/{planId}/attractions/{attractionId}/book")
    public String bookAttractionTicket(@PathVariable Long planId, @PathVariable Long attractionId,
                                     @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book attraction ticket...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else if (!requestBody.containsKey("visitDate") || !requestBody.containsKey("quantity")) {
            result.put("code", 400);
            result.put("message", "缺少参观日期或数量");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("visitDate", requestBody.getString("visitDate"));
            result.put("visitTime", requestBody.getString("visitTime"));
            result.put("quantity", requestBody.getInteger("quantity"));
            result.put("ticketType", requestBody.getString("ticketType"));
            result.put("totalPrice", requestBody.getDouble("totalPrice"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book attraction ticket...");
        }
        return result.toString();
    }

    // 85. 获取门票预订状态
    @GetMapping("/{planId}/attractions/bookings/{bookingId}")
    public String getAttractionBookingStatus(@PathVariable Long planId, @PathVariable Long bookingId) throws InterruptedException {
        logger.info("Execute get attraction booking status...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || bookingId == null || bookingId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或预订ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("bookingId", bookingId);
            result.put("status", "confirmed");
            result.put("attractionName", "埃菲尔铁塔");
            result.put("visitDate", "2023-07-15");
            result.put("visitTime", "14:00");
            result.put("quantity", 2);
            result.put("ticketType", "成人票");
            result.put("totalPrice", "50欧元");
            result.put("qrCode", "QR123456789");
            result.put("lastUpdated", Instant.now());
            logger.info("Success to get attraction booking status...");
        }
        return result.toString();
    }

    // 86. 取消景点门票预订
    @DeleteMapping("/{planId}/attractions/bookings/{bookingId}")
    public String cancelAttractionBooking(@PathVariable Long planId, @PathVariable Long bookingId) throws InterruptedException {
        logger.info("Execute cancel attraction booking...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || bookingId == null || bookingId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或预订ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("bookingId", bookingId);
            result.put("status", "cancelled");
            result.put("cancelledAt", Instant.now());
            result.put("refundAmount", "50欧元");
            result.put("refundStatus", "处理中");
            logger.info("Success to cancel attraction booking...");
        }
        return result.toString();
    }

    // 87. 获取景点开放时间
    @GetMapping("/{planId}/attractions/{attractionId}/hours")
    public String getAttractionHours(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction hours...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("hours", new JSONObject()
                .fluentPut("monday", "09:30-23:45")
                .fluentPut("tuesday", "09:30-23:45")
                .fluentPut("wednesday", "09:30-23:45")
                .fluentPut("thursday", "09:30-23:45")
                .fluentPut("friday", "09:30-23:45")
                .fluentPut("saturday", "09:30-23:45")
                .fluentPut("sunday", "09:30-23:45")
                .fluentPut("holidays", "09:30-23:45")
                .fluentPut("lastEntry", "23:00")
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction hours...");
        }
        return result.toString();
    }

    // 88. 获取景点评价
    @GetMapping("/{planId}/attractions/{attractionId}/reviews")
    public String getAttractionReviews(@PathVariable Long planId, @PathVariable Long attractionId,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) throws InterruptedException {
        logger.info("Execute get attraction reviews...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray reviews = new JSONArray();
            String[] usernames = {"张三", "李四", "王五", "赵六", "钱七"};
            double[] ratings = {5.0, 4.5, 4.0, 5.0, 4.5};
            String[] comments = {"非常棒的体验！", "值得一去", "景色很美", "服务很好", "推荐给大家"};
            
            for (int i = 0; i < size && i < 5; i++) {
                reviews.add(new JSONObject()
                    .fluentPut("username", usernames[i])
                    .fluentPut("rating", ratings[i])
                    .fluentPut("comment", comments[i])
                    .fluentPut("date", "2023-06-2" + (5 + i))
                    .fluentPut("helpful", 10 + i * 2)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("reviews", reviews);
            result.put("page", page);
            result.put("size", size);
            result.put("total", 156);
            result.put("averageRating", 4.6);
            logger.info("Success to get attraction reviews...");
        }
        return result.toString();
    }

    // 89. 添加景点评价
    @PostMapping("/{planId}/attractions/{attractionId}/reviews")
    public String addAttractionReview(@PathVariable Long planId, @PathVariable Long attractionId,
                                    @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add attraction review...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else if (!requestBody.containsKey("rating") || !requestBody.containsKey("comment")) {
            result.put("code", 400);
            result.put("message", "缺少评分或评论");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("reviewId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("rating", requestBody.getDouble("rating"));
            result.put("comment", requestBody.getString("comment"));
            result.put("username", requestBody.getString("username"));
            result.put("createdAt", Instant.now());
            logger.info("Success to add attraction review...");
        }
        return result.toString();
    }

    // 90. 获取景点照片
    @GetMapping("/{planId}/attractions/{attractionId}/photos")
    public String getAttractionPhotos(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction photos...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray photos = new JSONArray();
            String[] urls = {
                "https://photos.example.com/attraction1_1.jpg",
                "https://photos.example.com/attraction1_2.jpg",
                "https://photos.example.com/attraction1_3.jpg",
                "https://photos.example.com/attraction1_4.jpg",
                "https://photos.example.com/attraction1_5.jpg"
            };
            String[] captions = {"全景图", "夜景", "内部结构", "游客照片", "历史照片"};
            
            for (int i = 0; i < 5; i++) {
                photos.add(new JSONObject()
                    .fluentPut("url", urls[i])
                    .fluentPut("caption", captions[i])
                    .fluentPut("uploadedBy", "官方")
                    .fluentPut("uploadDate", "2023-06-15")
                    .fluentPut("likes", 100 + i * 20)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("photos", photos);
            result.put("total", 5);
            logger.info("Success to get attraction photos...");
        }
        return result.toString();
    }

    // 91. 获取景点附近设施
    @GetMapping("/{planId}/attractions/{attractionId}/nearby")
    public String getAttractionNearby(@PathVariable Long planId, @PathVariable Long attractionId,
                                    @RequestParam(defaultValue = "all") String type) throws InterruptedException {
        logger.info("Execute get attraction nearby...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray nearby = new JSONArray();
            String[] names = {"附近餐厅", "停车场", "地铁站", "购物中心", "酒店"};
            String[] types = {"餐厅", "停车", "交通", "购物", "住宿"};
            String[] distances = {"200米", "150米", "300米", "500米", "800米"};
            double[] ratings = {4.2, 4.0, 4.5, 4.3, 4.1};
            
            for (int i = 0; i < 5; i++) {
                if ("all".equals(type) || types[i].equals(type)) {
                    nearby.add(new JSONObject()
                        .fluentPut("name", names[i])
                        .fluentPut("type", types[i])
                        .fluentPut("distance", distances[i])
                        .fluentPut("rating", ratings[i])
                        .fluentPut("address", "附近地址" + (i + 1))
                        .fluentPut("phone", "01-2345-678" + i)
                    );
                }
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("type", type);
            result.put("nearby", nearby);
            logger.info("Success to get attraction nearby...");
        }
        return result.toString();
    }

    // 92. 获取景点最佳参观时间
    @GetMapping("/{planId}/attractions/{attractionId}/best-time")
    public String getAttractionBestTime(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction best time...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("bestTime", new JSONObject()
                .fluentPut("season", "春季和秋季")
                .fluentPut("month", "4-6月，9-11月")
                .fluentPut("dayOfWeek", "周二到周四")
                .fluentPut("timeOfDay", "上午9-11点，下午3-5点")
                .fluentPut("reason", "人少景美，光线最佳")
                .fluentPut("avoid", "周末和节假日")
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction best time...");
        }
        return result.toString();
    }

    // 93. 获取景点排队时间
    @GetMapping("/{planId}/attractions/{attractionId}/wait-time")
    public String getAttractionWaitTime(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction wait time...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("waitTime", new JSONObject()
                .fluentPut("current", "25分钟")
                .fluentPut("average", "30分钟")
                .fluentPut("peak", "60分钟")
                .fluentPut("offPeak", "10分钟")
                .fluentPut("lastUpdated", Instant.now())
                .fluentPut("trend", "稳定")
            );
            logger.info("Success to get attraction wait time...");
        }
        return result.toString();
    }

    // 94. 获取景点历史信息
    @GetMapping("/{planId}/attractions/{attractionId}/history")
    public String getAttractionHistory(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction history...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("history", new JSONObject()
                .fluentPut("built", "1889年")
                .fluentPut("architect", "古斯塔夫·埃菲尔")
                .fluentPut("purpose", "1889年世界博览会")
                .fluentPut("height", "324米")
                .fluentPut("materials", "铁")
                .fluentPut("significance", "巴黎标志性建筑")
                .fluentPut("visitors", "每年700万游客")
                .fluentPut("facts", new JSONArray()
                    .fluentAdd("曾是世界上最高的建筑")
                    .fluentAdd("每7年重新粉刷一次")
                    .fluentAdd("有1665级台阶")
                )
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction history...");
        }
        return result.toString();
    }

    // 95. 获取景点导览信息
    @GetMapping("/{planId}/attractions/{attractionId}/tours")
    public String getAttractionTours(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction tours...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray tours = new JSONArray();
            String[] types = {"语音导览", "人工导览", "团体导览", "VIP导览"};
            double[] prices = {5, 15, 25, 50};
            String[] durations = {"1小时", "1.5小时", "2小时", "3小时"};
            String[] languages = {"中文", "英文", "法文", "多语言"};
            
            for (int i = 0; i < 4; i++) {
                tours.add(new JSONObject()
                    .fluentPut("type", types[i])
                    .fluentPut("price", prices[i] + "欧元")
                    .fluentPut("duration", durations[i])
                    .fluentPut("language", languages[i])
                    .fluentPut("groupSize", i == 0 ? "个人" : i == 1 ? "小团" : i == 2 ? "大团" : "私人")
                    .fluentPut("available", true)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("tours", tours);
            logger.info("Success to get attraction tours...");
        }
        return result.toString();
    }

    // 96. 预订景点导览
    @PostMapping("/{planId}/attractions/{attractionId}/tours/book")
    public String bookAttractionTour(@PathVariable Long planId, @PathVariable Long attractionId,
                                   @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute book attraction tour...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else if (!requestBody.containsKey("tourType") || !requestBody.containsKey("tourDate")) {
            result.put("code", 400);
            result.put("message", "缺少导览类型或日期");
        } else {
            TimeUnit.SECONDS.sleep(2);
            result.put("code", 201);
            result.put("bookingId", generateRandomNum(10));
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("tourType", requestBody.getString("tourType"));
            result.put("tourDate", requestBody.getString("tourDate"));
            result.put("tourTime", requestBody.getString("tourTime"));
            result.put("participants", requestBody.getInteger("participants"));
            result.put("language", requestBody.getString("language"));
            result.put("totalPrice", requestBody.getDouble("totalPrice"));
            result.put("status", "confirmed");
            result.put("bookedAt", Instant.now());
            logger.info("Success to book attraction tour...");
        }
        return result.toString();
    }

    // 97. 获取景点活动日历
    @GetMapping("/{planId}/attractions/{attractionId}/events")
    public String getAttractionEvents(@PathVariable Long planId, @PathVariable Long attractionId,
                                    @RequestParam String startDate, @RequestParam String endDate) throws InterruptedException {
        logger.info("Execute get attraction events...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else if (startDate == null || endDate == null) {
            result.put("code", 400);
            result.put("message", "开始日期和结束日期不能为空");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray events = new JSONArray();
            String[] eventNames = {"灯光秀", "音乐会", "艺术展览", "历史讲座", "摄影比赛"};
            String[] dates = {"2023-07-15", "2023-07-20", "2023-07-25", "2023-07-30", "2023-08-05"};
            String[] times = {"20:00", "19:30", "10:00", "14:00", "16:00"};
            String[] types = {"表演", "音乐", "展览", "教育", "比赛"};
            
            for (int i = 0; i < 5; i++) {
                events.add(new JSONObject()
                    .fluentPut("name", eventNames[i])
                    .fluentPut("date", dates[i])
                    .fluentPut("time", times[i])
                    .fluentPut("type", types[i])
                    .fluentPut("duration", "2小时")
                    .fluentPut("price", "免费")
                    .fluentPut("description", eventNames[i] + "活动详情")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("startDate", startDate);
            result.put("endDate", endDate);
            result.put("events", events);
            logger.info("Success to get attraction events...");
        }
        return result.toString();
    }

    // 98. 获取景点访问统计
    @GetMapping("/{planId}/attractions/{attractionId}/statistics")
    public String getAttractionStatistics(@PathVariable Long planId, @PathVariable Long attractionId) throws InterruptedException {
        logger.info("Execute get attraction statistics...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || attractionId == null || attractionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或景点ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionId", attractionId);
            result.put("statistics", new JSONObject()
                .fluentPut("totalVisitors", 7000000)
                .fluentPut("averageRating", 4.6)
                .fluentPut("totalReviews", 125000)
                .fluentPut("peakMonth", "7月")
                .fluentPut("averageVisitTime", "2.5小时")
                .fluentPut("popularTime", "下午2-4点")
                .fluentPut("visitorSatisfaction", "95%")
            );
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction statistics...");
        }
        return result.toString();
    }

    // 99. 获取景点比较
    @GetMapping("/{planId}/attractions/compare")
    public String compareAttractions(@PathVariable Long planId, @RequestParam String attractionIds) throws InterruptedException {
        logger.info("Execute compare attractions...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (attractionIds == null || attractionIds.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "景点ID列表不能为空");
        } else {
            TimeUnit.SECONDS.sleep(2);
            String[] ids = attractionIds.split(",");
            JSONArray comparison = new JSONArray();
            String[] names = {"埃菲尔铁塔", "卢浮宫", "凡尔赛宫"};
            double[] ratings = {4.8, 4.7, 4.6};
            double[] prices = {25, 17, 20};
            String[] visitTimes = {"2小时", "4小时", "3小时"};
            
            for (int i = 0; i < Math.min(ids.length, 3); i++) {
                comparison.add(new JSONObject()
                    .fluentPut("id", ids[i])
                    .fluentPut("name", names[i])
                    .fluentPut("rating", ratings[i])
                    .fluentPut("price", prices[i] + "欧元")
                    .fluentPut("visitTime", visitTimes[i])
                    .fluentPut("category", i == 0 ? "地标" : i == 1 ? "博物馆" : "宫殿")
                    .fluentPut("recommended", i == 0)
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionIds", attractionIds);
            result.put("comparison", comparison);
            result.put("generatedAt", Instant.now());
            logger.info("Success to compare attractions...");
        }
        return result.toString();
    }

    // 100. 获取景点推荐路线
    @GetMapping("/{planId}/attractions/route")
    public String getAttractionRoute(@PathVariable Long planId, @RequestParam String attractionIds,
                                   @RequestParam(defaultValue = "1") int days) throws InterruptedException {
        logger.info("Execute get attraction route...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (attractionIds == null || attractionIds.trim().length() == 0) {
            result.put("code", 400);
            result.put("message", "景点ID列表不能为空");
        } else {
            TimeUnit.SECONDS.sleep(3);
            String[] ids = attractionIds.split(",");
            JSONArray route = new JSONArray();
            String[] names = {"埃菲尔铁塔", "卢浮宫", "凡尔赛宫", "塞纳河", "蒙马特高地"};
            String[] times = {"09:00", "11:00", "14:00", "16:00", "18:00"};
            String[] durations = {"2小时", "3小时", "2小时", "1小时", "1小时"};
            
            for (int i = 0; i < Math.min(ids.length, 5); i++) {
                route.add(new JSONObject()
                    .fluentPut("day", (i / 3) + 1)
                    .fluentPut("order", (i % 3) + 1)
                    .fluentPut("attractionId", ids[i])
                    .fluentPut("name", names[i])
                    .fluentPut("startTime", times[i])
                    .fluentPut("duration", durations[i])
                    .fluentPut("transportTime", i > 0 ? "30分钟" : "0分钟")
                    .fluentPut("transportMode", "地铁")
                );
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("attractionIds", attractionIds);
            result.put("days", days);
            result.put("route", route);
            result.put("totalDuration", "8小时");
            result.put("totalCost", "85欧元");
            result.put("generatedAt", Instant.now());
            logger.info("Success to get attraction route...");
        }
        return result.toString();
    }
}
