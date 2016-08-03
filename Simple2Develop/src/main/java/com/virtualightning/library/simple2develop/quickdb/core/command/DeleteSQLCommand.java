package com.virtualightning.library.simple2develop.quickdb.core.command;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.virtualightning.library.simple2develop.quickdb.util.LogUtils;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 用于删除的 SQL 命令，继承自 SQLCommand<br>
 * <br>相关链接：<br>
 * {@link SQLCommand}<br>
 */
public class DeleteSQLCommand extends SQLCommand{

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
        sqlBuilder.append("delete from ")
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
        try {
            if(args != null)
                database.execSQL(sql, args);
            else database.execSQL(sql);
        } catch (SQLException e)
        {
            LogUtils.errorLog(e.getMessage());
            setSuccess(false);
        }
    }
}
