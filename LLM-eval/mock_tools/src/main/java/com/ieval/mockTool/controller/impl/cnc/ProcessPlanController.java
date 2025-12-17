package com.ieval.mockTool.controller.impl.cnc;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/cnc/process/plans")
public class ProcessPlanController extends BasedController {

    // 1. 上传加工计划
    @PostMapping
    public String uploadPlan(@RequestBody String reqBody) throws InterruptedException {
        logger.info("Execute upload plan...");
        JSONObject result = new JSONObject();
        JSONObject body = JSONObject.parseObject(reqBody);

        TimeUnit.MILLISECONDS.sleep(200);
        result.put("code", 200);
        result.put("planId", System.currentTimeMillis());
        result.put("planName", body.getString("planName"));
        result.put("createdAt", Instant.now());
        return result.toString();
    }

    // 2. 查询单个加工计划
    @GetMapping("/{planId}")
    public String getPlan(@PathVariable Long planId) {
        logger.info("Execute get plan...");
        JSONObject result = new JSONObject();

        if (planId == null || planId <= 0) {
            result.put("code", 400);
            result.put("message", "无效的计划ID");
        } else {
            result.put("code", 200);
            result.put("planId", planId);
            result.put("planName", "示例加工计划");
            result.put("status", "进行中");
        }
        return result.toString();
    }

    // 3. 查询所有加工计划
    @GetMapping
    public String getAllPlans() {
        logger.info("Execute get all plans...");
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("plans", new String[]{"计划A", "计划B"});
        return result.toString();
    }

    // 4. 查询计划下所有任务
    @GetMapping("/{planId}/tasks")
    public String getPlanTasks(@PathVariable Long planId) {
        logger.info("Execute get plan tasks...");
        JSONObject result = new JSONObject();

        result.put("code", 200);
        result.put("planId", planId);
        result.put("tasks", new String[]{"任务1", "任务2"});
        return result.toString();
    }
}
