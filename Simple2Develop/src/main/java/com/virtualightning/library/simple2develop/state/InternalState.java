package com.virtualightning.library.simple2develop.state;

/**
 * Created by CimZzz on 16/7/21.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Description:<br>
 * 状态记录的内部状态
 */
@SuppressWarnings("unused")
public final class InternalState {
    static final int INTERNAL_STATE_CREATE = 0;
    static final int INTERNAL_STATE_RUN = 1;
    static final int INTERNAL_STATE_STOP = 2;

    private int internalState;

    /*构造时设定默认状态*/

    /**
     * 构造函数，默认状态为{@link #INTERNAL_STATE_CREATE}（创建状态）
     */
    InternalState()
    {
        internalState = INTERNAL_STATE_CREATE;
    }

    /*状态的设置与判断*/

    /**
     * 判断当前的内部状态是否处于运行状态。
     * @return 如果当前内部状态为{@link #INTERNAL_STATE_RUN}时返回true
     */
    boolean isRunState()
    {
        return internalState == INTERNAL_STATE_RUN;
    }

    /**
     * 设置内部状态
     * @param internalState 内部状态
     */
    public void setInternalState(int internalState) {
        this.internalState = internalState;
    }
}
