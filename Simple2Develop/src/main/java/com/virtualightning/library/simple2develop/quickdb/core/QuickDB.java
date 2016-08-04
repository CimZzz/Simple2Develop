package com.virtualightning.library.simple2develop.quickdb.core;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.core.command.DeleteSQLCommand;
import com.virtualightning.library.simple2develop.quickdb.core.command.QuerySQLCommand;
import com.virtualightning.library.simple2develop.quickdb.core.command.ReplaceSQLCommand;
import com.virtualightning.library.simple2develop.quickdb.core.command.UpdateSQLCommand;
import com.virtualightning.library.simple2develop.quickdb.exception.VLEntityNotFoundException;
import com.virtualightning.library.simple2develop.quickdb.util.LogUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by CimZzz on 16/6/7.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 单例类，作为QuickDB的启动配置项
 */
public class QuickDB {

    /**
     * 单例实例
     */
    private static QuickDB instance;




    /**
     * 同步锁对象
     */
    private static final Object locker = new Object();




    /**
     * 上下文
     */
    private Context context;




    /**
     * 持久化对象实体表
     */
    private Map<Class,Entity> entityMap;





    /**
     * 构造函数，保留传递来的配置信息<br>
     *
     * <br>相关链接：<br>
     * {@link QuickDBConfig}<br>
     * {@link DBHelper}<br>
     *
     * @param config 配置信息
     */
    private QuickDB(QuickDBConfig config)
    {
        /*为 DBHelper 设置数据库信息*/
        DBHelper.dbName = config.dbName;
        DBHelper.version = config.version;

        /*获取持久化对象实体表*/
        entityMap = config.entityMap;
    }







    /**
     * 对 QuickDB 进行初始化，创建配置信息并调用 QuickDBConfig.initQuickDBConfig(Context, int)
     * 方法对其进行初始化
     *
     * {@link QuickDBConfig}
     * {@link QuickDBConfig#initQuickDBConfig(Context, int)}
     *
     * @param context 上下文
     * @param xmlId xmlId(必须要在 res/raw 文件夹下)
     */
    public static void initQuickDB(Context context,int xmlId)
    {
        synchronized (locker) {
            /*创建配置信息并根据 xml 文件初始化数据库*/
            QuickDBConfig config = new QuickDBConfig();
            config.initQuickDBConfig(context, xmlId);

            /*创建 QuickDB 的实例作为单例，传递配置信息作为参数*/
            instance = new QuickDB(config);
            instance.context = context;


            config = null;


            /*清除配置信息所占用的内存*/
            Runtime.getRuntime().gc();
        }
    }





    /**
     * 单例方法，获得 QuickDB 的实例，在调用前必须先调用 initQuickDB(Context,int) 方法进行
     * 初始化
     *
     * {@link #initQuickDB(Context, int)}
     *
     * @return QuickDB实例
     */
    public static QuickDB getInstance()
    {
        return instance;
    }

    private Entity getEntity(Class cls)
    {
        Entity entity = entityMap.get(cls);
        if(entity == null)
        {
            throw new VLEntityNotFoundException(cls);
        }

        return entity;
    }

    /**
     * 保存对象，如果对象存在则更新对象（执行 ReplaceCommand）<br>
     * <br>相关链接：<br>
     * {@link ReplaceSQLCommand}<br>
     * @param a 要保存的持久化对象
     * @param <T> 泛型参数
     * @return 执行结果
     */
    public <T> boolean saveOrUpdate(T a)
    {
        synchronized (locker)
        {
            /*从持久化实体中取出对应实体*/
            Entity entity = getEntity(a.getClass());

            /*创建 DBHelper 对象，作为数据库操作对象*/
            DBHelper helper = new DBHelper(context);

            /*获得读写数据库，并开始事务*/
            SQLiteDatabase database = helper.getWritableDatabase();
            database.beginTransaction();

            /*获得持久化对象属性集*/
            Collection<Field> fields = entity.getFieldList((String[])null);

            /*创建 SQL 可执行命令 ReplaceCommand，设置表名与属性名（TBP），生成 SQL 语句*/
            ReplaceSQLCommand command = new ReplaceSQLCommand();
            command.setTbName(entity.tableName);
            command.addPropertyNames(entity.getAllTBP());
            command.genSql();

            try {
                /*根据属性集获取值*/
                for(Field field : fields)
                {
                    command.addPropertyValue(field.get(a));
                }

                /*执行 SQL 命令*/
                command.execute(database);

                /*如果成功提交事务*/
                database.setTransactionSuccessful();
            } catch (SQLException | IllegalAccessException e) {
                /*执行失败，设置命令失败并发送错误日志*/
                command.setSuccess(false);
                LogUtils.errorLog(e.getMessage());
            }

            /*结束事务，关闭 DBHelper*/
            database.endTransaction();
            helper.close();

            /*返回是否 SQL 是否执行成功*/
            return command.isSuccess();
        }
    }






