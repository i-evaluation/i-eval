package com.litevar.ieval.llm.tools.invoker.impl;

import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.tools.FunctionInfo;
import com.litevar.ieval.llm.tools.invoker.AbstractFunctionInvoker;
import com.litevar.ieval.llm.utils.RestUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author  action
 * @Date  2025/10/14 17:23 
 * @company litevar
 **/
public class JsonRpcFunctionInvoker extends AbstractFunctionInvoker {
    public JsonRpcFunctionInvoker(FunctionInfo info, String method) {
        super(info, method);
    }

    @Override
    public Object invoke(JSONObject realArgs) {
        //只支持位置参数
        JSONObject protocol = new JSONObject();
        protocol.put("jsonrpc", "2.0");
        protocol.put("id", ThreadLocalRandom.current().nextInt(1,100));
        protocol.put("method", methodName);
		/*
		JSONArray paramArray = new JSONArray();
		paramArray.add(realArgs);
		protocol.put("params", paramArray);
		*/
        protocol.put("params", realArgs);
        return RestUtil.sendPost(info.getServer(), info.getHeaders(), protocol.toJSONString());

    }
}
