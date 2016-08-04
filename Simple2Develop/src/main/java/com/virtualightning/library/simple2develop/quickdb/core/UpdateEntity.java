package com.virtualightning.library.simple2develop.quickdb.core;

import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.exception.VLInitException;

import java.util.Map;

/**
 * Created by CimZzz on 16/6/7.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 持久化对象更新信息实体
 */
public class UpdateEntity {
    /**
     * 更新执行方法
     */
    Update update;



    /**
     * 更新的版本
     */
    Integer version;



    /**
     *更新执行方法的类
     */
    String process;


    /**
     * 解析参数化更新信息实体<br>
     * @param updateMap 享元表
     */
    void analyzeEntity(Map<String,Update> updateMap)
    {
        /*查找享元表内是否已存在该对象，如果存在则无需创建(绝对安全)*/
        update = updateMap.get(process);
        if(update != null)
            return;

        /*享元表内不存在该对象，需要实例化此对象*/
        try {
            /*加载 Update 类，判断是否实现 Update 接口*/
            Class cls = Class.forName(process);
            if(!Update.class.isAssignableFrom(cls))
            {
                throw new VLInitException("Update 类必须实现 \"com.quickdb.Update\" 接口:"+process);
            }

            /*实例化对象并置入享元表内*/
            update = (Update) cls.newInstance();
            updateMap.put(process,update);
        } catch (ClassNotFoundException e) {
            throw new VLInitException("找不到指定的 Update :"+process);
        } catch (InstantiationException e) {
            throw new VLInitException("Update 初始化错误，必须拥有无参数的构造函数:"+process);
        } catch (IllegalAccessException e) {
            throw new VLInitException("Update 无法访问构造函数，请确保无参数的构造函数是公有的:"+process);
        }
    }


    /**
     * 执行更新方法
     * @param db 数据库
     */
    void doUpdate(SQLiteDatabase db)
    {
        update.update(db,version);
    }
}
