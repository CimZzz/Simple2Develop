package com.virtualightning.library.simple2develop.quickdb.exception;

/**
 * Created by CimZzz on 16/6/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * Virtual-Lightning 没有找到实体异常
 */
public class VLEntityNotFoundException extends RuntimeException {
    public VLEntityNotFoundException(Class cls)
    {
        super(cls.getName()+" 该实体没有找到，请在 XML 文件中定义");
    }
}
