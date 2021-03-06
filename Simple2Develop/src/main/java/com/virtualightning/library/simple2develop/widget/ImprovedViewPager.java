package com.virtualightning.library.simple2develop.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.virtualightning.library.simple2develop.R;

import com.virtualightning.library.simple2develop.interfaces.OnDrawingListener;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by CimZzz on 16/7/25.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.3<br>
 * Description:<br>
 * FragmentViewPager
 */
@SuppressWarnings("unused")
public class ImprovedViewPager extends LinearLayout implements ViewPager.OnPageChangeListener{

    /*内部参数*/

    /**
     * 判断是否存在标签栏
     */
    private boolean existTab;

    /**
     * 标签栏最大同时存在标签数
     */
    private int tabCount;

    /**
     * 标签宽度
     */
    private int tabWidth;

    /**
     * 标签高度
     */
    private int tabHeight;

    /**
     * 显示中最后一个标签项下标，用于滑动时自动滑动标题栏
     */
    private int lastTabPostion;

    /**
     * 判断是否处于初始化状态
     */
    private boolean isInit = false;


    /*子控件*/

    /**
     * 横向滚动视图
     */
    private TabLayout scrollView;

    /**
     * 标签栏容器
     */
    private LinearLayout tabContent;

    /**
     * ViewPager容器
     */
    private CustomViewPager viewPager;

    /**
     * Tab标题栏绘图视图
     */
    private DrawView drawView;

    /**
     * BaseUIPager适配器
     */
    private PagerAdapterCallBack adapterCallBack;

    /**
     * BaseUIPager标签适配器
     */
    private BaseUIPagerTabFactory tabViewFactory;

    /**
     * 重绘辅助类，拥有标签栏滑动的时候重绘
     */
    private DrawHelper drawHelper;

    public ImprovedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        isInit = true;

        viewPager = new CustomViewPager(context);
        viewPager.setId(viewPager.hashCode());
        viewPager.addOnPageChangeListener(this);

        setOrientation(VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ImprovedViewPager);


        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;

        viewPager.setLayoutParams(params);

        existTab = typedArray.getBoolean(R.styleable.ImprovedViewPager_existTab, false);

        boolean canTouchScroll = typedArray.getBoolean(R.styleable.ImprovedViewPager_canTouchScroll,true);

        viewPager.setCanTouchScroll(canTouchScroll);

