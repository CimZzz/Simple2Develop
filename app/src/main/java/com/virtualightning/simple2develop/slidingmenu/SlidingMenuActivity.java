package com.virtualightning.simple2develop.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.virtualightning.library.simple2develop.state.AnalyzeView;
import com.virtualightning.library.simple2develop.state.Analyzer;
import com.virtualightning.library.simple2develop.ui.ActionBarUI;
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater;
import com.virtualightning.library.simple2develop.widget.SlidingMenu;
import com.virtualightning.simple2develop.R;

import java.util.Random;

/**
 * Created by CimZzz on 16/8/3.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.1<br>
 * Description:<br>
 * 滑动菜单
 */
public class SlidingMenuActivity extends ActionBarUI {
    @AnalyzeView(R.id.sliding)
    private View slidingBtn;

    @AnalyzeView(R.id.slidingmenu)
    private SlidingMenu slidingMenu;

    @Override
    protected void onBaseUICreate(ActionBarUICreater creater) {
        creater.setActionBarID(R.layout.actionbar_title_sliding).setLayoutID(R.layout.slidingmenu);
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState) {
        Analyzer.analyzeView(this);
        slidingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingMenu.toggleOther();
            }
        });
    }
}