    /**
     * 保存对象集合，如果其中对象存在则更新对象（执行 ReplaceCommand）<br>
     * <br>相关链接：<br>
     * {@link ReplaceSQLCommand}<br>
     * @param as 要保存的持久化对象列表
     * @param <T> 泛型参数
     * @return 执行结果
     */
    public <T> boolean saveOrUpdate(List<T> as)
    {
        synchronized (locker)
        {
            /*如果持久化对象列表中没有对象时，直接返回 true*/
            if(as.size() == 0)
            {
                return true;
            }

            /*从持久化实体中取出对应实体*/
            Entity entity = getEntity(as.get(0).getClass());

            /*创建 DBHelper 对象，作为数据库操作对象*/
            DBHelper helper = new DBHelper(context);

            /*获得读写数据库，并开始事务*/
            SQLiteDatabase database = helper.getWritableDatabase();
            database.beginTransaction();

            /*获得持久化对象属性集*/
            Collection<Field> fields = entity.getFieldList((String[])null);

            /*创建 SQL 可执行命令 ReplaceCommand，设置表名与属性名（TBP），生成 SQL 语句*/
            ReplaceSQLCommand command = new ReplaceSQLCommand();
            command.setTbName(entity.tableName);
            command.addPropertyNames(entity.getAllTBP());
            command.genSql();

            try {
                /*循环列表执行*/
                for(T a : as)
                {
                    /*根据属性集获取值*/
                    for(Field field : fields)
                    {
                        command.addPropertyValue(field.get(a));
                    }

                    /*执行 SQL 命令*/
                    command.execute(database);

                    /*执行完成清空 SQL 命令属性值集合*/
                    command.clearPropertyValues();
                }

                /*如果成功提交事务*/
                database.setTransactionSuccessful();
            } catch (SQLException | IllegalAccessException e) {
                /*执行失败，设置命令失败并发送错误日志*/
                command.setSuccess(false);
                LogUtils.errorLog(e.getMessage());
            }

            /*结束事务，关闭 DBHelper*/
            database.endTransaction();
            helper.close();

            /*返回是否 SQL 是否执行成功*/
            return command.isSuccess();
        }
    }






    /**
     * 调用 query(Class, String, String...) 的默认实现（没有查询条件），获得持久化对象列表<br>
     * <br>相关链接：<br>
     * {@link #query(Class, String, String...)}<br>
     * @param cls 持久化对象类
     * @param <T> 泛型参数
     * @return 持久化对象列表
     */
    public <T> List<T> query(Class<T> cls)
    {
        return query(cls,null,(String[])null);
    }







    /**
     * 根据条件查询表，获得持久化对象列表（执行 QuerySQLCommand）<br>
     * <br>相关链接：<br>
     * {@link QuerySQLCommand}<br>
     * @param cls 持久化对象类
     * @param condition 条件语句
     * @param args 条件参数
     * @param <T> 泛型参数
     * @return 持久化对象列表
     */
    public <T> List<T> query(Class<T> cls,String condition,String... args)
    {

        synchronized (locker) {
            /*从持久化实体中取出对应实体*/
            Entity entity = getEntity(cls);

            /*创建 DBHelper 对象，作为数据库操作对象*/
            DBHelper helper = new DBHelper(context);

            /*获得只读数据库*/
            SQLiteDatabase database = helper.getReadableDatabase();

            /*创建 SQL 可执行命令 QuerySQLCommand，设置表名、条件与条件参数，生成 SQL 语句*/
            QuerySQLCommand<T> command = new QuerySQLCommand<>(entity.properties, cls);
            command.setCondition(condition, args);
            command.setTbName(entity.tableName);
            command.genSql();

            /*执行 SQL 命令*/
            command.execute(database);

            /*关闭 DBHelper，返回查询结果列表*/
            helper.close();
            return command.getList();
        }
    }