        if(existTab)
        {
            FrameLayout tabLayout = new FrameLayout(context);
            tabContent = new LinearLayout(context);
            tabContent.setOrientation(HORIZONTAL);

            drawView = new DrawView(context);
            drawView.setDrawingListener((drawHelper = new DrawHelper()));

            scrollView = new TabLayout(context);
            scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
            scrollView.setHorizontalScrollBarEnabled(false);
            scrollView.setFillViewport(true);

            tabHeight = typedArray.getDimensionPixelSize(R.styleable.ImprovedViewPager_tabHeight, -1);

            if(tabHeight == -1)
                tabHeight = context.getResources().getDimensionPixelSize(R.dimen.viewpage_tab_height);

            tabCount = typedArray.getInt(R.styleable.ImprovedViewPager_tabCount, -1);
            if(tabCount == -1)
                tabCount = 3;

            drawHelper.start = 0;
            lastTabPostion = tabCount;

            boolean reverseTab = typedArray.getBoolean(R.styleable.ImprovedViewPager_reverseTab,false);

            tabLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tabHeight));
            drawView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            scrollView.addView(tabContent);
            tabLayout.addView(scrollView);
            tabLayout.addView(drawView);

            if(reverseTab) {
                addView(viewPager);
                addView(tabLayout);
            }
            else {
                addView(tabLayout);
                addView(viewPager);
            }
        }
        else addView(viewPager);


        typedArray.recycle();

        isInit = false;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(isInit)
            super.addView(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(existTab && tabWidth == -1)
        {
            int width = MeasureSpec.getSize(widthMeasureSpec);

            int realCount = tabContent.getChildCount();

            tabWidth = realCount >= tabCount ? width / tabCount : width / realCount;

            for (int i = 0 ; i < realCount ; i++) {
                tabContent.getChildAt(i).getLayoutParams().width = tabWidth;
                tabContent.getChildAt(i).getLayoutParams().height = tabHeight;
            }

            drawHelper.tabWidth = tabWidth;

            scrollView.getLayoutParams().width = width;
            scrollView.getLayoutParams().height = tabHeight;

            tabContent.getLayoutParams().width = width;
            tabContent.getLayoutParams().height = tabHeight;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(existTab){
            adjustTab(position);
            drawHelper.position = position;
            drawHelper.offset = positionOffset;

            scrollView.redrawTab();
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void adjustTab(int position)
    {
        if(position >= lastTabPostion - 1)
        {
            lastTabPostion = lastTabPostion + 1;
            scrollView.smoothScrollTo((lastTabPostion - tabCount + 1) * tabWidth,0);
        }
        else if  (position <= lastTabPostion - tabCount)
        {
            lastTabPostion = lastTabPostion - 1;
            scrollView.smoothScrollTo((lastTabPostion - tabCount) * tabWidth,0);
        }
    }


    private View generateTabItem(AppCompatActivity activity, final int position,@Nullable Class pageCls)
    {
        View tabItemView;
        if (tabViewFactory != null)
        {
            tabItemView = tabViewFactory.generatedView(activity.getLayoutInflater(),position);

            OnClickListener clickListener = tabViewFactory.generatedOnClickListener(viewPager,position);

            tabItemView.setOnClickListener(clickListener == null ? new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position, true);
                }
            } : clickListener);
        }
        else{
            TextView textView = new TextView(activity);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            int nameID = -1;
            String nameStr = "";
            if(pageCls != null) {
                PagerName pagerName = (PagerName) pageCls.getAnnotation(PagerName.class);
                nameID = pagerName != null ? pagerName.value() : -1;
                nameStr = pagerName != null ? pagerName.name() : "";
            }

            //noinspection ResourceType
            textView.setText(nameID != -1 ?
                    activity.getText(nameID)
                    : !nameStr.equals("") ?
                    nameStr
                    : "无名字");

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position, true);
                }
            });
            tabItemView = textView;
        }


        return tabItemView;
    }










    public void changeCurrentPage(int position)
    {
        viewPager.setCurrentItem(position,true);
    }

    public Bundle getPagerBundle()
    {
        return adapterCallBack != null ? adapterCallBack.getPagerBundle() : null;
    }

    public void resetData()
    {
        if(adapterCallBack != null)
            adapterCallBack.resetData();
    }

    public int size()
    {
        return adapterCallBack != null ? adapterCallBack.getCount() : -1;
    }


    /*设置适配器*/
    public final void setPagerAdapter(AppCompatActivity activity,AbstractPagerAdapter adapter)
    {
        adapterCallBack = adapter;
        viewPager.setAdapter(adapter);

        if(existTab) {
            /*使用变量i 与 position 设定点击事件定位的位置*/
            int length = adapter.getCount();

            tabContent.removeAllViews();
            for (int i = 0 ; i < length ; i++) {
                tabContent.addView(generateTabItem(activity,i,null));
            }

            tabWidth = -1;
        }

    }

    @SafeVarargs
    public final void setFragmentAdapter(AppCompatActivity activity, Class<? extends Fragment>... pagerCls)
    {
        List<Class<? extends Fragment>> clsList = new ArrayList<>();

        Collections.addAll(clsList, pagerCls);

        setFragmentAdapter(activity,clsList);
    }

    public final void setFragmentAdapter(AppCompatActivity activity, List<Class<? extends Fragment>> pagerCls)
    {
        PagerAdapter adapter = new PagerAdapter(activity.getSupportFragmentManager(), pagerCls);
        adapterCallBack = adapter;
        viewPager.setAdapter(adapter);

        if(existTab) {
            /*使用变量i 与 position 设定点击事件定位的位置*/
            int i = 0;

            tabContent.removeAllViews();
            for (Class cls : pagerCls) {
                tabContent.addView(generateTabItem(activity,i,cls));
                i++;
            }

            tabWidth = -1;
        }
    }

    public void setDrawAdapterListener(DrawScrollBarListener listener)
    {
        if(drawHelper == null)
            return;

        drawHelper.adapterListener = listener;
    }

    public void setBaseUIPagerTabFactory(BaseUIPagerTabFactory factory)
    {
        tabViewFactory = factory;
    }

    public void addPagerChangeListener(ViewPager.OnPageChangeListener listener)
    {
        viewPager.addOnPageChangeListener(listener);
    }

    public void setCanTouchScroll(boolean canTouchScroll)
    {
        viewPager.setCanTouchScroll(canTouchScroll);
    }








    /*自定义内部类*/






    /*自定义ViewPager*/
    private static class CustomViewPager extends ViewPager{
        private boolean canTouchScroll;

        public CustomViewPager(Context context) {
            super(context);
        }

        /*滑动控制*/
        public void setCanTouchScroll(boolean canTouchScroll)
        {
            this.canTouchScroll = canTouchScroll;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return canTouchScroll && super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            /*判断是否支持滑动控制*/
            return canTouchScroll && super.onTouchEvent(ev);
        }
    }




    /*ImprovedViewPager 适配器抽象类*/

    public static abstract class AbstractPagerAdapter extends android.support.v4.view.PagerAdapter
            implements PagerAdapterCallBack{

    }


    /*ImprovedViewPager Fragment适配器*/

    private static class PagerAdapter extends FragmentPagerAdapter implements PagerAdapterCallBack{
        private boolean isReseting;
        private final Bundle dataBundle;
        private HashMap<Class<? extends Fragment>,SoftReference<Fragment>> fragmentMap;
        private List<Class<? extends Fragment>> fragmentCls;

        public PagerAdapter(FragmentManager manager,List<Class<? extends Fragment>> fragmentCls) {
            super(manager);
            isReseting = false;
            dataBundle = new Bundle();
            fragmentMap = new LinkedHashMap<>();
            this.fragmentCls = fragmentCls;

            for(Class<? extends Fragment> cls : fragmentCls)
                fragmentMap.put(cls,null);
        }

        @Override
        public synchronized Fragment getItem(int position) {
            Class<? extends Fragment> cls = fragmentCls.get(position);
            SoftReference<Fragment> fragmentReference = fragmentMap.get(cls);
            Fragment fragment;

            if(fragmentReference == null || (fragment = fragmentReference.get()) == null)
            {
                try {
                    fragment = cls.newInstance();
                    fragmentMap.put(cls,new SoftReference<>(fragment));
                } catch (Throwable e) {
                    throw new RuntimeException("Fragment无法实例化，必须拥有一个无参的构造函数");
                }
            }

            return fragment;
        }


        @Override
        public int getCount() {
            return fragmentCls.size();
        }

        public void resetData()
        {
            synchronized (this) {
                if (isReseting)
                    return;

                isReseting = true;
            }

            for (SoftReference<Fragment> reference : fragmentMap.values()) {
                Fragment fragment;

                if (reference == null || (fragment = reference.get()) == null)
                    continue;

                fragment.onDestroy();
            }

            isReseting = false;
        }

        public Bundle getPagerBundle()
        {
            synchronized (dataBundle){
                for (SoftReference<Fragment> reference : fragmentMap.values()) {
                    Fragment fragment;

                    if (reference == null || (fragment = reference.get()) == null)
                        continue;

                    fragment.onSaveInstanceState(dataBundle);
                }
            }

            return dataBundle;
        }
    }








    /*标签容器*/

    private class TabLayout extends HorizontalScrollView {

        public TabLayout(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);
            redrawTab();
        }

        /**
         * 重绘滚动条
         */
        public void redrawTab()
        {
            drawHelper.start = (drawHelper.position + drawHelper.offset) * tabWidth - getScrollX();
            drawView.invalidate();
        }
    }






    /*滚动条视图绘制接口*/

    private class DrawHelper implements OnDrawingListener {
        private DrawScrollBarListener adapterListener;

        float tabWidth;
        float offset;
        float start;
        int position;

        private DrawHelper()
        {
            adapterListener = new DefaultDrawAdapterListener();
        }

        @Override
        public void onDraw(DrawView view, Canvas canvas) {
            adapterListener.onDrawScrollBar(view,canvas,start,tabWidth);
        }
    }







    /*生成标签项视图工厂*/

    public interface BaseUIPagerTabFactory{
        View generatedView(LayoutInflater inflater, final int position);
        OnClickListener generatedOnClickListener(
                ViewPager pager,
                final int position);
    }





    /*绘制滚动条接口*/

    public interface DrawScrollBarListener{
        void onDrawScrollBar(View view, Canvas canvas, float start, float tabWidth);
    }






    /*默认的滚动条接口*/

    private static class DefaultDrawAdapterListener implements DrawScrollBarListener{
        private Paint paint = new Paint();

        @Override
        public void onDrawScrollBar(View view, Canvas canvas, float start, float tabWidth) {
            final float right = start + tabWidth;
            final float top = view.getHeight()- 15;
            final float bottom = view.getHeight();
            final float triangleHalfWidth = 20;
            final float triangleCenter = (start + right)/ 2;

            paint.setColor(Color.BLACK);


            Path path = new Path();
            path.moveTo(triangleCenter - triangleHalfWidth,top);
            path.lineTo(triangleCenter, top - 20);
            path.lineTo(triangleCenter + triangleHalfWidth,top);
            path.close();
            canvas.drawRect(start, top, right, bottom, paint);
            canvas.drawPath(path,paint);
        }
    }





    /*ImprovedViewPager回调接口*/
    public interface PagerAdapterCallBack{
        Bundle getPagerBundle();
        void resetData();
        int getCount();
    }
}
