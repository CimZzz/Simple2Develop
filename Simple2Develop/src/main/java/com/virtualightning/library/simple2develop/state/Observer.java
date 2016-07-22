package com.virtualightning.library.simple2develop.state;

/**
 * Created by CimZzz on 16/7/21.<br/>
 * Project Name : Virtual-Lightning Simple2Develop<br/>
 * Since : VLSimple2Develop_0.0.1<br/>
 * Description:<br/>
 * 状态观察者（基类）
 */
public abstract class Observer {
    private boolean isActived;
    private boolean runWhenStop;

    Observer(boolean runWhenStop)
    {
        this.runWhenStop = runWhenStop;
        this.isActived = false;
    }

    void handle(boolean state,boolean isRunState,Object... arg){
        if(runWhenStop || isRunState) {
            if (verify(state)) {
                if (state)
                    trueState(arg);
                else falseState(arg);
            }
        }
    }


    void setActived(boolean actived)
    {
        this.isActived = actived;
    }

    boolean isActivedObserver()
    {
        return isActived;
    }

    abstract boolean verify(boolean state);
    protected abstract void trueState(Object... arg);
    protected abstract void falseState(Object... arg);
}
