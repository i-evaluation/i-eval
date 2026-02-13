package com.litevar.ieval.llm.tools.invoker.impl;

import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.tools.FunctionInfo;
import com.litevar.ieval.llm.tools.invoker.AbstractFunctionInvoker;
import com.litevar.ieval.llm.utils.RestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author  action
 * @Date  2025/10/14 17:22
 * @company litevar
 **/
public class HttpFunctionInvoker extends AbstractFunctionInvoker {
    private JSONObject realBodyObject;
    public HttpFunctionInvoker(FunctionInfo info, String method) {
        super(info, method);
    }

    /**
     * /MDC/data/1
     * /_AUT/_data/2
     * methodName: MDCdata1_GET, _AUT_data2_POST
     */
    @Override
    public Object invoke(JSONObject realArgs) {
        logger.debug("methodName: {}", methodName);
        if(StringUtils.isBlank(info.getMethod()) || "get".equalsIgnoreCase(info.getMethod())) {
            logger.info("http GET");
            //return RestUtil.sendGet(info.getServer()+transParams(realArgs, info.getRemarks()), info.getHeaders());
            String exUrl = dealQueryAndPath(info.getServer(), realArgs, info.getQueries(), info.getPaths());
            return RestUtil.sendGet(exUrl, info.getHeaders());
        }
        else if("post".equalsIgnoreCase(info.getMethod())) {
            logger.info("http post");
            String exUrl = dealQueryAndPath(info.getServer(), realArgs, info.getQueries(), info.getPaths());
            //return RestUtil.sendPost(info.getServer(), info.getHeaders(), realArgs!=null?realArgs.toJSONString():"");
            return RestUtil.sendPost(exUrl, info.getHeaders(), realBodyObject!=null?realBodyObject.toJSONString():"");
        }
        else if("put".equalsIgnoreCase(info.getMethod())) {
            logger.info("http put");
            String exUrl = dealQueryAndPath(info.getServer(), realArgs, info.getQueries(), info.getPaths());
            //return RestUtil.sendPut(info.getServer(), info.getHeaders(), realArgs!=null?realArgs.toJSONString():"");
            return RestUtil.sendPut(exUrl, info.getHeaders(), realBodyObject!=null?realBodyObject.toJSONString():"");
        }
        else if("delete".equalsIgnoreCase(info.getMethod())) {
            logger.info("http delete");
            //return RestUtil.sendDelete(info.getServer()+transParams(realArgs, info.getRemarks()), info.getHeaders());
            String exUrl = dealQueryAndPath(info.getServer(), realArgs, info.getQueries(), info.getPaths());
            return RestUtil.sendDelete(exUrl, info.getHeaders());
        }
        return "ok";
    }

    //POST,PUT /v1/{pet}?name=xx has body
    private String dealQueryAndPath(String url, JSONObject args, String queries, String paths) {
        if(args!=null && !args.isEmpty()) {
            String targetUrl = url;
            realBodyObject = new JSONObject();
            StringBuffer queryBuffer = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = args.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if(StringUtils.isNoneBlank(queries) && StringUtils.contains(queries, entry.getKey())) {
                    if(!queryBuffer.toString().contains("?")) {
                        queryBuffer.append("?"+entry.getKey()+"="+entry.getValue()+"&");
                    }
                    else {
                        queryBuffer.append(entry.getKey()+"="+entry.getValue()+"&");
                    }
                }
                else if(StringUtils.isNoneBlank(paths) && StringUtils.contains(paths, entry.getKey())) {
                    if(targetUrl.contains("{" + entry.getKey() + "}")) {
                        targetUrl = targetUrl.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
                    }
                }
                else {
                    realBodyObject.put(entry.getKey(), entry.getValue());
                }
            }
            if(!queryBuffer.isEmpty()) {
                String params = queryBuffer.toString();
                params = params.endsWith("&")?StringUtils.substring(params, 0, params.length()-1):params;
                logger.info("params: {}", params);
                targetUrl = targetUrl + params;
            }
            return targetUrl;
        }
        return url;
    }
}
