package com.virtualightning.library.simple2develop.quickdb.core.command;

import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.core.DBHelper;
import com.virtualightning.library.simple2develop.quickdb.util.LogUtils;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 用于保存或更新的 SQL 命令，继承自 PropertySQLCommand<br>
 * <br>相关链接：<br>
 * {@link PropertySQLCommand}
 */
public class ReplaceSQLCommand extends PropertySQLCommand{

    /**
     * 根据表名和属性名列表生成 SQL 语句<br>
     * <br>相关链接：<br>
     * {@link #tbName}<br>
     * {@link #propertyNames}<br>
     * {@link #sql}<br>
     */
    @Override
    public void genSql() {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("replace into ")
                .append(tbName)
                .append("(")
                .append(DBHelper.genTBP(propertyNames))
                .append(")")
                .append(" values(")
                .append(DBHelper.genWildcards(propertyNames.size()))
                .append(")");

        sql = sqlBuilder.toString();

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
