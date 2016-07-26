package com.virtualightning.simple2develop.analyze.view;

import android.os.Bundle;
import android.view.View;

import com.virtualightning.library.simple2develop.state.AnalyzeView;
import com.virtualightning.library.simple2develop.state.Analyzer;
import com.virtualightning.library.simple2develop.ui.ActionBarUI;
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater;
import com.virtualightning.simple2develop.R;

/**
 * Created by CimZzz on 16/7/26.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.4<br>
 * Description:<br>
 */
public class AnalyzeViewActivity extends ActionBarUI {

    @AnalyzeView(R.id.analyzeview1)
    private View view1;

    @AnalyzeView(R.id.analyzeview2)
    private View view2;

    @Override
    protected void onBaseUICreate(ActionBarUICreater creater) {
        creater.setLayoutID(R.layout.analyzeview);
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState) {
        Analyzer.analyzeView(this);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view2.setVisibility(View.INVISIBLE);
            }
        });
    }
}
