package com.virtualightning.library.simple2develop.state;

import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by CimZzz on 16/7/22.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.0.1<br>
 * Description:<br>
 * 注解解析器
 */
@SuppressWarnings("unused")
public class Analyzer {



    /*解析一般状态*/

    /**
     * 解析对象的一般状态注解
     * @param record 状态记录
     * @param obj 对象
     */
    public static void analyzeState(StateRecord record,Object obj)
    {
        AnalyzeState stateAnno = obj.getClass().getAnnotation(AnalyzeState.class);
        AnalyzeGlobalState globalStateAnno = obj.getClass().getAnnotation(AnalyzeGlobalState.class);

        if(stateAnno != null) {


            String[] stateNames = stateAnno.stateNames();
            boolean[] states = stateAnno.states();

            if (stateNames.length != states.length)
                throw new RuntimeException("状态名和状态数量不一致：" + obj.getClass().getSimpleName());

            for (int i = 0; i < stateNames.length; i++) {
                record.monitorState(stateNames[i], states[i]);
            }
        }

        if(globalStateAnno != null)
        {
            String[] stateNames = globalStateAnno.value();

            for(String stateName : stateNames)
                record.monitorState(stateName);
        }
    }



    /*解析继承状态和一般状态*/

    /**
     * 解析对象的一般状态注解和继承状态注解
     * @param subRecord 子状态记录
     * @param baseRecord 基状态记录
     * @param obj 对象
     */
    public static void analyzeExtendState(StateRecord subRecord,StateRecord baseRecord,Object obj)
    {
        analyzeState(subRecord,obj);

        AnalyzeExtendState extendStateAnno = obj.getClass().getAnnotation(AnalyzeExtendState.class);

        if(extendStateAnno != null)
            subRecord.monitorState(baseRecord,extendStateAnno.value());
    }



    /*解析视图*/

    /**
     * 解析对象的视图注解
     * @param obj 对象
     * @param rootView 根视图
     */
    public static void analyzeView(Object obj,View rootView)
    {
        Field[] fields = obj.getClass().getDeclaredFields();

        for(Field field : fields)
        {
            AnalyzeView analyzeViewAnno = field.getAnnotation(AnalyzeView.class);

            if(analyzeViewAnno == null)
                continue;

            View view = rootView.findViewById(analyzeViewAnno.value());

            Class fieldCls = field.getType();

            if(fieldCls.isInstance(view))
            {
                field.setAccessible(true);
                try {
                    field.set(obj,view);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("视图获取失败："+obj.getClass().getSimpleName());
                }
            }
            else throw new RuntimeException("视图转型失败："+obj.getClass().getSimpleName()+" 视图名："+field.getName());
        }
    }
}
