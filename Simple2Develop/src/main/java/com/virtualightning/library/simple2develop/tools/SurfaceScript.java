package com.virtualightning.library.simple2develop.tools;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by CimZzz on 16/7/27.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.5<br>
 * Description:<br>
 * Surface脚本<br>
 * 包装了一些简单操作
 */
public abstract class SurfaceScript implements SurfaceHolder.Callback{
    protected SurfaceHolder surfaceHolder;

    public SurfaceScript(SurfaceView view)
    {
        surfaceHolder = view.getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
