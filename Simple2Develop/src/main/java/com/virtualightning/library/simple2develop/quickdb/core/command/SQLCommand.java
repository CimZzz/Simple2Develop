package com.virtualightning.library.simple2develop.quickdb.core.command;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * SQL 命令的基类
 */
public abstract class SQLCommand {
    /**
     * 生成的 SQL 语句
     */
    protected String sql;


    /**
     * 表名
     */
    protected String tbName;


    /**
     * 条件语句
     */
    protected String condition;

    /**
     * 条件参数
     */
    protected String[] args;

    /**
     * 是否执行成功
     */
    private boolean success;


    /**
     * 构造方法，初始执行结果为成功<br>
     * <br>相关链接：<br>
     * {@link #success}<br>
     */
    public SQLCommand()
    {
        success = true;
    }


    /**
     * 设置执行结果
     * <br>相关链接：<br>
     * {@link #success}<br>
     * @param success 执行结果
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }


    /**
     * 设置表名
     * <br>相关链接：<br>
     * {@link #tbName}<br>
     * @param tbName 表名
     */
    public void setTbName(String tbName)
    {
        this.tbName = tbName;
    }


    /**
     * 设置条件语句和条件参数<br>
     * <br>相关链接：<br>
     * {@link #condition}<br>
     * {@link #args}<br>
     * @param condition 条件语句
     * @param args 条件参数
     */
    public void setCondition(String condition,String... args)
    {
        this.condition = condition;
        this.args = args;
    }


    /**
     * 返回执行结果
     * @return 执行结果
     */
    public boolean isSuccess()
    {
        return success;
    }


    /**
     * 抽象方法，生成 SQL 语句
     */
    public abstract void genSql();


    /**
     * 抽象方法，执行 SQL 语句
     * @param database 数据库对象
     */
    public abstract void execute(SQLiteDatabase database);

}
