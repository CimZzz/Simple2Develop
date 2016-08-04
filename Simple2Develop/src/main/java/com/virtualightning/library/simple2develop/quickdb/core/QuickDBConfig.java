package com.virtualightning.library.simple2develop.quickdb.core;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.exception.VLInitException;
import com.virtualightning.library.simple2develop.quickdb.util.LogUtils;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by CimZzz on 16/6/7.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 配置信息<br>
 * 保存：<br>
 * 数据库版本 version<br>
 * 数据库名称 dbName<br>
 * 持久化对象信息实体哈希表 entities<br>
 *
 * QuickDB 会根据配置文件构造 DBHelper<br>
 * <br>相关链接：<br>
 * {@link QuickDB}<br>
 * {@link #version}<br>
 * {@link #dbName}<br>
 * {@link #entities}
 */
public class QuickDBConfig{
    /**
     * 数据库版本号
     */
    Integer version;






    /**
     * 数据库名称
     */
    String dbName;






    /**
     * 是否显示创建过程日志
     */
    Boolean showCreateLog;







    /**
     * 持久化对象信息实体列表
     */
    List<ReadEntity> entities;





    /**
     * 持久化对象实体表
     */
    Map<Class,Entity> entityMap;



    /**
     * 不可见的构造方法，只允许在 QuickDB.InitQuickDB(Context, int) 中构造
     * 实例化了实体列表，初始化日志信息
     *
     * {@link QuickDB#initQuickDB(Context, int)}
     */
    QuickDBConfig()
    {
        entities = new ArrayList<>();
        entityMap = new HashMap<>();

        showCreateLog = false;
    }





    /**
     * 根据 xml 文件初始化配置信息，使用 AnalyzeHandler 进行 xml 解析
     *
     * {@link QuickDBHandler}
     *
     * @param context 上下文
     * @param xmlId xmlId(必须要在 res/raw 文件夹下)
     */
    void initQuickDBConfig(Context context,int xmlId)
    {
        String name = context.getResources().getResourceName(xmlId);

        if(!name.substring(name.indexOf(":")+1,name.indexOf("/")).equals("raw"))
        {
            throw new VLInitException("资源文件后缀名不正确！必须为\".xml\"结尾放在\"res/raw\"的XML文件");
        }

        InputStream source = context.getResources().openRawResource(xmlId);

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();


            parser.parse(source,new QuickDBHandler(this));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new VLInitException("解析 XML 文件发生错误:"+e.getMessage());
        } finally {
            try {
                source.close();
            } catch (IOException ignored) {
            }
        }

        initDatabase(context);
    }


    /**
     * 初始化数据库
     * @param context 上下文
     */
    void initDatabase(Context context)
    {
        SQLiteDatabase database = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null, null);

        /*获取当前数据库版本号*/
        int oldVersion = database.getVersion();
        if(showCreateLog)
        {
            LogUtils.createLog("获取本地数据库版本号：" + oldVersion);
            LogUtils.createLog("目标数据库版本号："+version);
        }

        /*判断数据库版本，如果旧版本大于当前版本则抛出异常*/
        if (oldVersion > version)
        {
            throw new VLInitException("旧版本不能大于当前版本，旧版本为："+oldVersion);
        }


        /*验证每个持久化对象信息实体；添加持久化实体至持久化实体表中；建立Update对象享元表；建立外键语句列表*/
        if(showCreateLog)
        {
            LogUtils.createLog("开始解析持久化对象信息实体");
        }
        Map<Class,Boolean> checkMap = new HashMap<>();
        Map<String,Update> updateMap = new HashMap<>();
        List<String> foreignSQL = new ArrayList<>();
        for(ReadEntity entity : entities)
        {
            Entity entitY = entity.analyzeEntity(checkMap, updateMap,foreignSQL,oldVersion);
            entityMap.put(entity.cls, entitY);

            if(showCreateLog)
            {
                LogUtils.createLog("持久化对象信息实体："+entity.cls.getName() +" 完成解析");
            }
        }
        if(showCreateLog)
        {
            LogUtils.createLog("持久化对象信息实体解析完成");
        }


        /*判断数据库版本，如果版本相同则不做下列操作*/
        if (oldVersion == version)
        {

            if(showCreateLog) {
                LogUtils.createLog("版本相同，不执行建表与更新操作");
            }

            database.close();
            return;
        }


        /*遍历校验表，查看是否有外键是不存在的*/
        for(Class cls : checkMap.keySet())
        {
            if(!checkMap.get(cls))
            {
                throw new VLInitException(cls.getName()+"必须要在 XMl 文件中定义外键的 Entity 元素标签");
            }
        }
        if(showCreateLog) {
            LogUtils.createLog("外键合法性校验完成");
        }


        /*对持久化对象信息实体进行排序，排序规则为：
        1.无外键的优先于有外键
        2.两个有外键的，如果其中一个包含另一个，则包含者优先
        */
        Collections.sort(entities, new Comparator<ReadEntity>() {
            @Override
            public int compare(ReadEntity lhs, ReadEntity rhs) {
                if (lhs.foreignClass.size() == 0)
                    return -1;
                else if (rhs.foreignClass.size() == 0)
                    return 1;
                else if (lhs.foreignClass.contains(rhs.cls))
                    return 1;
                else if (rhs.foreignClass.contains(lhs.cls))
                    return -1;
                else return 0;
            }
        });

        /*建表并执行更新语句*/
        database.beginTransaction();

        if(showCreateLog) {
            LogUtils.createLog("执行建表与更新语句");
        }
        try {
            for (final ReadEntity entity : entities) {
                if(showCreateLog) {
                    LogUtils.createLog("执行实体对象："+entity.cls.getName());
                    LogUtils.createLog("执行的SQL建表语句："+entity.sql);
                }

                database.execSQL(entity.sql);

                for(UpdateEntity updateEntity : entity.updateEntityList)
                {
                    if(showCreateLog) {
                        LogUtils.createLog("执行更新对象："+updateEntity.getClass().getName());
                    }
                    updateEntity.doUpdate(database);
                }
            }


            /*如果所有SQL成功执行，提交事务*/
            if(showCreateLog) {
                LogUtils.createLog("执行建表与更新语句成功！");
            }
            database.setTransactionSuccessful();
        } catch (SQLException e)
        {
            throw new VLInitException(e.getMessage());
        }

        database.endTransaction();


        /*更新完成后刷新数据库版本，关闭数据库*/
        if(showCreateLog) {
            LogUtils.createLog("设置当前数据库版本：" + version);
            LogUtils.createLog("创建日志结束");
        }
        database.setVersion(version);
        database.close();
    }

}
