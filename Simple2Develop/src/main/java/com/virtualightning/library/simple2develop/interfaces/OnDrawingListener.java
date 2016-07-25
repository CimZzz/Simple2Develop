package com.virtualightning.library.simple2develop.interfaces;

import android.graphics.Canvas;

import com.virtualightning.library.simple2develop.widget.DrawView;

/**
 * Created by CimZzz on 16/7/25.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.3<br>
 * Description:<br>
 * 绘制接口
 */
@SuppressWarnings("unused")
public interface OnDrawingListener {
    void onDraw(DrawView view, Canvas canvas);
}
