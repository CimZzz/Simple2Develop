package com.virtualightning.library.simple2develop.quickdb.core;

import com.virtualightning.library.simple2develop.quickdb.core.register.ForeignKey;
import com.virtualightning.library.simple2develop.quickdb.core.register.NotNull;
import com.virtualightning.library.simple2develop.quickdb.core.register.PrimaryKey;
import com.virtualightning.library.simple2develop.quickdb.exception.VLInitException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by CimZzz on 16/6/7.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Description:<br>
 * 持久化对象信息读取实体
 */
public class ReadEntity{
    /**
     * 持久化对象信息实体类名
     */
    String path;


    /**
     * 持久化对象信息实体类
     */
    Class cls;


    /**
     * 外键类列表
     */
    List<Class> foreignClass;


    /**
     * SQL 建表语句
     */
    String sql;


    /**
     * 包含的持久化对象更新信息实体列表
     */
    List<UpdateEntity> updateEntityList;


    /**
     * 构造方法，实例化外键类列表和持久化对象更新信息实体列表
     */
    ReadEntity()
    {
        updateEntityList = new ArrayList<>();
        foreignClass = new ArrayList<>();
    }



    /**
     * 解析持久化对象信息实体
     * @param checkMap 持久化实体验证表（用来判断实体是否存在其中）
     * @param updateMap Update对象享元表
     * @param foreignSQL 外键语句列表（暂时无用）
     * @param oldVersion 旧版本
     * @return 解析完成的实体
     */
    Entity analyzeEntity(Map<Class,Boolean> checkMap,Map<String,Update> updateMap,List<String> foreignSQL,int oldVersion)
    {
        /*获取持久化类的 Class 对象，将其置入校验表*/
        try {
            cls = Class.forName(path);
        } catch (ClassNotFoundException e) {
            throw new VLInitException("请确认 Entity 的类名路径正确");
        }
        /*如果校验表内已经存在该 Class 对象并且为 true，则 Entity 重复定义*/
        Boolean flag = checkMap.get(cls);
        if(flag != null && flag)
        {
            throw new VLInitException(cls.getName()+" Entity 重复定义");
        }
        checkMap.put(cls,true);

        /*旧版本如果不为0，那么就会有可能用到更新项*/
        if(oldVersion != 0) {
            /*删除更新版本小于旧版本的更新项，进行解析实体*/
            Iterator<UpdateEntity> iterator = updateEntityList.iterator();
            while (iterator.hasNext()) {
                UpdateEntity entity = iterator.next();
                if(entity.version <= oldVersion)
                {
                    iterator.remove();
                }
                else{
                    entity.analyzeEntity(updateMap);
                }
            }

            /*进行排序
            * 版本较大的排在版本较小的后面
            */
            Collections.sort(updateEntityList, new Comparator<UpdateEntity>() {
                @Override
                public int compare(UpdateEntity lhs, UpdateEntity rhs) {
                    if (lhs.version > rhs.version) {
                        return 1;
                    } else if (lhs.version < rhs.version) {
                        return -1;
                    } else {
                        throw new VLInitException("Entity 下有版本相同的 Update，Entity名为:" + cls.getName());
                    }
                }
            });
        }
        else {
            /*版本为0，删除所有持久化对象更新信息实体*/
            updateEntityList.clear();
        }

        /*创建持久化对象实体*/
        Entity entity = new Entity();

        /*通过反射解析实体获取 SQL 语句，生成表名*/
        String name = cls.getSimpleName().toLowerCase();
        String tableName = "tb_" + name;
        entity.tableName = tableName;

        StringBuilder createSQL = new StringBuilder();
        createSQL.append("create table if not exists ");
        createSQL.append(tableName);
        createSQL.append("(");

        String primaryKey = "";
        List<ForeignKeyPair> foreignKey = new ArrayList<>();

        /*遍历属性*/
        Field[] fields = cls.getDeclaredFields();
        for(Field field : fields)
        {
            /*如果 field 被 static 或 final 修饰，则不做操作*/
            int modifier = field.getModifiers();
            //noinspection IncompatibleBitwiseMaskOperation
            if(
                    (modifier & 8 )== 8
                            || (modifier & 16 )== 16
                    )
            {
                continue;
            }

            /*基本声明*/
            String fieldName = field.getName().toLowerCase();
            String typeName = "";

            /*获取属性类型*/
            Class<?> fieldCls = field.getType();

            /*设置属性可访问权限*/
            field.setAccessible(true);

            /*获取全部支持的注解类型*/
            PrimaryKey pK = field.getAnnotation(PrimaryKey.class);
            ForeignKey fK = field.getAnnotation(ForeignKey.class);
            NotNull notNull = field.getAnnotation(NotNull.class);

            /*属性冲突*/
            if( pK != null && notNull != null )
            {
                throw new VLInitException(cls.getName()+"中 PrimaryKey 注解不能和 NotNull 注解合用");
            }

            /*在判断外键之前先检查是否拥有主键注解，用于表示该属性作为该实体的主键类型*/
            boolean isPrimaryKey = pK != null;

            /*判断其是否拥有外键，如果外键合法，则其主键注解会作为该属性的主键注解，但是不会主动被当做主键*/
            if(fK != null)
            {
                Class foreignCls = fK.fieldSrc();

                Field foreignField = null;
                try {
                    foreignField = foreignCls.getDeclaredField(fK.fieldName());

                } catch (NoSuchFieldException e) {
                    throw new VLInitException(cls.getName()+"外键源内没有同名的属性");
                }

                /*判断外键类型与属性类型是否一致*/
                if(!field.getType().equals(foreignField.getType()))
                {
                    throw new VLInitException(cls.getName()+"外键属性类型不一致");
                }

                /*判断外键属性是否拥有主键注解*/
                pK = foreignField.getAnnotation(PrimaryKey.class);
                if(pK == null)
                {
                    throw new VLInitException(cls.getName()+"外键必须拥有主键注解");
                }

                /*外键合法加入外键列表*/
                ForeignKeyPair pair = new ForeignKeyPair();
                pair.keyName = field.getName();
                pair.srcName = name;
                pair.foreignKeySrcName = foreignCls.getSimpleName().toLowerCase();
                pair.foreignKeyName =foreignField.getName().toLowerCase();

                foreignKey.add(pair);
                foreignClass.add(foreignCls);

                /*如果在校对表中不存在外键，则添加外键并设置外键存在为false*/
                if(!checkMap.containsKey(foreignCls))
                    checkMap.put(foreignCls,false);
            }

            /*检测是否拥有 PrimaryKey 注解，则根据主键注解设置属性类型名，否则根据类型设置属性类型名*/
            if(pK != null)
            {
                if(fieldCls.equals(String.class))
                {
                    typeName += "varchar(";

                    typeName += pK.keyLengthRange()+")";
                }
                else if(fieldCls.equals(Integer.class))
                {
                    typeName += pK.variable() ? " auto_increment" : "";
                }
                else {
                    throw new VLInitException(cls.getName()+"主键属性或关联外键的属性类型只能为 String或Integer");
                }

                /*如果是主键的话加入主键集合*/
                if(isPrimaryKey)
                    primaryKey += " " + fieldName;
            }
            else {
                typeName =verfityType(fieldCls);
            }

            /*将属性 SQL 添加至 SQL 语句中*/
            createSQL.append(fieldName);
            createSQL.append(" ");
            createSQL.append(typeName);
            createSQL.append(" ");

            /*判断其额外属性*/
            if (pK != null || notNull != null)
            {
                createSQL.append("not null");
            }

            createSQL.append(",");

            /*添加属性到持久化实体内*/
            entity.properties.put(fieldName,field);
        }

        /*判断主键是否为空*/
        if(primaryKey.equals(""))
        {
            throw new VLInitException(cls.getName()+"至少需要一个主键");
        }

        /*最后添加外键和主键*/
        for(ForeignKeyPair pair : foreignKey)
        {
            createSQL.append(pair.getForeignKeyPair());
            createSQL.append(" on delete cascade on update cascade");
            createSQL.append(",");
        }
        createSQL.append("primary key (");
        createSQL.append(primaryKey.trim().replace(" ",","));
        createSQL.append("))");

        sql = createSQL.toString();

        /*最后返回实体*/

        return entity;
    }


