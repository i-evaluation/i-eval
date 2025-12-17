package com.ieval.mockTool.controller.impl.post;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ieval.mockTool.controller.BasedController;

/*
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
*/
@RestController
@RequestMapping("/posts")
//@Tag(name = "创建帖子")
public class SmltPostController extends BasedController {

	@PostMapping("/{userId}/create")
	public String createPost(@PathVariable Long userId, @RequestBody String reqBody) throws InterruptedException {
		logger.info("Execute create post...");
		JSONObject result = new JSONObject();
		JSONObject requestBody = JSONObject.parseObject(reqBody);
		
		if (reqBody == null || reqBody.trim().length() == 0) {
			result.put("code", 401);
			result.put("message", "请求参数不能为空");
		} else if (!requestBody.containsKey("title") || !requestBody.containsKey("content")) {
			result.put("code", 401);
			result.put("message", "请求参数异常：缺少必要参数title或content");
		} else {
			TimeUnit.SECONDS.sleep(2);
			result.put("code", 201);
			
			// 基础必需参数
			result.put("postId", generateRandomNum(10));
			result.put("userId", String.valueOf(userId));
			result.put("title", requestBody.getString("title"));
			result.put("content", requestBody.getString("content"));
			
			// 可选参数 - 根据传入参数动态添加
			if (requestBody.containsKey("category")) {
				result.put("category", requestBody.getString("category"));
			} else {
				result.put("category", "general");
			}
			
			if (requestBody.containsKey("tags")) {
				result.put("tags", requestBody.get("tags"));
			} else {
				JSONArray defaultTags = new JSONArray();
				defaultTags.add("默认标签");
				result.put("tags", defaultTags);
			}
			
			if (requestBody.containsKey("priority")) {
				result.put("priority", requestBody.getString("priority"));
			} else {
				result.put("priority", "normal");
			}
			
			if (requestBody.containsKey("visibility")) {
				result.put("visibility", requestBody.getString("visibility"));
			} else {
				result.put("visibility", "public");
			}
			
			if (requestBody.containsKey("allowComments")) {
				result.put("allowComments", requestBody.getBoolean("allowComments"));
			} else {
				result.put("allowComments", true);
			}
			
			if (requestBody.containsKey("allowLikes")) {
				result.put("allowLikes", requestBody.getBoolean("allowLikes"));
			} else {
				result.put("allowLikes", true);
			}
			
			if (requestBody.containsKey("allowShares")) {
				result.put("allowShares", requestBody.getBoolean("allowShares"));
			} else {
				result.put("allowShares", true);
			}
			
			if (requestBody.containsKey("location")) {
				result.put("location", requestBody.getString("location"));
			} else {
				result.put("location", "未知位置");
			}
			
			if (requestBody.containsKey("mood")) {
				result.put("mood", requestBody.getString("mood"));
			} else {
				result.put("mood", "neutral");
			}
			
			if (requestBody.containsKey("weather")) {
				result.put("weather", requestBody.getString("weather"));
			} else {
				result.put("weather", "晴天");
			}
			
			if (requestBody.containsKey("attachments")) {
				result.put("attachments", requestBody.get("attachments"));
			} else {
				JSONArray defaultAttachments = new JSONArray();
				result.put("attachments", defaultAttachments);
			}
			
			if (requestBody.containsKey("mentions")) {
				result.put("mentions", requestBody.get("mentions"));
			} else {
				JSONArray defaultMentions = new JSONArray();
				result.put("mentions", defaultMentions);
			}
			
			if (requestBody.containsKey("hashtags")) {
				result.put("hashtags", requestBody.get("hashtags"));
			} else {
				JSONArray defaultHashtags = new JSONArray();
				defaultHashtags.add("#生活");
				result.put("hashtags", defaultHashtags);
			}
			
			if (requestBody.containsKey("language")) {
				result.put("language", requestBody.getString("language"));
			} else {
				result.put("language", "zh-CN");
			}
			
			if (requestBody.containsKey("timezone")) {
				result.put("timezone", requestBody.getString("timezone"));
			} else {
				result.put("timezone", "Asia/Shanghai");
			}
			
			if (requestBody.containsKey("device")) {
				result.put("device", requestBody.getString("device"));
			} else {
				result.put("device", "mobile");
			}
			
			if (requestBody.containsKey("appVersion")) {
				result.put("appVersion", requestBody.getString("appVersion"));
			} else {
				result.put("appVersion", "1.0.0");
			}
			
			if (requestBody.containsKey("source")) {
				result.put("source", requestBody.getString("source"));
			} else {
				result.put("source", "web");
			}
			
			if (requestBody.containsKey("isDraft")) {
				result.put("isDraft", requestBody.getBoolean("isDraft"));
			} else {
				result.put("isDraft", false);
			}
			
			if (requestBody.containsKey("scheduledTime")) {
				result.put("scheduledTime", requestBody.getString("scheduledTime"));
			} else {
				result.put("scheduledTime", null);
			}
			
			if (requestBody.containsKey("expiryTime")) {
				result.put("expiryTime", requestBody.getString("expiryTime"));
			} else {
				result.put("expiryTime", null);
			}
			
			if (requestBody.containsKey("parentPostId")) {
				result.put("parentPostId", requestBody.getString("parentPostId"));
			} else {
				result.put("parentPostId", null);
			}
			
			if (requestBody.containsKey("isRepost")) {
				result.put("isRepost", requestBody.getBoolean("isRepost"));
			} else {
				result.put("isRepost", false);
			}
			
			if (requestBody.containsKey("originalPostId")) {
				result.put("originalPostId", requestBody.getString("originalPostId"));
			} else {
				result.put("originalPostId", null);
			}
			
			if (requestBody.containsKey("repostReason")) {
				result.put("repostReason", requestBody.getString("repostReason"));
			} else {
				result.put("repostReason", null);
			}
			
			if (requestBody.containsKey("contentType")) {
				result.put("contentType", requestBody.getString("contentType"));
			} else {
				result.put("contentType", "text");
			}
			
			if (requestBody.containsKey("wordCount")) {
				result.put("wordCount", requestBody.getInteger("wordCount"));
			} else {
				result.put("wordCount", requestBody.getString("content").length());
			}
			
			if (requestBody.containsKey("readingTime")) {
				result.put("readingTime", requestBody.getInteger("readingTime"));
			} else {
				result.put("readingTime", Math.max(1, requestBody.getString("content").length() / 200));
			}
			
			if (requestBody.containsKey("difficulty")) {
				result.put("difficulty", requestBody.getString("difficulty"));
			} else {
				result.put("difficulty", "easy");
			}
			
			if (requestBody.containsKey("targetAudience")) {
				result.put("targetAudience", requestBody.getString("targetAudience"));
			} else {
				result.put("targetAudience", "general");
			}
			
			// 新增参数1: 帖子状态
			if (requestBody.containsKey("status")) {
				result.put("status", requestBody.getString("status"));
			} else {
				result.put("status", "published");
			}
			
			// 新增参数2: 帖子评分
			if (requestBody.containsKey("rating")) {
				result.put("rating", requestBody.getDouble("rating"));
			} else {
				result.put("rating", 0.0);
			}
			
			// 新增参数3: 帖子类型
			if (requestBody.containsKey("postType")) {
				result.put("postType", requestBody.getString("postType"));
			} else {
				result.put("postType", "standard");
			}
			
			// 新增参数4: 是否置顶
			if (requestBody.containsKey("isPinned")) {
				result.put("isPinned", requestBody.getBoolean("isPinned"));
			} else {
				result.put("isPinned", false);
			}
			
			// 新增参数5: 帖子模板
			if (requestBody.containsKey("template")) {
				result.put("template", requestBody.getString("template"));
			} else {
				result.put("template", "default");
			}
			
			// 时间戳
			result.put("createdAt", Instant.now());
			result.put("updatedAt", Instant.now());
			
			// 统计信息
			result.put("viewCount", 0);
			result.put("likeCount", 0);
			result.put("commentCount", 0);
			result.put("shareCount", 0);
			
			logger.info("Success to create post with " + requestBody.size() + " parameters...");
		}
		return result.toString();
	}
	
