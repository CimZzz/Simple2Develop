package com.virtualightning.simple2develop.quickdb.entity;

import com.virtualightning.library.simple2develop.quickdb.core.register.PrimaryKey;

import java.util.Date;

/**
 * Created by CimZzz on 16/8/11.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.4<br>
 * Description:<br>
 * 用户实体类1
 */
public class User1 {
    @PrimaryKey(variable = false)
    private Integer id;
    private String name;
    private Double weight;
    private Boolean isBoy;
    private Date birth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getBoy() {
        return isBoy;
    }

    public void setBoy(Boolean boy) {
        isBoy = boy;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return String.format("id:%s\nname:%s\nweight:%s\nisBoy:%s\nbirth:%s\n",id,name,weight,isBoy,birth);
    }
}
