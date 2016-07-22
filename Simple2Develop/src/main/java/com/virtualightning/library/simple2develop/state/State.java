package com.virtualightning.library.simple2develop.state;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by CimZzz on 16/7/21.<br/>
 * Project Name : Virtual-Lightning Simple2Develop<br/>
 * Since : VLSimple2Develop_0.0.1<br/>
 * Description:<br/>
 * 状态对象
 */
@SuppressWarnings("unused")
public final class State {
    private boolean state;
    private List<WeakReference<StateMediator>> mediatorReferences;

    State(boolean state)
    {
        this.state = state;
        mediatorReferences = new LinkedList<>();
    }

    /*多个中介者同时共享一个状态对象，保证状态的一致性*/

    /**
     * 添加中介者至状态对象
     * @param mediator 中介者
     */
    void addMediator(StateMediator mediator)
    {
        synchronized (this) {
            mediatorReferences.add(new WeakReference<>(mediator));
        }
    }

    /*获取状态*/

    /**
     * 获取状态
     * @return 返回状态
     */
    boolean getState()
    {
        return state;
    }


    /*状态变更*/

    /**
     * 改变状态至指定状态，在改变的过程中可以附加额外的参数。
     * @param state 改变后的状态
     * @param arg 额外的参数
     */
    void changeState(boolean state,Object... arg)
    {
        synchronized (this)
        {
            this.state = state;

            notifyMediator(arg);
        }
    }


    /*通知中介进行观察者更新，并在更新的过程中删除被回收的实例引用*/

    /**
     * 通知中介进行观察者更新
     * @param arg 额外的参数
     */
    private void notifyMediator(Object... arg)
    {
        Iterator<WeakReference<StateMediator>> iterator = mediatorReferences.iterator();

        while (iterator.hasNext())
        {
            WeakReference<StateMediator> mediatorReference = iterator.next();
            final StateMediator mediator = mediatorReference.get();

            if(mediator == null)
            {
                iterator.remove();
                continue;
            }

            mediator.notifyObserver(true,arg);
        }
    }
}
