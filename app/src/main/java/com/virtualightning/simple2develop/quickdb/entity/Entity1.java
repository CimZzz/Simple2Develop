package com.virtualightning.simple2develop.quickdb.entity;

import com.virtualightning.library.simple2develop.quickdb.core.register.PrimaryKey;

/**
 * Created by CimZzz on 16/8/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.4<br>
 * Description:<br>
 * 实体类1
 */
public class Entity1 {
    @PrimaryKey
    private Integer id1;
    @PrimaryKey
    private Integer id2;

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public Integer getId2() {
        return id2;
    }

    public void setId2(Integer id2) {
        this.id2 = id2;
    }

    @Override
    public String toString() {
        return String.format("id1:%s\nid2:%s\n",id1,id2);
    }
}
