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
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/travel-plans/activities")
public class TravelPlanActivityController extends BasedController {

    // 11. 获取旅游提醒列表
    @GetMapping("/{planId}/reminders")
    public String getReminders(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get reminders...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray reminders = new JSONArray();
            String[] titles = {"准备护照", "预订酒店", "购买机票", "准备行李", "兑换货币"};
            String[] types = {"准备", "预订", "购买", "准备", "兑换"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject reminder = new JSONObject();
                reminder.put("reminderId", generateRandomNum(8));
                reminder.put("planId", planId);
                reminder.put("reminderTitle", titles[i]);
                reminder.put("reminderTime", "2023-07-0" + (1 + i) + " 09:00");
                reminder.put("reminderType", types[i]);
                reminder.put("description", titles[i] + "的详细说明");
                reminder.put("isCompleted", i % 2 == 0);
                reminders.add(reminder);
            }
            
            result.put("code", 200);
            result.put("data", reminders);
            result.put("planId", planId);
            logger.info("Success to get reminders...");
        }
        return result.toString();
    }

    // 12. 更新旅游提醒
    @PutMapping("/{planId}/reminders/{reminderId}")
    public String updateReminder(@PathVariable Long planId, @PathVariable Long reminderId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update reminder...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || reminderId == null || reminderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或提醒ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("reminderId", reminderId);
            result.put("planId", planId);
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

    // 13. 删除旅游提醒
    @DeleteMapping("/{planId}/reminders/{reminderId}")
    public String deleteReminder(@PathVariable Long planId, @PathVariable Long reminderId) throws InterruptedException {
        logger.info("Execute delete reminder...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || reminderId == null || reminderId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或提醒ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "提醒删除成功");
            result.put("reminderId", reminderId);
            result.put("planId", planId);
            result.put("deletedAt", Instant.now());
            logger.info("Success to delete reminder...");
        }
        return result.toString();
    }

    // 14. 添加旅游笔记
    @PostMapping("/{planId}/notes")
    public String addNote(@PathVariable Integer planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add note...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("noteTitle") || !requestBody.containsKey("noteContent")) {
            result.put("code", 400);
            result.put("message", "缺少笔记标题或笔记内容");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("noteId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("noteTitle", requestBody.getString("noteTitle"));
            result.put("noteContent", requestBody.getString("noteContent"));
            result.put("noteType", requestBody.getString("noteType"));
            result.put("tags", requestBody.get("tags"));
            result.put("createdAt", Instant.now());
            logger.info("Success to add note...");
        }
        return result.toString();
    }

    // 15. 获取旅游笔记列表
    @GetMapping("/{planId}/notes")
    public String getNotes(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get notes...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray notes = new JSONArray();
            String[] titles = {"巴黎印象", "美食推荐", "购物心得", "交通攻略", "住宿体验"};
            String[] types = {"印象", "美食", "购物", "交通", "住宿"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject note = new JSONObject();
                note.put("noteId", generateRandomNum(8));
                note.put("planId", planId);
                note.put("noteTitle", titles[i]);
                note.put("noteContent", titles[i] + "的详细内容描述");
                note.put("noteType", types[i]);
                note.put("tags", new JSONArray().fluentAdd("标签" + (i + 1)));
                note.put("createdAt", Instant.now());
                notes.add(note);
            }
            
            result.put("code", 200);
            result.put("data", notes);
            result.put("planId", planId);
            logger.info("Success to get notes...");
        }
        return result.toString();
    }

    // 16. 更新旅游笔记
    @PutMapping("/{planId}/notes/{noteId}")
    public String updateNote(@PathVariable Long planId, @PathVariable Long noteId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update note...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || noteId == null || noteId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或笔记ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("noteId", noteId);
            result.put("planId", planId);
            result.put("noteTitle", requestBody.getString("noteTitle"));
            result.put("noteContent", requestBody.getString("noteContent"));
            result.put("noteType", requestBody.getString("noteType"));
            result.put("tags", requestBody.get("tags"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update note...");
        }
        return result.toString();
    }

    // 17. 删除旅游笔记
    @DeleteMapping("/{planId}/notes/{noteId}")
    public String deleteNote(@PathVariable Long planId, @PathVariable Long noteId) throws InterruptedException {
        logger.info("Execute delete note...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0 || noteId == null || noteId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或笔记ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("message", "笔记删除成功");
            result.put("noteId", noteId);
            result.put("planId", planId);
            result.put("deletedAt", Instant.now());
            logger.info("Success to delete note...");
        }
        return result.toString();
    }

    // 18. 添加旅游同伴
    @PostMapping("/{planId}/companions")
    public String addCompanion(@PathVariable Long planId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute add companion...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else if (!requestBody.containsKey("companionName") || !requestBody.containsKey("companionPhone")) {
            result.put("code", 400);
            result.put("message", "缺少同伴姓名或电话");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("companionId", generateRandomNum(8));
            result.put("planId", planId);
            result.put("companionName", requestBody.getString("companionName"));
            result.put("companionPhone", requestBody.getString("companionPhone"));
            result.put("companionEmail", requestBody.getString("companionEmail"));
            result.put("relationship", requestBody.getString("relationship"));
            result.put("emergencyContact", requestBody.getString("emergencyContact"));
            result.put("createdAt", Instant.now());
            logger.info("Success to add companion...");
        }
        return result.toString();
    }

    // 19. 获取旅游同伴列表
    @GetMapping("/{planId}/companions")
    public String getCompanions(@PathVariable Long planId) throws InterruptedException {
        logger.info("Execute get companions...");
        JSONObject result = new JSONObject();
        
        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            JSONArray companions = new JSONArray();
            String[] names = {"张三", "李四", "王五", "赵六", "钱七"};
            String[] relationships = {"朋友", "家人", "同事", "朋友", "家人"};
            
            for (int i = 0; i < 5; i++) {
                JSONObject companion = new JSONObject();
                companion.put("companionId", generateRandomNum(8));
                companion.put("planId", planId);
                companion.put("companionName", names[i]);
                companion.put("companionPhone", "138" + generateRandomNum(8));
                companion.put("companionEmail", names[i] + "@example.com");
                companion.put("relationship", relationships[i]);
                companion.put("emergencyContact", "紧急联系人" + (i + 1));
                companion.put("createdAt", Instant.now());
                companions.add(companion);
            }
            
            result.put("code", 200);
            result.put("data", companions);
            result.put("planId", planId);
            logger.info("Success to get companions...");
        }
        return result.toString();
    }

    // 20. 更新旅游同伴
    @PutMapping("/{planId}/companions/{companionId}")
    public String updateCompanion(@PathVariable Long planId, @PathVariable Long companionId, @RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute update companion...");
        JSONObject result = new JSONObject();
        JSONObject requestBody = JSONObject.parseObject(reqBody);
        
        if (planId == null || planId <= 0 || companionId == null || companionId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID或同伴ID");
        } else {
            TimeUnit.SECONDS.sleep(1);
            result.put("code", 200);
            result.put("companionId", companionId);
            result.put("planId", planId);
            result.put("companionName", requestBody.getString("companionName"));
            result.put("companionPhone", requestBody.getString("companionPhone"));
            result.put("companionEmail", requestBody.getString("companionEmail"));
            result.put("relationship", requestBody.getString("relationship"));
            result.put("emergencyContact", requestBody.getString("emergencyContact"));
            result.put("updatedAt", Instant.now());
            logger.info("Success to update companion...");
        }
        return result.toString();
    }
}

