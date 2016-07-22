package com.virtualightning.library.simple2develop.state;

/**
 * Created by CimZzz on 16/7/22.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Description:<br>
 * 记忆状态观察者<br>
 * 只有当更新的状态与内部的旧状态不一致时才会执行更新
 */
@SuppressWarnings("unused")
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
    protected void trueStateUpdate(Object... arg) {

    }

    @Override
    protected void falseStateUpdate(Object... arg) {

    }
}
