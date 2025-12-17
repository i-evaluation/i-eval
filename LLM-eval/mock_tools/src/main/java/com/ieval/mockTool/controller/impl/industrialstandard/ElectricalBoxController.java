package com.ieval.mockTool.controller.impl.industrialstandard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

@RestController
@RequestMapping("/api/ebox")
public class ElectricalBoxController extends BasedController {

    private static final String[] FLOW_STEPS = {
        "POWER_OFF", "VERIFY", "HANDLE", "RECHECK", "RESTORE"
    };

    private int currentIndex = 0;
    private final List<Map<String, String>> history = new ArrayList<>();

    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static JSONObject ok(String message, JSONObject data) {
        JSONObject resp = new JSONObject();
        resp.put("code", 0);
        resp.put("message", message);
        if (data != null) resp.put("data", data);
        return resp;
    }

    private static JSONObject err(String message) {
        JSONObject resp = new JSONObject();
        resp.put("code", -1);
        resp.put("message", message);
        return resp;
    }

    private String expectedStep() {
        return FLOW_STEPS[currentIndex];
    }

    private void advance(String step) {
        Map<String, String> rec = new HashMap<>();
        rec.put("step", step);
        rec.put("time", now());
        history.add(rec);
        // 完成 RESTORE 后自动回到起点，便于连续执行
        if ("RESTORE".equals(step)) {
            currentIndex = 0;
            return;
        }
        if (currentIndex < FLOW_STEPS.length - 1) {
            currentIndex += 1;
        }
    }

    private JSONObject status() {
        JSONObject st = new JSONObject();
        st.put("expectedNext", FLOW_STEPS[currentIndex]);
        st.put("history", history);
        boolean finished = !history.isEmpty() && "RESTORE".equals(history.get(history.size() - 1).get("step"));
        st.put("finished", finished);
        return st;
    }

    @PostMapping("/power_off")
    public synchronized JSONObject powerOff() {
        logger.info("/power_off called, expected {}", expectedStep());
        if (!"POWER_OFF".equals(expectedStep())) {
            return err("Invalid step order. Expected '" + expectedStep() + "'");
        }
        advance("POWER_OFF");
        return ok("Power off completed", status());
    }

    @PostMapping("/verify")
    public synchronized JSONObject verify() {
        logger.info("/verify called, expected {}", expectedStep());
        if (!"VERIFY".equals(expectedStep())) {
            return err("Invalid step order. Expected '" + expectedStep() + "'");
        }
        advance("VERIFY");
        return ok("Verification completed (no-voltage confirmed)", status());
    }

    @PostMapping("/handle")
    public synchronized JSONObject handle() {
        logger.info("/handle called, expected {}", expectedStep());
        if (!"HANDLE".equals(expectedStep())) {
            return err("Invalid step order. Expected '" + expectedStep() + "'");
        }
        advance("HANDLE");
        return ok("Handling completed", status());
    }

    @PostMapping("/recheck")
    public synchronized JSONObject recheck() {
        logger.info("/recheck called, expected {}", expectedStep());
        if (!"RECHECK".equals(expectedStep())) {
            return err("Invalid step order. Expected '" + expectedStep() + "'");
        }
        advance("RECHECK");
        return ok("Recheck completed", status());
    }

    @PostMapping("/restore")
    public synchronized JSONObject restore() {
        logger.info("/restore called, expected {}", expectedStep());
        if (!"RESTORE".equals(expectedStep())) {
            return err("Invalid step order. Expected '" + expectedStep() + "'");
        }
        advance("RESTORE");
        return ok("Power restored", status());
    }
}


