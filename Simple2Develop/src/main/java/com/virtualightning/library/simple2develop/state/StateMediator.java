package com.virtualightning.library.simple2develop.state;

import android.os.Message;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Modify : VLSimple2Develop_0.1.4 添加切换相反状态方法<br>
 * Description:<br>
 * 状态中介者
 */
@SuppressWarnings("unused")
public final class StateMediator implements Serializable {
    private State state;
    private InternalState internalState;
    private Observer observer;
    private int sequenceId;

    StateMediator(boolean state,InternalState internalState)
    {
        this.state = new State(state);
        this.internalState = internalState;
        this.sequenceId = -1;

        this.state.addMediator(this);
    }

    private StateMediator(State state,InternalState internalState)
    {
        this.state = state;
        this.internalState = internalState;
        this.sequenceId = -1;

        this.state.addMediator(this);
    }

    /*注册状态观察者*/

    /**
     * 注册状态观察者，一旦注册之后不能更改
     * @param observer 状态观察者
     */
    void registObserver(Observer observer)
    {
        if(this.observer == null)
            this.observer = observer;
    }


    /*状态的复制与继承*/

    /**
     * 克隆状态中介，多个克隆体之间共享同一个状态对象，实现状态的一致性。
     * @param internalState 内部状态
     * @return 状态中介
     */
    StateMediator cloneState(InternalState internalState)
    {
        return new StateMediator(state,internalState);
    }


    /*状态变更*/



    /**
     * 更改状态为相反状态
     * @since VLSimple2Develop_0.1.4
     * @param arg 额外的参数
     */
    void changeStateAgainst(Object... arg)
    {
        this.state.changeStateAgainst(arg);
    }

    /**
     * 改变状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    void changeState(boolean state,Object... arg)
    {
        this.state.changeState(state,arg);
    }

    /*获取状态*/

    /**
     * 获取状态
     * @return 返回状态
     */
    boolean getState()
    {
        return state.getState();
    }

    /*执行更新*/

    /**
     * 发送通知更新消息
     * @param arg 额外的参数
     */
    synchronized void notifyObserver(boolean isStateCall,Object... arg)
    {
        if((observer == null) || (!isStateCall && !observer.isActivedObserver()))
            return;

        /*生成下一个序列号*/
        nextSequenceId();

        Message msg = MainLoopCall.getInstance().obtainMessage();
        msg.what = MainLoopCall.MSG_STATE_UPDATE;
        msg.arg1 = sequenceId;
        msg.obj = new Object[]{this,arg};
        msg.sendToTarget();
    }

    /**
     * 更新观察者
     * @param arg 额外的参数
     */
    synchronized void updateObserver(@Nullable Object... arg)
    {
        synchronized (this)
        {
            /*序列号初始化*/
            sequenceId = -1;
        }

        /*处理观察者更新*/
        observer.handle(state.getState(),internalState.isRunState(),arg);
    }



    /*序列ID*/

    /**
     * 获取此中介对象在通知观察更新消息队列中的位置。由于在同一队列中可能包括多个相同中介对象，所以以序列ID决定
     * 将要执行的那个消息
     * @return 序列ID
     */
    int getSequenceId()
    {
        synchronized (this) {
            return sequenceId;
        }
    }

    /**
     * 生成下一个序列ID
     */
    private void nextSequenceId()
    {
        synchronized (this) {
            sequenceId++;
        }
    }
}
