package com.virtualightning.library.simple2develop.state;


/**
 * Created by CimZzz on 16/6/26.<br/>
 * Project Name : Virtual-Lightning Simple2Develop<br/>
 * Since : VLSimple2Develop_0.0.1<br/>
 * Description:<br/>
 * 状态管理器（单例）（备忘录）
 */
public final class StateManagement {
    private static StateManagement management;

    /*定义单例方法*/

    private StateManagement()
    {

    }

    public static StateManagement getInstance()
    {
        return management != null ? management : (management = new StateManagement());
    }





    /*状态记录操作方法*/


    /**
     * 创建临时的状态记录，但不存入缓存池内
     * @param key Class对象
     * @return 临时的状态记录
     */
    public StateRecord getTempStateRecord(Class key)
    {
        return new StateRecord();
    }



    /**
     * 使用指定标签发送消息日志，监控状态管理器的内部状态
     * @param tag 标签
     */
    public static void sendInformationLog(String tag)
    {
        //send LogMessage
    }
}
