package com.virtualightning.library.simple2develop.quickdb.core.command;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.core.DBHelper;
import com.virtualightning.library.simple2develop.quickdb.util.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 用于查询的 SQL 命令，继承自 SQLCommand<br>
 * <br>相关链接：<br>
 * {@link SQLCommand}<br>
 */
public class QuerySQLCommand<T> extends SQLCommand {
    /**
     * 属性表
     */
    private Map<String,Field> fieldMap;


    /**
     * 持久化对象类型
     */
    private Class<T> t;


    /**
     * 保存持久化对象类型的列表
     */
    private List<T> list;


    /**
     * 构造方法，接受属性表和持久化对象类型<br>
     * <br>相关链接：<br>
     * {@link #fieldMap}<br>
     * {@link #t}<br>
     * @param fieldMap 属性表
     * @param t 持久化对象类型
     */
    public QuerySQLCommand(Map<String,Field> fieldMap,Class<T> t)
    {
        this.fieldMap = fieldMap;
        this.t = t;
    }


    /**
     * 如果 SQL 执行成功，获得持久化对象类型的列表<br>
     * <br>相关链接：<br>
     * {@link #list}<br>
     * @return 持久化对象类型的列表
     */
    public List<T> getList()
    {
        return isSuccess() ? list : null;
    }

    /**
     * 根据表名和条件语句生成 SQL 语句<br>
     * <br>相关链接：<br>
     * {@link #tbName}<br>
     * {@link #condition}<br>
     * {@link #sql}<br>
     */
    @Override
    public void genSql() {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from ")
                .append(tbName)
                .append(" ")
                .append(condition != null ? condition : "");

        sql = sqlBuilder.toString();

        LogUtils.sqlLog(sql);
    }


    /**
     * 执行 SQL 语句
     * @param database 数据库对象
     */
    @Override
    public void execute(SQLiteDatabase database) {
        list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, args);

        int cursorCount = cursor.getColumnCount();

        /*生成赋值方法列表，根据结果行字段类型对应位置生成赋值方法*/
        List<GetMethod> methods = new ArrayList<>();
        for(int i = 0 ; i < cursorCount ; i ++)
        {
            Field field = fieldMap.get(cursor.getColumnName(i));

            Class cls = field.getType();

            if(cls.equals(String.class))
            {
                methods.add(new GetStringMethod(i,field));
            }
            else if(cls.equals(Integer.class))
            {
                methods.add(new GetIntegerMethod(i,field));
            }
            else if(cls.equals(Boolean.class))
            {
                methods.add(new GetBooleanMethod(i,field));
            }
            else if(cls.equals(Double.class))
            {
                methods.add(new GetDoubleMethod(i,field));
            }
            else if(cls.equals(Date.class))
            {
                methods.add(new GetDateMethod(i,field));
            }
        }

        try {
            while (cursor.moveToNext()) {
                T object = (T) t.newInstance();

                for (GetMethod method : methods)
                {
                    method.setFieldValue(object,cursor);
                }

                list.add(object);
            }

            cursor.close();
        } catch (InstantiationException | IllegalAccessException e) {
            LogUtils.errorLog(e.getMessage());
            setSuccess(false);
        }

    }


    /**
     * 赋值方法的基类
     */
    private abstract class GetMethod
    {
        /**
         * 属性值对应结果行的列下标
         */
        protected int fieldIndex;

        /**
         * 属性对象
         */
        protected Field field;


        /**
         * 接受属性值对应结果行的列下标和属性对象
         * @param fieldIndex 属性值对应结果行的列下标
         * @param field 属性对象
         */
        public GetMethod(int fieldIndex,Field field)
        {
            this.fieldIndex = fieldIndex;
            this.field = field;
        }

        /**
         * 抽象方法，赋给持久化对象对应属性的属性值
         * @param object 持久化对象
         * @param cursor 游标（结果行）
         * @throws IllegalAccessException 权限异常
         */
        public abstract void setFieldValue(Object object,Cursor cursor) throws IllegalAccessException;
    }

    /**
     * 赋值整数的方法，继承自 GetMethod<br>
     * <br>相关链接：<br>
     * {@link GetMethod}
     */
    private class GetIntegerMethod extends GetMethod{

        public GetIntegerMethod(int fieldIndex, Field field) {
            super(fieldIndex, field);
        }

        @Override
        public void setFieldValue(Object object,Cursor cursor) throws IllegalAccessException {
            Integer integer = cursor.getInt(fieldIndex);
            field.set(object,integer);
        }
    }

    /**
     * 赋值浮点数的方法，继承自 GetMethod<br>
     * <br>相关链接：<br>
     * {@link GetMethod}
     */
    private class GetDoubleMethod extends GetMethod{

        public GetDoubleMethod(int fieldIndex, Field field) {
            super(fieldIndex, field);
        }

        @Override
        public void setFieldValue(Object object,Cursor cursor) throws IllegalAccessException {
            Double aDouble = cursor.getDouble(fieldIndex);
            field.set(object,aDouble);
        }
    }

    private class GetBooleanMethod extends GetMethod{

        public GetBooleanMethod(int fieldIndex, Field field) {
            super(fieldIndex, field);
        }

        @Override
        public void setFieldValue(Object object,Cursor cursor) throws IllegalAccessException {
            Integer aBoolean  = cursor.getInt(fieldIndex);
            field.set(object,aBoolean == 1);
        }
    }

    private class GetStringMethod extends GetMethod{

        public GetStringMethod(int fieldIndex, Field field) {
            super(fieldIndex, field);
        }

        @Override
        public void setFieldValue(Object object,Cursor cursor) throws IllegalAccessException {
            String string  = cursor.getString(fieldIndex);
            field.set(object,string);
        }
    }

    /**
     * 赋值日期的方法，继承自 GetMethod<br>
     * <br>相关链接：<br>
     * {@link GetMethod}
     */
    private class GetDateMethod extends GetMethod{

        public GetDateMethod(int fieldIndex, Field field) {
            super(fieldIndex, field);
        }

        @Override
        public void setFieldValue(Object object,Cursor cursor) throws IllegalAccessException {
            String string  = cursor.getString(fieldIndex);
            field.set(object, string != null ? DBHelper.genDate(string) : null);
        }
    }
}
