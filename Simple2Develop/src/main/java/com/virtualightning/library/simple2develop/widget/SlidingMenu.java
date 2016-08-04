package com.virtualightning.library.simple2develop.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.virtualightning.library.simple2develop.R;

/**
 * Created by CimZzz on 16/8/3.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Modify : VLSimple2Develop_0.1.1 开放滚动接口供开发者使用<br>
 * Description:<br>
 * 滑动菜单
 */
public final class SlidingMenu extends ViewGroup {
    private Scroller scroller;
    private OnScrollListener scrollListener;

    private View menuView;
    private View contentView;

    private boolean isDirectionLeft;
    private boolean isMenuState;

    private float menuWidth;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        scroller = new Scroller();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);

        int menuId = array.getResourceId(R.styleable.SlidingMenu_menu, -1);
        int contentId = array.getResourceId(R.styleable.SlidingMenu_content, -1);
        isDirectionLeft = array.getInt(R.styleable.SlidingMenu_direction, 0) == 0;

        menuWidth = array.getFraction(R.styleable.SlidingMenu_menuWidth, 1, 1, -1);

        LayoutInflater inflater = LayoutInflater.from(context);

        if (menuId != -1) {
            menuView = inflater.inflate(menuId, null);
            addView(menuView);
        }

        if (contentId != -1) {
            contentView = inflater.inflate(contentId, null);
            addView(contentView);
        }

        array.recycle();
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (contentView == child || menuView == child)
            super.addView(child, index, params);
        else if (contentView == null && child.getTag().equals("Content")) {
            contentView = child;
            super.addView(child, index, params);
        } else if (menuView == null && child.getTag().equals("Menu")) {
            menuView = child;
            super.addView(child, index, params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int menuViewWidth = (int) (menuWidth != -1 ? width * menuWidth : menuWidth);

        if (contentView != null)
            contentView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

        if (menuView != null)
            menuView.measure(MeasureSpec.makeMeasureSpec(menuViewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null)
            contentView.layout(l, t, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());

        if (menuView != null)
            if (isDirectionLeft)
                menuView.layout(-menuView.getMeasuredWidth(), t, 0, menuView.getMeasuredHeight());
            else menuView.layout(r, t, r + menuView.getMeasuredWidth(), menuView.getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!scroller.isFinished() && scroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = scroller.getCurrentX();
            int y = scroller.getCurrentY();

            if (oldX != x || oldY != y) {
                super.scrollTo(x, y);
            }

            if(scrollListener != null)
                scrollListener.onScroll(isMenuState,menuView,contentView,scroller.currentReciprocal);
            // Keep on drawing until the animation has finished.
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public final void scrollTo(int x, int y) {
    }

    @Override
    public final void scrollBy(int x, int y) {
    }



    private void smoothTo(int x,int y)
    {
        if(menuView == null)
            return;
        if(!scroller.isFinished())
            scroller.abortAnimation();

        int startX = scroller.getCurrentX();
        int startY = scroller.getCurrentY();

        int distanceX = x - startX;
        int distanceY = y - startY;

        int duration = (int) (((float)Math.abs(distanceX) / menuView.getWidth() + 1)) * 100;

        scroller.startScroll(startX,startY,distanceX,distanceY,duration);

        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setOnScrollListener(OnScrollListener scrollListener)
    {
        this.scrollListener = scrollListener;
    }

    public void setContentView(View view) {
        if(!scroller.isFinished())
            throw new IllegalStateException("不能在滚动时切换视图");

        if (contentView != null)
            removeView(contentView);

        contentView = view;
        addView(view);
    }

    public void setMenuView(View view, float menuWidthPercent) {
        if(!scroller.isFinished())
            throw new IllegalStateException("不能在滚动时切换视图");

        if (menuView != null)
            removeView(menuView);

        menuView = view;
        this.menuWidth = menuWidthPercent;
        addView(view);
    }

    public void toggleMenu()
    {
        if(menuView == null || contentView == null)
            return;

        final int finalX = isDirectionLeft ? -menuView.getWidth() : getWidth() + menuView.getWidth();
        final int finalY = 0;

        smoothTo(finalX,finalY);

        isMenuState = true;
    }

    public void toggleContent()
    {

        if(menuView == null || contentView == null)
            return;

        final int finalX = 0;
        final int finalY = 0;

        smoothTo(finalX,finalY);

        isMenuState = false;
    }

    public void toggleOther()
    {
        synchronized (this) {
            if (isMenuState)
                toggleContent();
            else toggleMenu();
        }
    }



    /*内部类，滚动助手*/
    private class Scroller{
        private int startX;
        private int startY;
        private int currentX;
        private int currentY;
        private int finalX;
        private int finalY;

        private float deltaX;
        private float deltaY;

        private long startTime;
        private int duration;
        private float durationReciprocal;
        private float currentReciprocal;
        private boolean finished;

        void startScroll(int startX, int startY, int dx, int dy, int duration)
        {
            this.finished = false;
            this.duration = duration;
            this.startTime = AnimationUtils.currentAnimationTimeMillis();
            this.startX = startX;
            this.startY = startY;
            this.finalX = startX + dx;
            this.finalY = startY + dy;
            this.deltaX = dx;
            this.deltaY = dy;
            this.durationReciprocal = 1.0f / (float) this.duration;
        }


        boolean computeScrollOffset()
        {
            if (finished) {
                return false;
            }

            int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - startTime);

            if (timePassed < duration) {
                currentReciprocal = timePassed * durationReciprocal;
                currentX = startX + Math.round(currentReciprocal * deltaX);
                currentY = startY + Math.round(currentReciprocal * deltaY);
            }
            else {
                currentX = finalX;
                currentY = finalY;
                finished = true;
            }
            return true;
        }

        void abortAnimation() {
            finished = true;
        }


        int getCurrentX() {
            return currentX;
        }

        int getCurrentY() {
            return currentY;
        }

        float getCurrentReciprocal() {
            return currentReciprocal;
        }

        public boolean isFinished() {
            return finished;
        }
    }

    public interface OnScrollListener
    {
        void onScroll(boolean isMenuState,View menu,View content,float currentPercent);
    }
}