package com.virtualightning.library.simple2develop.quickdb.util;

import android.util.Log;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 日志工具
 */
public final class LogUtils {
    private LogUtils()
    {

    }

    /**
     * 发送 Virtual-Lightning 创建日志
     * @param msg 日志消息
     */
    public static void createLog(Object msg)
    {
        String send = msg == null ? "null" : msg.toString();
        Log.i("QuickDB","QuickDB创建----"+send);
    }

    /**
     * 发送 Virtual-Lightning SQL 执行日志
     * @param msg 日志消息
     */
    public static void sqlLog(Object msg)
    {
        String send = msg == null ? "null" : msg.toString();
        Log.i("QuickDB","执行的SQL："+send);
    }

    /**
     * 发送 Virtual-Lightning 错误日志
     * @param msg 日志消息
     */
    public static void errorLog(Object msg)
    {
        String send = msg == null ? "null" : msg.toString();
        Log.i("QuickDB","发生错误："+send);
    }
}
