package com.virtualightning.simple2develop.state;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.virtualightning.library.simple2develop.state.AnalyzeGlobalState;
import com.virtualightning.library.simple2develop.state.AnalyzeState;
import com.virtualightning.library.simple2develop.state.AnalyzeView;
import com.virtualightning.library.simple2develop.state.Analyzer;
import com.virtualightning.library.simple2develop.state.PassiveObserver;
import com.virtualightning.library.simple2develop.state.StateManagement;
import com.virtualightning.library.simple2develop.state.StateRecord;
import com.virtualightning.library.simple2develop.ui.ActionBarUI;
import com.virtualightning.library.simple2develop.ui.ActionBarUICreater;
import com.virtualightning.simple2develop.R;

import java.nio.ByteBuffer;

/**
 * Created by CimZzz on 16/8/24.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.8<br>
 * Description:<br>
 */
/*监控全局状态，状态名为 “GS_0”*/
@AnalyzeGlobalState(StateRecordActivity.GLOBAL_STATE_0)

/*监控私有状态，状态名为 “0” ，初始状态为 true*/
@AnalyzeState(
       stateNames = {StateRecordActivity.STATE_0},
       states = {true}
)
public class StateRecordActivity extends ActionBarUI {
    public static final String GLOBAL_STATE_0 = "GS_0";//全局状态
    public static final String STATE_0 = "0";//私有状态

    private StateRecord stateRecord;//状态记录


    /*使用注解快速绑定控件*/

    @AnalyzeView(R.id.state_tv)
    private TextView tv;

    @AnalyzeView(R.id.state_btn)
    private Button btn;



    @Override
    protected void onBaseUICreate(ActionBarUICreater creater) {
        creater.setLayoutID(R.layout.state);

        /*注册全局状态，建议在自定义 Application 类中进行此操作，状态名最好使用静态常量声明*/

        StateRecord.registGlobalState(GLOBAL_STATE_0,true);//注册一个全局状态，状态名为 “GS_0” ，初始状态为 true


        /*通过状态管理生成一个临时的状态记录*/
        stateRecord = StateManagement.getInstance().getTempStateRecord(null);
    }

    @Override
    protected void onViewInit(Bundle savedInstanceState) {
        /*根据注解解析控件并绑定控件*/
        Analyzer.analyzeView(this);

        /*根据注解监控状态*/
        Analyzer.analyzeState(stateRecord,this);

        /*为 Button 注册一个事件*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*启动一个线程模拟耗时处理*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*模拟通过耗时操作获得结果*/
                        String result = "123456";

                        /*通知私有状态发生改变，改变后状态为 true，附加额外的参数为一个字符串*/
                        stateRecord.changeState(STATE_0,true,result);
                    }
                }).start();
            }
        });



        /*注册状态观察者*/

        /*注册一个活性状态观察者（当 StateRecord 的内部状态每次变更为 RunState 时，都会自动激活状态观察者）*/
        /*将状态观察者绑定给监控的全局状态*/
        /*PassiveObserver 为被动状态，表示每次状态激活时都会执行对应状态的操作，其构造参数表示当 StateRecord 处于非 RunState 时，不执行操作*/
        stateRecord.registActiviteObserver(GLOBAL_STATE_0, new PassiveObserver(false) {
            /*当处于 true 状态时执行的操作*/
            @Override
            protected void trueStateUpdate(Object... arg) {
                /*将 TextView 的文本置为 HelloWord */
                tv.setText("状态记录重新激活，活性状态观察者自动触发 ： HelloWord!");
            }

            /*当处于 false 状态时执行的操作*/
            @Override
            protected void falseStateUpdate(Object... arg) {
                //不执行任何操作
            }
        });

        /*注册一个惰性状态观察者（当 StateRecord 的内部状态每次变更不会自动激活状态观察者）*/
        /*将状态观察者绑定给监控的私有状态*/
        /*PassiveObserver 为被动状态，表示每次状态激活时都会执行对应状态的操作，其构造参数表示当 StateRecord 处于非 RunState 时，不执行操作*/
        stateRecord.registInactiveObserver(STATE_0, new PassiveObserver(false) {
            /*当处于 true 状态时执行的操作*/
            @Override
            protected void trueStateUpdate(Object... arg) {
                /*将 TextView 的文本置为传递过来的参数 */
                String result = (String) arg[0];
                tv.setText("状态观察者触发，数据更改 ："+result);
            }

            /*当处于 false 状态时执行的操作*/
            @Override
            protected void falseStateUpdate(Object... arg) {
                //不执行任何操作
            }
        });
    }


    /*监控状态记录内部状态回调*/

    @Override
    protected void onResume() {
        super.onResume();
        /*当宿主处于继续状态时，StateRecord变更状态为 RunState */
        stateRecord.setRunState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*当宿主处于暂停状态时，StateRecord变更状态为 StopState */
        stateRecord.setStopState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*当宿主处于暂停状态时，StateRecord变更状态为 DestoryState */
        stateRecord.setDestroyState();
    }
}
