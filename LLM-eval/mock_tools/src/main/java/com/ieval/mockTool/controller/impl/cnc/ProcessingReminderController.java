package com.ieval.mockTool.controller.impl.cnc;

import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/reminders")
public class ProcessingReminderController extends BasedController {

    // 66. 上传提醒信息
    @PostMapping
    public String uploadReminder(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload reminder...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        TimeUnit.SECONDS.sleep(1);
        result.put("code", 200);
        result.put("reminderId", System.currentTimeMillis());
        result.put("machineId", requestBody.getString("machineId"));
        result.put("reminderTitle", requestBody.getString("reminderTitle"));
        result.put("reminderTime", requestBody.getString("reminderTime"));
        result.put("reminderType", requestBody.getString("reminderType"));
        result.put("description", requestBody.getString("description"));
        result.put("isCompleted", false);
        result.put("createdBy", requestBody.getString("createdBy"));
        result.put("createdAt", Instant.now());
        logger.info("Success to upload reminder...");
        return result.toString();
    }

    // 67. 查询提醒信息
    @GetMapping("/{reminderId}")
    public String getReminder(@PathVariable Long reminderId) throws InterruptedException {
        logger.info("Execute get reminder...");
        JSONObject result = new JSONObject();
        
        if (reminderId == null || reminderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的提醒ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("reminderId", reminderId);
            result.put("machineId", "MACHINE-001");
            result.put("reminderTitle", "定期保养提醒");
            result.put("reminderTime", "2025-09-24T09:00:00Z");
            result.put("reminderType", "维护");
            result.put("description", "主轴轴承需要定期润滑和检查");
            result.put("isCompleted", false);
            result.put("createdBy", "系统管理员");
            result.put("createdAt", "2025-09-23T10:00:00Z");
            logger.info("Success to get reminder...");
        }
        return result.toString();
    }

    // 68. 更新提醒信息
    @PostMapping("/{reminderId}/update")
    public String updateReminder(@PathVariable Long reminderId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update reminder...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = com.alibaba.fastjson.JSON.parseObject(reqBody);
        
        if (reminderId == null || reminderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的提醒ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("reminderId", reminderId);
            result.put("reminderTitle", requestBody.getString("reminderTitle"));
            result.put("reminderTime", requestBody.getString("reminderTime"));
            result.put("reminderType", requestBody.getString("reminderType"));
            result.put("description", requestBody.getString("description"));
            result.put("isCompleted", requestBody.getBoolean("isCompleted"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update reminder...");
        }
        return result.toString();
    }

    // 69. 标记提醒已完成
    @PostMapping("/{reminderId}/complete")
    public String completeReminder(@PathVariable Long reminderId) throws InterruptedException {
        logger.info("Execute complete reminder...");
        JSONObject result = new JSONObject();
        
        if (reminderId == null || reminderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的提醒ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("reminderId", reminderId);
            result.put("isCompleted", true);
            result.put("completedAt", Instant.now());
            logger.info("Success to complete reminder...");
        }
        return result.toString();
    }

    // 70. 查询提醒完成状态
    @GetMapping("/{reminderId}/status")
    public String getReminderStatus(@PathVariable Long reminderId) throws InterruptedException {
        logger.info("Execute get reminder status...");
        JSONObject result = new JSONObject();
        
        if (reminderId == null || reminderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的提醒ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("reminderId", reminderId);
            result.put("isCompleted", false);
            result.put("status", "待处理");
            logger.info("Success to get reminder status...");
        }
        return result.toString();
    }
}
