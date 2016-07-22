package com.virtualightning.library.simple2develop.state;

/**
 * Created by CimZzz on 16/7/21.<br/>
 * Project Name : Virtual-Lightning Simple2Develop<br/>
 * Since : VLSimple2Develop_0.0.1<br/>
 * Description:<br/>
 */
public final class InternalState {
    static final int INTERNAL_STATE_CREATE = 0;
    static final int INTERNAL_STATE_RUN = 1;
    static final int INTERNAL_STATE_STOP = 2;

    private int internalState;

    InternalState()
    {
        internalState = INTERNAL_STATE_CREATE;
    }

    public boolean isRunState()
    {
        return internalState == INTERNAL_STATE_RUN;
    }

    public void setInternalState(int internalState) {
        this.internalState = internalState;
    }
}
