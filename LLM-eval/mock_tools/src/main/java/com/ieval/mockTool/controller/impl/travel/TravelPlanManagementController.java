package com.ieval.mockTool.controller.impl.travel;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/travel-plans/management")
public class TravelPlanManagementController extends BasedController {

    // 21. 删除旅游同伴
    @DeleteMapping("/{planId}/companions/{companionId}")
    public String deleteCompanion(@PathVariable Long planId, @PathVariable Long companionId) throws InterruptedException {
        logger.info("Execute delete companion...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || companionId == null || companionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或同伴ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "同伴删除成功");
            result.put("companionId", companionId);
            result.put("planId", planId);
            result.put("deletedAt", Instant.now());
            logger.info("Success to delete companion...");
        }
        return result.toString();
    }

    // 22. 添加旅游文档
    @PostMapping("/{planId}/documents")
    public String addDocument(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add document...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("documentName") || !requestBody.containsKey("documentType")) {
            result.put("code", 400);
            result.put("message", "缺少文档名称或文档类型");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 201);
            result.put("documentId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("documentName", requestBody.getString("documentName"));
            result.put("documentType", requestBody.getString("documentType"));
            result.put("documentUrl", requestBody.getString("documentUrl"));
            result.put("description", requestBody.getString("description"));
            result.put("fileSize", requestBody.getLong("fileSize"));
            result.put("createdAt", Instant.now());
            logger.info("Success to add document...");
        }
        return result.toString();
    }

    // 23. 获取旅游文档列表
    @GetMapping("/{planId}/documents")
    public String getDocuments(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get documents...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray documents = new JSONArray();
            String[] names = {"护照扫描件", "签证文件", "机票确认单", "酒店预订单", "保险单"};
            String[] types = {"护照", "签证", "机票", "酒店", "保险"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject document = new JSONObject();
                document.put("documentId", generateRandomNum(8));
                document.put("planId", planId);
                document.put("documentName", names[i]);
                document.put("documentType", types[i]);
                document.put("documentUrl", "https://example.com/document" + (i + 1) + ".pdf");
                document.put("description", names[i] + "的详细描述");
                document.put("fileSize", 1024 * (i + 1) * 100);
                document.put("createdAt", Instant.now());
                documents.add(document);
            }
            
            result.put("code", 200);
            result.put("data", documents);
            result.put("planId", planId);
            logger.info("Success to get documents...");
        }
        return result.toString();
    }

    // 24. 更新旅游文档
    @PostMapping("/{planId}/documents/{documentId}")
    public String updateDocument(@PathVariable Long planId, @PathVariable Long documentId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update document...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || documentId == null || documentId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或文档ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("documentId", documentId);
            result.put("planId", planId);
            result.put("documentName", requestBody.getString("documentName"));
            result.put("documentType", requestBody.getString("documentType"));
            result.put("documentUrl", requestBody.getString("documentUrl"));
            result.put("description", requestBody.getString("description"));
            result.put("fileSize", requestBody.getLong("fileSize"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update document...");
        }
        return result.toString();
    }

    // 25. 删除旅游文档
    @DeleteMapping("/{planId}/documents/{documentId}")
    public String deleteDocument(@PathVariable Long planId, @PathVariable Long documentId) throws InterruptedException {
        logger.info("Execute delete document...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || documentId == null || documentId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或文档ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "文档删除成功");
            result.put("documentId", documentId);
            result.put("planId", planId);
            result.put("deletedAt", Instant.now());
            logger.info("Success to delete document...");
        }
        return result.toString();
    }

    // 26. 计算旅游预算
    @GetMapping("/{planId}/budget/calculate")
    public String calculateBudget(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute calculate budget...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("planId", planId);
            result.put("budgetBreakdown", new JSONObject()
                .fluentPut("accommodation", 8000.00)
                .fluentPut("transportation", 5000.00)
                .fluentPut("food", 3000.00)
                .fluentPut("activities", 2000.00)
                .fluentPut("shopping", 1500.00)
                .fluentPut("insurance", 500.00)
                .fluentPut("miscellaneous", 1000.00)
                .fluentPut("total", 20000.00)
            );
            result.put("currency", "CNY");
            result.put("generatedAt", Instant.now());
            logger.info("Success to calculate budget...");
        }
        return result.toString();
    }

    // 27. 生成旅游行程
    @GetMapping("/{planId}/itinerary/generate")
    public String generateItinerary(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute generate itinerary...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(2);
            JSONArray itinerary = new JSONArray();
            String[] activities = {"抵达目的地", "参观主要景点", "品尝当地美食", "购物体验", "返程"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject day = new JSONObject();
                day.put("day", i + 1);
                day.put("date", "2023-07-0" + (1 + i));
                day.put("activities", new JSONArray()
                    .fluentAdd(new JSONObject()
                        .fluentPut("time", "09:00")
                        .fluentPut("activity", activities[i])
                        .fluentPut("location", "地点" + (i + 1))
                        .fluentPut("duration", "2小时")
                    )
                    .fluentAdd(new JSONObject()
                        .fluentPut("time", "14:00")
                        .fluentPut("activity", "自由活动")
                        .fluentPut("location", "市中心")
                        .fluentPut("duration", "3小时")
                    )
                );
                itinerary.add(day);
            }
            
            result.put("code", 200);
            result.put("planId", planId);
            result.put("itinerary", itinerary);
            result.put("generatedAt", Instant.now());
            logger.info("Success to generate itinerary...");
        }
        return result.toString();
    }

    // 28. 分享旅游计划
    @PostMapping("/{planId}/share")
    public String shareTravelPlan(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute share travel plan...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("shareType") || !requestBody.containsKey("recipients")) {
            result.put("code", 400);
            result.put("message", "缺少分享类型或接收者");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("shareId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("shareType", requestBody.getString("shareType"));
            result.put("recipients", requestBody.get("recipients"));
            result.put("shareUrl", "https://example.com/share/" + generateRandomNum(8));
            result.put("expiryTime", Instant.now().plus(7, ChronoUnit.DAYS));
            result.put("isPublic", requestBody.getBoolean("isPublic"));
            result.put("createdAt", Instant.now());
            logger.info("Success to share travel plan...");
        }
        return result.toString();
    }

    // 29. 获取分享链接
    @GetMapping("/{planId}/share/{shareId}")
    public String getShareLink(@PathVariable Long planId, @PathVariable Long shareId) throws InterruptedException {
        logger.info("Execute get share link...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || shareId == null || shareId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或分享ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("shareId", shareId);
            result.put("planId", planId);
            result.put("shareUrl", "https://example.com/share/" + shareId);
            result.put("shareType", "public");
            result.put("isActive", true);
            result.put("viewCount", 15);
            result.put("createdAt", Instant.now());
            result.put("expiryTime", Instant.now().plus(7, ChronoUnit.DAYS));
            logger.info("Success to get share link...");
        }
        return result.toString();
    }

    // 30. 取消分享
    @DeleteMapping("/{planId}/share/{shareId}")
    public String cancelShare(@PathVariable Long planId, @PathVariable Long shareId) throws InterruptedException {
        logger.info("Execute cancel share...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || shareId == null || shareId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或分享ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "分享已取消");
            result.put("shareId", shareId);
            result.put("planId", planId);
            result.put("cancelledAt", Instant.now());
            logger.info("Success to cancel share...");
        }
        return result.toString();
    }
}

