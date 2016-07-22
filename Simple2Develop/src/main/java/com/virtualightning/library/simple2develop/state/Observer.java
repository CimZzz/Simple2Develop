package com.virtualightning.library.simple2develop.state;

/**
 * Created by CimZzz on 16/7/21.<br/>
 * Project Name : Virtual-Lightning Simple2Develop<br/>
 * Since : VLSimple2Develop_0.0.1<br/>
 * Description:<br/>
 * 状态观察者（基类）
 */
@SuppressWarnings("unused")
public abstract class Observer {
    private boolean isActived;
    private boolean runWhenStop;

    /*构造时设定状态运行环境*/

    /**
     * 设定状态观察者运行环境，如果为true则无论什么情况下均会执行更新，反之只能在状态记录处于运行状态下才可执行更新
     * @param runWhenStop 性质标识
     */
    Observer(boolean runWhenStop)
    {
        this.runWhenStop = runWhenStop;
        this.isActived = false;
    }

    /*设定状态观察者性质*/

    /**
     * 设定状态观察者性质，如果为true则界定观察者为活性状态观察者，反之为惰性状态观察者
     * @param actived 性质标识
     */
    void setActived(boolean actived)
    {
        this.isActived = actived;
    }

    /**
     * 根据内部性质标识判断是否为活性状态观察者
     * @return 如果是返回true
     */
    boolean isActivedObserver()
    {
        return isActived;
    }


    /*更新处理*/

    /**
     * 处理观察者更新事件（模板方法）
     * @param state 状态
     * @param isRunState 状态记录状态
     * @param arg 额外的参数
     */
    void handle(boolean state,boolean isRunState,Object... arg){
        if(runWhenStop || isRunState) {
            if (verify(state)) {
                if (state)
                    trueStateUpdate(arg);
                else falseStateUpdate(arg);
            }
        }
    }

    /**
     * 检测此状态是否满足更新条件
     * @param state 状态
     * @return 如果满足返回true
     */
    abstract boolean verify(boolean state);

    /**
     * 当处于true状态下更新操作
     * @param arg 额外的参数
     */
    protected abstract void trueStateUpdate(Object... arg);

    /**
     * 当处于false状态下更新操作
     * @param arg 额外的参数
     */
    protected abstract void falseStateUpdate(Object... arg);
}
