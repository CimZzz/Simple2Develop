package com.virtualightning.library.simple2develop.quickdb.core.command;

import com.virtualightning.library.simple2develop.quickdb.core.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 具有属性的 SQL 命令基类，继承自 SQLCommand<br>
 * <br>相关链接：<br>
 * {@link SQLCommand}<br>
 */
public abstract class PropertySQLCommand extends SQLCommand {
    /**
     * 属性名列表
     */
    protected List<String> propertyNames;


    /**
     * 属性值列表
     */
    protected List<Object> propertyValues;


    /**
     * 属性值数组
     */
    private Object[] valueArray;


    /**
     * 构造方法，初始化属性名列表和属性值列表<br>
     * <br>相关链接：<br>
     * {@link #propertyNames}<br>
     * {@link #propertyValues}<br>
     */
    public PropertySQLCommand()
    {
        propertyNames = new ArrayList<>();
        propertyValues = new ArrayList<>();
    }


    /**
     * 添加一个属性名至属性名列表<br>
     * <br>相关链接：<br>
     * {@link #propertyNames}<br>
     * @param name 属性名
     */
    public void addPropertyName(String name)
    {
        propertyNames.add(name);
    }


    /**
     * 添加一个属性名序列至属性名列表<br>
     * <br>相关链接：<br>
     * {@link #propertyNames}<br>
     * @param names 属性名序列
     */
    public void addPropertyNames(Collection<String> names)
    {
        propertyNames.addAll(names);
    }


    /**
     * 添加一个可变属性名数组至属性名列表<br>
     * <br>相关链接：<br>
     * {@link #propertyNames}<br>
     * @param names 属性名可变数组
     */
    public void addPropertyNames(String... names)
    {
        propertyNames.addAll(Arrays.asList(names));
    }



    /**
     * 添加一个属性值至属性值列表<br>
     * <br>相关链接：<br>
     * {@link #propertyValues}<br>
     * @param value 属性值
     */
    public void addPropertyValue(Object value)
    {
        Object newValue = value;

        /*如果属性值类型为 Date，则会解析成字符串*/
        if(value instanceof Date)
        {
            newValue = DBHelper.genDateValue((Date) value);
        }

        propertyValues.add(newValue);
    }


    /**
     * 添加一个属性值序列至属性值列表<br>
     * <br>相关链接：<br>
     * {@link #propertyValues}<br>
     * @param objects 属性值序列
     */
    public void addPropertyValues(Collection<Object> objects)
    {
        for(Object object : objects)
            addPropertyValue(object);
    }


    /**
     * 添加一个可变属性值数组至属性值列表<br>
     * <br>相关链接：<br>
     * {@link #propertyValues}<br>
     * @param objects 可变属性值数组
     */
    public void addPropertyValues(Object... objects)
    {
        for(Object object : objects)
            addPropertyValue(object);
    }



    /**
     * 根据属性值列表生成属性值数组，返回属性值数组<br>
     * <br>相关链接：<br>
     * {@link #valueArray}<br>
     * {@link #propertyValues}<br>
     * @return 属性值数组
     */
    protected Object[] getValueArray()
    {
        if(valueArray == null)
            valueArray = propertyValues.toArray();

        return valueArray;
    }


    /**
     * 清空属性名列表<br>
     * <br>相关链接：<br>
     * {@link #propertyNames}
     */
    public void clearPropertyNames()
    {
        propertyNames.clear();
    }


    /**
     * 清空属性值列表和属性值数组<br>
     * <br>相关链接：<br>
     * {@link #propertyValues}<br>
     * {@link #valueArray}<br>
     */
    public void clearPropertyValues()
    {
        propertyValues.clear();
        valueArray = null;
    }

    /**
     * 调用 clearPropertyNames() 和 clearPropertyValues() 清空全部<br>
     * <br>相关链接：<br>
     * {@link #clearPropertyNames()}<br>
     * {@link #clearPropertyValues()}<br>
     */
    public void clearAll()
    {
        clearPropertyNames();
        clearPropertyValues();
    }
}
