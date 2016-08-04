package com.virtualightning.library.simple2develop.quickdb.core.command;

import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.util.LogUtils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 用于修改更新的 SQL 命令，继承自 PropertySQLCommand<br>
 * <br>相关链接：<br>
 * {@link PropertySQLCommand}
 */
public class UpdateSQLCommand extends PropertySQLCommand{
    /**
     * 是否允许设置空值
     */
    private boolean allowNull;

    /**
     * 设置是否允许设置空值
     * @param allowNull 是否允许设置空值
     */
    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }


    /**
     * 根据表名、属性名列表和属性值列表生成 SQL 语句<br>
     * <br>相关链接：<br>
     * {@link #tbName}<br>
     * {@link #allowNull}<br>
     * {@link #propertyNames}<br>
     * {@link #propertyValues}<br>
     * {@link #sql}<br>
     */
    @Override
    public void genSql() {
        if(!allowNull) {
            int nullIndex;
            while ((nullIndex = propertyValues.indexOf(null)) != -1) {
                propertyValues.remove(nullIndex);
                propertyNames.remove(nullIndex);
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("update ")
                .append(tbName)
                .append(" set ");

        Iterator<String> iterator = propertyNames.iterator();
        while(iterator.hasNext())
        {
            sqlBuilder.append(iterator.next()).append("=? ");
            if(iterator.hasNext())
                sqlBuilder.append(",");
        }

        sqlBuilder.append(condition != null ? condition : "");

        sql = sqlBuilder.toString();

        if(args != null)
        {
            propertyValues.addAll(Arrays.asList(args));
        }

        LogUtils.sqlLog(sql);
    }


    /**
     * 执行 SQL 语句
     * @param database 数据库对象
     */
    @Override
    public void execute(SQLiteDatabase database) {
        database.execSQL(sql,getValueArray());
    }
}
