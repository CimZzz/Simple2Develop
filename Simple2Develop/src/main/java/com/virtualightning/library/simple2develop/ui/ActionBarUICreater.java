package com.virtualightning.library.simple2develop.ui;

/**
 * Created by CimZzz on 16/6/26.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.3<br>
 * Modify : VLSimple2Develop_0.2.4 添加是否显示工具栏选项<br>
 * Description:<br>
 * 界面生成器
 * 包装了一些生成界面所需必要的属性和额外的属性
 */
@SuppressWarnings("unused")
public class ActionBarUICreater {
    /**
     * 布局ID
     */
    private Integer layoutID;

    /**
     * ActionBar布局ID
     */
    private Integer actionBarID;

    /**
     * 是否显示工具栏
     */
    private boolean hasToolBar;



    ActionBarUICreater()
    {
        hasToolBar = true;
    }


    /*检验Creater是否合法*/
    boolean validate()
    {
        return (layoutID != null);
    }


    /*Getter & Setter*/

    Integer getLayoutID() {
        return layoutID;
    }

    public ActionBarUICreater setLayoutID(Integer layoutID) {
        this.layoutID = layoutID;

        return this;
    }


    Integer getActionBarID() {
        return actionBarID;
    }

    public ActionBarUICreater setActionBarID(Integer actionBarID) {
        this.actionBarID = actionBarID;

        return this;
    }

    public void setHasToolBar(boolean hasToolBar) {
        this.hasToolBar = hasToolBar;
    }

    boolean hasToolBar() {
        return hasToolBar;
    }
}
