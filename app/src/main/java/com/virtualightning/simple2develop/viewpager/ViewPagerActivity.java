package com.virtualightning.simple2develop.viewpager;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.virtualightning.library.simple2develop.state.AnalyzeView;
import com.virtualightning.library.simple2develop.state.Analyzer;
import com.virtualightning.library.simple2develop.widget.FragmentViewPager;
import com.virtualightning.simple2develop.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    @AnalyzeView(R.id.viewpager)
    private FragmentViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        Analyzer.analyzeView(this,getWindow().getDecorView());

        List<Class<? extends Fragment>> fragmentList = new ArrayList<>();
        fragmentList.add(Fragment1.class);
        fragmentList.add(Fragment2.class);
        fragmentList.add(Fragment3.class);
        fragmentList.add(Fragment4.class);
        viewPager.setFragmentAdapter(this,fragmentList);
    }
}
