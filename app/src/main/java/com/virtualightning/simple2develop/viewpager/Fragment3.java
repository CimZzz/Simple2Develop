package com.virtualightning.simple2develop.viewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtualightning.library.simple2develop.widget.PagerName;
import com.virtualightning.simple2develop.R;

/**
 * Created by CimZzz on 16/7/25.<br>
 * Project Name : Virtual-Lightning Riding<br>
 * Since : VLRiding_0.0.1<br>
 * Description:<br>
 */
@PagerName(R.string.viewpage_name_3)
public class Fragment3 extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundColor(Color.YELLOW);
        return textView;
    }
}