    /**
     * 验证持久化对象中属性的类型是否为合法类型，如果是则返回对应的类型字段
     * @param cls 持久化对象中属性的类型
     * @return 类型的字段
     */
    String verfityType(Class<?> cls)
    {
        if(cls.equals(String.class))
        {
            return "text";
        }
        else if(cls.equals(Integer.class))
        {
            return "integer";
        }
        else if(cls.equals(Boolean.class))
        {
            return "boolean";
        }
        else if(cls.equals(Double.class))
        {
            return "real";
        }
        else if(cls.equals(Date.class))
        {
            return "timestamp";
        }
        else{
            throw new VLInitException(cls.getName()+" 中的属性类型必须是合法类型，支持的类型有：String，Integer，Boolean，Double，Date");
        }
    }


    /**
     * 外键对（暂时无用）
     */
    private class ForeignKeyPair{
        /**
         * 键名
         */
        private String keyName;


        /**
         * 外键名
         */
        private String foreignKeyName;


        /**
         * 源名
         */
        private String srcName;


        /**
         * 外键源名
         */
        private String foreignKeySrcName;


        /**
         * 获得外键修饰语句
         * @return 外键修饰语句
         */
        String getForeignKeyPair()
        {
            StringBuilder builder = new StringBuilder();

            builder.append("foreign key (");
            builder.append(keyName);
            builder.append(") references ");
            builder.append("tb_");
            builder.append(foreignKeySrcName);
            builder.append("(");
            builder.append(foreignKeyName);
            builder.append(")");

            return builder.toString();
        }





        /**
         * 生成外键触发器语句
         * @return 外键触发器语句
         */
        String getForeignKeyTrigger()
        {
            StringBuilder builder = new StringBuilder();

            builder.append("create trigger fk_")
                    .append(srcName)
                    .append("_")
                    .append(foreignKeySrcName)
                    .append(" before insert on tb_")
                    .append(srcName)
                    .append(" for each row begin")
                    .append(" select case when ((select ")
                    .append(foreignKeyName)
                    .append(" from tb_")
                    .append(foreignKeySrcName)
                    .append(" where ")
                    .append(foreignKeyName)
                    .append("=new.")
                    .append(keyName)
                    .append(") is NULL )")
                    .append(" THEN RAISE (ABORT,'Foreign Key Violation') END;");

            return builder.toString();
        }
    }
}
