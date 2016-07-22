package com.virtualightning.library.simple2develop.state;

/**
 * Created by CimZzz on 16/7/22.<br/>
 * Project Name : Virtual-Lightning Simple2Develop<br/>
 * Since : VLSimple2Develop_0.0.1<br/>
 * Description:<br/>
 * 记忆状态观察者
 */
public abstract class MemoryObserver extends Observer{
    private Boolean oldState;

    public MemoryObserver(boolean runWhenStop) {
        super(runWhenStop);
    }

    @Override
    final synchronized boolean verify(boolean state) {
        boolean change = oldState == null || !oldState.equals(state);

        oldState = state;

        return change;
    }

    @Override
    protected void trueState(Object... arg) {

    }

    @Override
    protected void falseState(Object... arg) {

    }
}
