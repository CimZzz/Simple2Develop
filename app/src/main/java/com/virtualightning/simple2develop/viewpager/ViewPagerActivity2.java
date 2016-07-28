package com.virtualightning.simple2develop.viewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.virtualightning.library.simple2develop.state.AnalyzeView;
import com.virtualightning.library.simple2develop.state.Analyzer;
import com.virtualightning.library.simple2develop.widget.ImprovedViewPager;
import com.virtualightning.simple2develop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CimZzz on 16/7/25.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.3<br>
 * Description:<br>
 */
public class ViewPagerActivity2 extends AppCompatActivity {
    @AnalyzeView(R.id.viewpager)
    private ImprovedViewPager viewPager;

    private List<View> views = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        Analyzer.analyzeView(this,getWindow().getDecorView());

        TextView textView;

        textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundColor(Color.RED);
        views.add(textView);

        textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundColor(Color.BLACK);
        views.add(textView);

        textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundColor(Color.BLUE);
        views.add(textView);

        textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundColor(Color.CYAN);
        views.add(textView);

        textView = new TextView(this);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundColor(Color.GREEN);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Class<? extends Fragment>> fragmentList = new ArrayList<>();
                fragmentList.add(Fragment1.class);
                fragmentList.add(Fragment2.class);
                fragmentList.add(Fragment3.class);
                fragmentList.add(Fragment4.class);
                viewPager.setFragmentAdapter(ViewPagerActivity2.this,fragmentList);
            }
        });
        views.add(textView);

        viewPager.setPagerAdapter(this,new ImprovedViewPager.AbstractPagerAdapter() {

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views.get(position);

                container.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
               return view == object;
            }

            @Override
            public Bundle getPagerBundle() {
                return null;
            }

            @Override
            public void resetData() {
            }
        });
    }
}
