package com.virtualightning.library.simple2develop.quickdb.core.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by CimZzz on 16/6/9.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 限定属性为主键注解，主键类型只能为整数或字符串
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
    /**
     * ＊字符串有效<br>
     * 限定字符串主键的长度，缺省长度为10<br>
     * @return 字符串主键长度
     */
    int keyLengthRange() default 10;

    /**
     * ＊整数有效<br>
     * 限定整数主键是否为自增长，缺省为true<br>
     * @return 字符串主键长度是否可变或整数主键是否为自增长
     */
    boolean variable() default true;
}
