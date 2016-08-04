package com.virtualightning.library.simple2develop.quickdb.core;

import com.virtualightning.library.simple2develop.quickdb.exception.VLSQLException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CimZzz on 16/6/9.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 持久化对象实体<br>
 * 保存了持久化对象的属性对象和表名，避免了每次需要反射获取
 */
public class Entity {
    /**
     * 持久化对象表名
     */
    String tableName;





    /**
     * 属性表
     */
    Map<String,Field> properties;





    /**
     * 构造函数，构建属性表
     */
    public Entity()
    {
        properties = new HashMap<>();
    }





    /**
     * 根据属性名查找属性表，生成属性列表<br>
     *
     * <br>相关链接：<br>
     * {@link #properties}
     *
     * @param names 属性名
     * @return 属性列表
     */
    public Collection<Field> getFieldList(String... names)
    {
        /*如果属性名为空则返回全部属性*/
        if(names == null)
            return properties.values();

        /*根据属性名生成属性列表*/
        List<Field> fields = new ArrayList<>();
        for(String name : names)
        {
            /*判断此属性名是否有对应的属性*/
            Field field = properties.get(name);
            if(field == null)
            {
                throw new VLSQLException(tableName+"表中没有"+name+"属性");
            }
            fields.add(field);
        }

        return fields;
    }


    /**
     * 获得全部的属性名
     * @return 属性名集
     */
    public Collection<String> getAllTBP()
    {
        return properties.keySet();
    }
}
