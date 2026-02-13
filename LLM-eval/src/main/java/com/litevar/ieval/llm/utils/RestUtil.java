package com.litevar.ieval.llm.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author  action
 * @Date  2025/10/14 16:34
 * @company litevar
 **/
public class RestUtil {
    private static Logger logger = LoggerFactory.getLogger(RestUtil.class);
    //get
    public static HttpResponse getRequest(String url, Map<String, String> headers) {
        if(headers!=null) {
            return HttpRequest.get(url).setReadTimeout(300000).addHeaders(headers).execute();
        }
        return HttpRequest.get(url).setReadTimeout(300000).execute();
    }

    //post
    public static HttpResponse postRequest(String url, Map<String, String> headers, String bodyString) {
        if(headers!=null) {
            //return HttpRequest.post(url).addHeaders(headers).body(bodyString).execute();
            return HttpRequest.post(url).setReadTimeout(300000).addHeaders(headers).body(bodyString).execute();
        }
        return HttpRequest.post(url).setReadTimeout(300000).body(bodyString).execute();
    }
    //put
    public static HttpResponse putRequest(String url, Map<String, String> headers, String bodyString) {
        if(headers!=null) {
            return HttpRequest.put(url).addHeaders(headers).body(bodyString).execute();
        }
        return HttpRequest.put(url).body(bodyString).execute();
    }

    //delete
    public static HttpResponse deleteRequest(String url, Map<String, String> headers) {
        if(headers!=null) {
            return HttpRequest.delete(url).addHeaders(headers).execute();
        }
        return HttpRequest.delete(url).execute();
    }
    //return standard json
    public static JSONObject getWithHeader(String url, Map<String,String> headers) {
        if(!url.contains("key")) {
            logger.info("url: {}", url);
        }
        try {
            HttpResponse response = getRequest(url, headers);
            logger.info("httpStatus: {}", response.getStatus());
            String bodyString = response.body();
            if(bodyString==null) {
                JSONObject obj = new JSONObject();
                logger.error("The return body is empty, please manually check the target interface: {}", url);
                obj.put("code", 404);
                obj.put("info", "api server return null body");
                return obj;
            }
            else {
                logger.debug("response: {}", bodyString);
                return JSONObject.parseObject(bodyString);
            }
        }
        catch (Exception e) {
            logger.error("Error occurred", e);
            JSONObject obj = new JSONObject();
            obj.put("code", 500);
            obj.put("info", "api server throw exception: "+e.getMessage());
            return obj;
        }
    }
    //return standard json
    public static JSONObject postWithHeader(String url, Map<String,String> headers, String sendBody) {
        logger.info("url: {}", url);
        //data:image/jpeg;base64
        if(sendBody!=null&&!StringUtils.contains(sendBody, "llmConfig")&&!StringUtils.contains(sendBody, "data:image")) {
            logger.info("sendBody: {}", sendBody);
        }
        try {
            HttpResponse response = postRequest(url, headers, sendBody);
            logger.info("httpStatus: {}", response.getStatus());
            String bodyString = response.body();
            if(bodyString==null) {
                JSONObject obj = new JSONObject();
                logger.error("The return body is empty, please manually check the target interface: {}", url);
                obj.put("code", 404);
                obj.put("info", "Api return null body");
                return obj;
            }
            else if(StringUtils.startsWith(bodyString, "{") || StringUtils.endsWith(bodyString, "}")) {
                logger.debug("response: {}", bodyString);
                return JSONObject.parseObject(bodyString);
            }
            else {
                logger.info("response: {}", bodyString);
                JSONObject obj = new JSONObject();
                obj.put("code", response.getStatus());
                obj.put("info", bodyString);
                return obj;
            }
        } catch (Exception e) {
            logger.error("Error occurred", e);
            JSONObject obj = new JSONObject();
            obj.put("code", 500);
            obj.put("info", "api server throw exception: "+e.getMessage());
            return obj;
        }
    }
    //return Non standard json
    public static JSONObject sendGet(String url, Map<String,String> headers) {
        logger.info("url: {}", url);
        JSONObject obj = new JSONObject();
        try {
            HttpResponse response = getRequest(url, headers);
            logger.info("httpStatus: {}", response.getStatus());
            String bodyString = response.body();
            if(bodyString==null) {
                logger.error("The return body is empty, please manually check the target interface: {}", url);
                obj.put("code", 404);
                obj.put("info", "api server return null body");
            }
            else {
                obj.put("body", bodyString);
                logger.info("response: {}", bodyString);
            }
        } catch (Exception e) {
            logger.error("Error occurred", e);
            obj.put("code", 500);
            obj.put("info", "api server throw exception: "+e.getMessage());
        }
        return obj;
    }
    //return Non standard json
    public static JSONObject sendPost(String url, Map<String,String> headers, String sendBody) {
        logger.info("url: {}", url);
        logger.info("sendBody: {}", sendBody);
        JSONObject obj = new JSONObject();
        try {
            HttpResponse response = postRequest(url, headers, sendBody);
            logger.info("httpStatus: {}", response.getStatus());
            String bodyString = response.body();
            if(bodyString==null) {
                logger.error("The return body is empty, please manually check the target interface: {}", url);
                obj.put("code", 404);
                obj.put("info", "api server return null body");
            }
            else {
                obj.put("body", bodyString);
                logger.info("response: {}", bodyString);
            }
        } catch (Exception e) {
            logger.error("Error occurred", e);
            obj.put("code", 500);
            obj.put("info", "api server throw exception: "+e.getMessage());
        }
        return obj;
    }
    //return Non standard json
    public static JSONObject sendPut(String url, Map<String,String> headers, String sendBody) {
        logger.info("url: {}", url);
        logger.info("sendBody: {}", sendBody);
        JSONObject obj = new JSONObject();
        try {
            HttpResponse response = putRequest(url, headers, sendBody);
            logger.info("httpStatus: {}", response.getStatus());
            String bodyString = response.body();
            if(bodyString==null) {
                logger.error("The return body is empty, please manually check the target interface: {}", url);
                obj.put("code", 404);
                obj.put("info", "api server return null body");
            }
            else {
                obj.put("body", bodyString);
                logger.debug("response: {}", bodyString);
            }
        } catch (Exception e) {
            logger.error("Error occurred", e);
            obj.put("code", 500);
            obj.put("info", "api server throw exception: "+e.getMessage());
        }
        return obj;
    }
    //return Non standard json
    public static JSONObject sendDelete(String url, Map<String,String> headers) {
        logger.info("url: {}", url);
        JSONObject obj = new JSONObject();
        try {
            HttpResponse response = deleteRequest(url, headers);
            logger.info("httpStatus: {}", response.getStatus());
            String bodyString = response.body();
            if(bodyString==null) {
                logger.error("The return body is empty, please manually check the target interface: {}", url);
                obj.put("code", 404);
                obj.put("info", "api server return null body");
            }
            else {
                obj.put("body", bodyString);
                logger.debug("response: {}", bodyString);
            }
        } catch (Exception e) {
            logger.error("Error occurred", e);
            obj.put("code", 500);
            obj.put("info", "api server throw exception: "+e.getMessage());
        }
        return obj;
    }
}
