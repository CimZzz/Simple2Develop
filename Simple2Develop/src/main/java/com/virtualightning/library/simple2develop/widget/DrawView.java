package com.virtualightning.library.simple2develop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.virtualightning.library.simple2develop.interfaces.OnDrawingListener;

/**
 * Created by CimZzz on 16/7/25.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.3<br>
 * Description:<br>
 * 外接绘制接口视图
 */
@SuppressWarnings("unused")
public class DrawView extends View {
    private OnDrawingListener drawingListener;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*外接绘制接口的设置与调用*/

    /**
     * 设置外接绘制接口
     * @param listener 绘制接口
     */
    public void setDrawingListener(OnDrawingListener listener)
    {
        drawingListener = listener;
    }


    /**
     * 在视图调用Ondraw时绘制外接绘制接口
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawingListener != null)
            drawingListener.onDraw(this,canvas);
    }
}
