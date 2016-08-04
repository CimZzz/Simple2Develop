package com.virtualightning.library.simple2develop.quickdb.core;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CimZzz on 16/6/10.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : <br>
 * Description:<br>
 * 更新方法对象接口，所有更新方法对象必须实现此接口
 */
public interface Update {
    void update(SQLiteDatabase database, int version);
}