    /**
     * 调用 update(Object, boolean, String, String...)} 的默认实现（没有更新条件，即全部更新），返回更新结果<br>
     * <br>相关链接：<br>
     * {@link #update(Object, boolean, String, String...)}<br>
     * @param a 持久化对象（作为更新的数据）
     * @param allowNull 是否允许更新为 null 值
     * @return 执行结果
     */
    public <T> boolean update(T a,boolean allowNull)
    {
        return update(a,allowNull,null,(String[])null);
    }






    /**
     * 根据条件更新表，根据传递的持久化对象更新表内数据（执行 UpdateSQLCommand）<br>
     * <br>相关链接：<br>
     * {@link UpdateSQLCommand}<br>
     * @param a 持久化对象（作为更新的数据）
     * @param allowNull 是否允许更新为 null 值
     * @param condition 条件语句
     * @param args 条件参数
     * @param <T> 泛型参数
     * @return 执行结果
     */
    public <T> boolean update(T a,boolean allowNull,String condition,String... args) {
        synchronized (locker) {
            /*从持久化实体中取出对应实体*/
            Entity entity = getEntity(a.getClass());

            /*创建 DBHelper 对象，作为数据库操作对象*/
            DBHelper helper = new DBHelper(context);

            /*获得读写数据库，并开始事务*/
            SQLiteDatabase database = helper.getWritableDatabase();
            database.beginTransaction();

            /*获得持久化对象属性集*/
            Collection<Field> fields = entity.getFieldList((String[])null);

            /*创建 SQL 可执行命令 UpdateSQLCommand，设置更新限制、表名、条件、条件参数与属性名（TBP）*/
            UpdateSQLCommand command = new UpdateSQLCommand();
            command.setAllowNull(allowNull);
            command.setTbName(entity.tableName);
            command.setCondition(condition, args);
            command.addPropertyNames(entity.getAllTBP());

            try {
                /*根据属性集获取值*/
                for (Field field : fields) {
                    command.addPropertyValue(field.get(a));
                }

                /*生成 SQL 语句*/
                command.genSql();


                /*执行 SQL 命令*/
                command.execute(database);


                /*如果成功提交事务*/
                database.setTransactionSuccessful();
            } catch (SQLException | IllegalAccessException e) {
                /*执行失败，设置命令失败并发送错误日志*/
                command.setSuccess(false);
                LogUtils.errorLog(e.getMessage());
            }

            /*结束事务，关闭 DBHelper*/
            database.endTransaction();
            helper.close();

            /*返回是否 SQL 是否执行成功*/
            return command.isSuccess();
        }
    }







    /**
     * 调用 delete(Class, String, String...) 的默认实现（没有删除条件，即全部删除），返回更新结果<br>
     * <br>相关链接：<br>
     * {@link #delete(Class, String, String...)} <br>
     * @param cls 持久化对象类
     * @param <T> 泛型参数
     * @return 执行结果
     */
    public <T> boolean deleteAll(Class<T> cls)
    {
        return delete(cls,null,(String[])null);
    }

    /**
     * 根据条件删除表数据（执行 DeleteSQLCommand）<br>
     * <br>相关链接：<br>
     * {@link DeleteSQLCommand}<br>
     * @param cls 持久化对象类
     * @param condition 条件语句
     * @param args 条件参数
     * @param <T> 泛型参数
     * @return 执行结果
     */
    public <T> boolean delete(Class<T> cls,String condition,String... args)
    {
        synchronized (locker) {
            /*从持久化实体中取出对应实体*/
            Entity entity = getEntity(cls);

            /*创建 DBHelper 对象，作为数据库操作对象*/
            DBHelper helper = new DBHelper(context);

            /*获得读写数据库，并开始事务*/
            SQLiteDatabase database = helper.getWritableDatabase();
            database.beginTransaction();

            /*创建 SQL 可执行命令 DeleteSQLCommand，设置表名、条件、条件参数，生成 SQL 语句*/
            DeleteSQLCommand command = new DeleteSQLCommand();
            command.setTbName(entity.tableName);
            command.setCondition(condition, args);
            command.genSql();

            /*执行 SQL 命令*/
            command.execute(database);

            /*获取命令执行结果，判断执行结果*/
            boolean success = command.isSuccess();
            if(success)
            {
                /*如果成功提交事务*/
                database.setTransactionSuccessful();
            }

            /*结束事务，关闭 DBHelper*/
            database.endTransaction();
            helper.close();

            /*返回是否 SQL 是否执行成功*/
            return success;
        }
    }






}
