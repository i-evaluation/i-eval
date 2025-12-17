package com.litevar.ieval.llm.utils;
/**
 * @Author  action
 * @Date  2025/10/14 17:54 
 * @company litevar
 **/
public class TimeUtil {
    public static void waitFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
