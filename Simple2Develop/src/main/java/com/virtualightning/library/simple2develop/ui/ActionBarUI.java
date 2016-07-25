package com.virtualightning.library.simple2develop.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.virtualightning.library.simple2develop.R;

/**
 * Created by CimZzz on 16/7/23.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.3<br>
 * Description:<br>
 * 自定义ActionBarActivity
 */
@SuppressWarnings("unused")
public abstract class ActionBarUI extends AppCompatActivity {
    private static final int ACTIONBAR_DEFAULT_DP = 50;
    private static Integer actionBarHeight;
    private ViewGroup rootView;
    private ViewGroup actionBarView;
    private ViewGroup contentView;


    /*定义模板方法*/

    /**
     * 定义模板方法，根据流程创建Activity
     * @param savedInstanceState 保存的状态
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(actionBarHeight == null)
        {
            actionBarHeight =
                    (int) (getResources().getDisplayMetrics().density
                            * ACTIONBAR_DEFAULT_DP
                            + 0.5f);
        }

        ActionBarUICreater creater = new ActionBarUICreater();

        onBaseUICreate(creater);

        if(!creater.validate())
        {
            throw new RuntimeException("ActivityCreater缺少必要的属性！");
        }

        LayoutInflater inflater = getLayoutInflater();

        setContentView(R.layout.baseui_activity);
        rootView = (ViewGroup) findViewById(R.id.baseui_activity_rootview);
        actionBarView = (ViewGroup) rootView.findViewById(R.id.baseui_activity_actionbar);
        contentView = (ViewGroup) rootView.findViewById(R.id.baseui_activity_content);

        Integer actionBarID = creater.getActionBarID();
        if(actionBarID != null)
        {
            actionBarView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    actionBarHeight
            ));
            actionBarView.addView(inflater.inflate(actionBarID,null));
        }
        else actionBarView = null;
        contentView.addView(inflater.inflate(creater.getLayoutID(), null));

        onViewInit(savedInstanceState);
    }



    /**
     * 通过BaseUIFragment创建者设置BaseUIFragment创建参数
     * @param creater BaseUI创建者
     */
    protected abstract void onBaseUICreate(ActionBarUICreater creater);

    /**
     * 进行BaseUI上视图的初始化
     * @param savedInstanceState 保存的内部数据
     */
    protected abstract void onViewInit(Bundle savedInstanceState);




    /*工具方法，提供视图的提取*/

    /**
     * 获得根视图
     * @return 根视图
     */
    public final ViewGroup getRootView() {
        return rootView;
    }

    /**
     * 获得ActionBar容器
     * @return actionBar容器
     */
    public final ViewGroup getActionBarView() {
        return actionBarView;
    }

    /**
     * 获得内容容器
     * @return 内容容器
     */
    public final ViewGroup getContentView() {
        return contentView;
    }
}