	@PostMapping("/posts/add")
	public String addPost(@RequestBody String reqBody) throws InterruptedException {
		logger.info("Execute add post...");
		JSONObject result = new JSONObject();
		JSONObject requestBody = JSONObject.parseObject(reqBody);
		if (reqBody == null || reqBody.trim().length() == 0) {
			result.put("code", 401);
			result.put("message", "请求参数不能为空");
		} else if (!requestBody.containsKey("title")||!requestBody.containsKey("content")) {
			result.put("code", 401);
			result.put("message", "请求参数异常");
		}else {
			TimeUnit.SECONDS.sleep(20);
			result.put("code", 201);
			result.put("title", requestBody.getString("title"));
			result.put("content", requestBody.getString("content"));
			result.put("tag", requestBody.getString("tag"));
		}
		return result.toString();
	}
	
	@PostMapping("/posts/modify")
	public String modifyPost(@RequestBody String reqBody) {
		logger.info("Execute modify post...");
		JSONObject result = new JSONObject();
		JSONObject requestBody = JSONObject.parseObject(reqBody);
		if (reqBody == null || reqBody.trim().length() == 0) {
			result.put("code", 401);
			result.put("message", "请求参数不能为空");
		} else {
			result.put("code", 201);
			result.put("postId", requestBody.getString("postId"));
			result.put("title", requestBody.getString("title"));
			result.put("content", requestBody.getString("content"));
			result.put("tags", requestBody.getString("tags"));
			result.put("updatedAt", Instant.now());
		}
		return result.toString();
	}
}
