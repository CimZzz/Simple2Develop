package com.virtualightning.library.simple2develop.quickdb.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.virtualightning.library.simple2develop.quickdb.exception.VLSQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by CimZzz on 16/6/8.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 数据库辅助类<br>
 */
public class DBHelper extends SQLiteOpenHelper {
    /**
     * 辨识数据库版本
     */
    static int version;

    /**
     * 数据库名
     */
    static String dbName;


    /**
     * 构造函数，默认实现
     * @param context
     */
    DBHelper(Context context) {
        super(context, dbName, null, version);
    }


    /**
     * 默认实现
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    /**
     * 默认实现
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 根据一个数生成通配符字符串
     * @param n 通配符数
     * @return 通配符字符串
     */
    public static String genWildcards(int n)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0 ; i < n ; i ++)
        {
            if(i != 0)
            {
                stringBuilder.append(",");
            }
            stringBuilder.append("?");
        }
        return stringBuilder.toString();
    }





    /**
     * 根据属性名迭代器生成一个属性名字符串
     * @param names 属性名迭代器
     * @return 属性名字符串
     */
    public static String genTBP(Iterable<String> names)
    {
        StringBuilder stringBuilder = new StringBuilder();

        Iterator<String> iterator = names.iterator();
        while(iterator.hasNext())
        {
            stringBuilder.append(iterator.next());

            if(iterator.hasNext())
            {
                stringBuilder.append(",");
            }
        }

        return  stringBuilder.toString();
    }





    /**
     * 根据日期生成 Timestamp 格式的字符串
     * @param date 日期
     * @return Timestamp 格式的字符串
     */
    public static String genDateValue(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(date);
    }


    /**
     * 根据 Timestamp 格式的字符串生成日期
     * @param dateValue Timestamp 格式的字符串
     * @return 日期
     */
    public static Date genDate(String dateValue)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return format.parse(dateValue);
        } catch (ParseException e) {
            throw new VLSQLException("日期值转换错误");
        }
    }
}
