package com.virtualightning.library.simple2develop.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.4 添加切换相反状态方法<br>
 * Description:<br>
 * 状态记录
 */
@SuppressWarnings("unused")
public final class StateRecord implements Serializable{
    private static final HashMap<String,StateMediator> globalStates = new HashMap<>();
    private HashMap<String,StateMediator> monitorStates;
    private InternalState internalState;
    private final Object locker;

    StateRecord()
    {
        monitorStates = new HashMap<>();
        internalState = new InternalState();
        locker = new Object();
    }

    /*注册状态与内部状态设置*/

    /**
     * 注册全局状态，如果当前状态名已经存在则无法覆盖添加
     * @param stateID 状态ID
     * @param state 状态
     */
    public static void registGlobalState(String stateID,boolean state)
    {
        synchronized (globalStates)
        {
            if(!globalStates.containsKey(stateID))
                globalStates.put(stateID,new StateMediator(state,null));
        }
    }

    /**
     * 设置当前状态为可运行状态，更新全部状态
     */
    public void setRunState()
    {
        internalState.setInternalState(InternalState.INTERNAL_STATE_RUN);
        synchronized (locker){
            for(StateMediator mediator : monitorStates.values())
                mediator.notifyObserver(false);
        }
    }

    /**
     * 设置当前状态为停止状态
     */
    public void setStopState()
    {
        internalState.setInternalState(InternalState.INTERNAL_STATE_STOP);
    }

    /**
     * 判断当前状态是否为可运行状态
     * @return 如果是返回true
     */
    public boolean isRunState()
    {
        return internalState.isRunState();
    }


    /*添加监控状态，如果当前状态名已经存在则无法覆盖添加*/

    /**
     * 新建状态并监控
     * @param stateID 状态ID
     * @param state 状态
     */
    void monitorState(String stateID,Boolean state)
    {
        synchronized (locker)
        {
            if(monitorStates.containsKey(stateID))
                return;

            monitorStates.put(stateID,new StateMediator(state,internalState));
        }
    }

    /**
     * 捕捉全局状态并监控
     * @param stateID 全局状态ID
     */
    void monitorState(String stateID)
    {
        synchronized (locker)
        {
            if(!globalStates.containsKey(stateID) || monitorStates.containsKey(stateID))
                return;

            StateMediator mediator = globalStates.get(stateID);

            monitorStates.put(stateID,mediator.cloneState(internalState));
        }
    }

    /**
     * 继承状态并监控
     * @param record 状态记录
     * @param stateIDs 状态ID数组
     */
    void monitorState(StateRecord record,String... stateIDs)
    {
        synchronized (locker)
        {
            Map<String,StateMediator> parentMonitorMap = record.monitorStates;
            for(String stateID : stateIDs)
                if(parentMonitorMap.containsKey(stateID) && !monitorStates.containsKey(stateID))
                    monitorStates.put(stateID,parentMonitorMap.get(stateID).cloneState(internalState));
        }
    }

    /*注册状态观察者*/

    /**
     * 注册状态观察者，一旦注册之后不能更改
     * @param stateID 状态ID
     * @param observer 状态观察者
     * @param isActivateObserver 是否为活性状态观察者
     */
    private void registObserver(String stateID,Observer observer,boolean isActivateObserver)
    {
        synchronized (locker){
            if(!monitorStates.containsKey(stateID))
                return;

            observer.setActived(isActivateObserver);

            monitorStates.get(stateID).registObserver(observer);
        }
    }

    /**
     * 注册活性状态观察者
     * 活性状态观察者：每次内部状态切换至运行状态时自动通知更新一次
     * @param stateID 状态ID
     * @param observer 状态观察者
     */
    public void registActiviteObserver(String stateID,Observer observer)
    {
        registObserver(stateID,observer,true);
    }

    /**
     * 注册惰性状态观察者
     * 惰性状态观察者：只有状态改变时才会通知更新
     * @param stateID 状态ID
     * @param observer 状态观察者
     */
    public void registInactiveObserver(String stateID,Observer observer)
    {
        registObserver(stateID,observer,false);
    }



    /*状态变更*/


    /**
     * 更改状态为相反状态
     * @since VLSimple2Develop_0.1.4
     * @param stateID 状态ID
     * @param arg 额外的参数
     */
    public void changeStateAgainst(String stateID,Object... arg)
    {
        synchronized (locker)
        {
            if(!monitorStates.containsKey(stateID))
                return;

            monitorStates.get(stateID).changeStateAgainst(arg);
        }
    }

    /**
     * 根据状态ID改变状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param stateID 状态ID
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    public void changeState(String stateID,boolean state,Object... arg)
    {
        synchronized (locker)
        {
            if(!monitorStates.containsKey(stateID))
                return;

            monitorStates.get(stateID).changeState(state,arg);
        }
    }

    /**
     * 根据状态ID改变全局状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param stateID 状态ID
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    public static void changeGlobalState(String stateID,boolean state,Object... arg)
    {
        synchronized (globalStates){
            if(!globalStates.containsKey(stateID))
                return;

            globalStates.get(stateID).changeState(state, arg);
        }
    }

    /*获取状态，如果状态不存在则返回空*/

    /**
     * 根据状态ID获取状态
     * @param stateID 状态ID
     * @return 状态
     */
    public Boolean getState(String stateID)
    {
        synchronized (locker)
        {
            if(!monitorStates.containsKey(stateID))
                return null;

            return monitorStates.get(stateID).getState();
        }
    }




    /**
     * 根据状态ID获取全局状态
     * @param stateID 状态ID
     * @return 状态
     */
    public static Boolean getGlobalState(String stateID)
    {
        synchronized (globalStates)
        {
            if(!globalStates.containsKey(stateID))
                return null;

            return globalStates.get(stateID).getState();
        }
    }

}
