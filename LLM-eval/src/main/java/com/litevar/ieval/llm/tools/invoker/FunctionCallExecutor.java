package com.litevar.ieval.llm.tools.invoker;

import com.alibaba.fastjson.JSONObject;
import com.litevar.ieval.llm.tools.FunctionInfo;
import com.litevar.ieval.llm.tools.invoker.impl.HttpFunctionInvoker;
import com.litevar.ieval.llm.tools.invoker.impl.JsonRpcFunctionInvoker;

/**
 * @Author  action
 * @Date  2025/10/14 17:19
 * @company litevar
 **/
public class FunctionCallExecutor {
    public Object execute(FunctionInfo info, String methodName, JSONObject args) {
        if("http".equals(info.getKind())) {
            return new HttpFunctionInvoker(info, methodName).invoke(args);
        }
        else if("jsonrpcHttp".equals(info.getKind())) {
            return new JsonRpcFunctionInvoker(info, methodName).invoke(args);
        }
        else {
            return "";
        }
    }
}
