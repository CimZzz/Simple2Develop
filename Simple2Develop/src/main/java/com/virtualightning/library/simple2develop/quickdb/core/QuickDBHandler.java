package com.virtualightning.library.simple2develop.quickdb.core;

import com.virtualightning.library.simple2develop.quickdb.exception.VLInitException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by CimZzz on 16/6/8.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.1.0<br>
 * Modify : VLSimple2Develop_0.1.2 修改 AnalyzerHandler 类名为 QuickDBHanlder 增强语意，删除Schema检测<br>
 * Description:<br>
 * 配置文件解析控制器<br>
 * 基于 SAX 对 xml 文件进行解析，并将解析后的数据保存至配置信息中<br>
 * <br>相关链接：<br>
 * {@link QuickDBConfig}
 */
@SuppressWarnings("unused")
public class QuickDBHandler extends DefaultHandler {



    /**
     * Virtual-Lightning 标签名
     */
    private static final String TAG_QUICKDB = "QuickDB";




    /**
     * 配置信息 标签名
     */
    private static final String TAG_CONFIG = "Config";




    /**
     * 持久化对象信息实体 标签名
     */
    private static final String TAG_ENTITY = "Entity";




    /**
     * 持久化对象信息更新实体 标签名
     */
    private static final String TAG_UPDATE = "Update";





    /**
     * 引用schema 属性名
     */
    private static final String ATTR_XSI_SCHEMALOCATION = "xsi:schemaLocation";





    /**
     * 数据库版本号 属性名
     */
    private static final String ATTR_VERSION = "version";





    /**
     * 数据库名 属性名
     */
    private static final String ATTR_DBNAME = "dbname";




    /**
     * 持久化对象信息实体类名 标签名
     */
    private static final String ATTR_NAME = "name";





    /**
     * 版本更新处理类名 属性名
     */
    private static final String ATTR_PROCESS = "process";





    /**
     * 显示创建过程日志 属性名
     */
    private static final String ATTR_SHOW_CREATE_LOG = "showCreateLog";




    /**
     * 装填的配置信息实例
     */
    private QuickDBConfig config;




    /**
     * 持久化对象信息实体引用
     */
    private ReadEntity readEntity;




    /**
     * 持久化对象信息更新实体引用
     */
    private UpdateEntity updateEntity;



    /**
     * 根节点标识
     */
    private boolean isRoot;






    /**
     * 构造函数，需要传递配置信息<br>
     * <br>相关链接：<br>
     * {@link QuickDBConfig}<br>
     *
     * @param config 配置信息
     */
    public QuickDBHandler(QuickDBConfig config)
    {
        isRoot = true;
        this.config = config;
    }





    /**
     * 分析元素开始，根据不同的元素标签有不同的动作<br>
     *
     * <br>相关链接：<br>
     * {@link #TAG_CONFIG}元素标签获取两个属性值：<br>
     * <ul>
     *     <li>
     *         {@link #ATTR_DBNAME} 数据库名称
     *     </li>
     *     <li>
     *         {@link #ATTR_VERSION} 数据库版本
     *     </li>
     *     <li>
     *         {@link #ATTR_SHOW_CREATE_LOG} 显示创建日志
     *     </li>
     * </ul>
     * {@link #TAG_ENTITY}元素标签获取一个属性值：<br>
     * <ul>
     *     <li>
     *         {@link #ATTR_NAME} 持久化对象类名
     *     </li>
     * </ul>
     * {@link #TAG_UPDATE}元素标签是{@link #TAG_ENTITY}的内部元素，获取两个属性：<br>
     * <ul>
     *     <li>
     *         {@link #ATTR_VERSION} 数据库版本
     *     </li>
     *     <li>
     *         {@link #ATTR_PROCESS} 处理方法类名
     *     </li>
     * </ul>
     * {@link ReadEntity} 持久化对象信息实体<br>
     * {@link UpdateEntity} 持久化对象信息更新实体<br>
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            switch (qName) {
                case TAG_QUICKDB:
                    if(!isRoot)
                    {
                        throw new VLInitException("根结点必须为 \"Virtual-Lightning\"");
                    }

                    String schemaLocation = attributes.getValue(ATTR_XSI_SCHEMALOCATION);

                    isRoot = false;
                    break;
                case TAG_CONFIG:
                    if(isRoot)
                    {
                        throw new VLInitException("根结点必须为 \"Virtual-Lightning\"");
                    }

                    config.version = Integer.parseInt(attributes.getValue(ATTR_VERSION));
                    config.dbName = attributes.getValue(ATTR_DBNAME);

                    String showCreateLog = attributes.getValue(ATTR_SHOW_CREATE_LOG);

                    if(config.dbName == null)
                    {
                        throw new VLInitException("Config标签中\"dbName\"属性定义不能为空");
                    }

                    if(showCreateLog != null)
                    {
                        config.showCreateLog = Boolean.parseBoolean(showCreateLog);
                    }

                    break;

                case TAG_ENTITY:
                    if(isRoot)
                    {
                        throw new VLInitException("根结点必须为 \"Virtual-Lightning\"");
                    }
                    readEntity = new ReadEntity();

                    readEntity.path = attributes.getValue(ATTR_NAME);


                    if(readEntity.path == null)
                    {
                        throw new VLInitException("Entity标签中\"name\"属性定义不能为空");
                    }

                    break;

                case TAG_UPDATE:
                    if(isRoot)
                    {
                        throw new VLInitException("根结点必须为 \"Virtual-Lightning\"");
                    }
                    updateEntity = new UpdateEntity();

                    updateEntity.version = Integer.parseInt(attributes.getValue(ATTR_VERSION));
                    updateEntity.process = attributes.getValue(ATTR_PROCESS);

                    if(updateEntity.version > config.version)
                    {
                        throw new VLInitException("Update标签中\"version\"属性定义有误，该版本号不能大于数据库版本号");
                    }

                    if(updateEntity.process == null)
                    {
                        throw new VLInitException("Update标签中\"process\"属性不能为空");
                    }


                    break;

                default:
                    if(isRoot)
                    {
                        throw new VLInitException("根结点必须为 \"QuickDB\"");
                    }
                    else throw new VLInitException("未知的结点 \""+qName+"\"");
            }
        } catch (NumberFormatException e)
        {
            throw new VLInitException("版本号设置不合法，必须为整数");
        }
    }


    /**
     * 在元素分析完成后，添加持久化对象信息实体和持久化对象信息更新实体元素<br>
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case TAG_ENTITY:
                config.entities.add(readEntity);
                break;

            case TAG_UPDATE:
                readEntity.updateEntityList.add(updateEntity);
                break;
        }
    }
}