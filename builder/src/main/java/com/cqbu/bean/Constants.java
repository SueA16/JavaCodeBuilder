package com.cqbu.bean;

import com.cqbu.utils.PropertiesUtils;

public class Constants {
    public static String AUTHOR_COMMENT;

    public static Boolean IGNORE_TABLE_PREFIX;

    // 后缀
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_FUZZY;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;
    public static String SUFFIX_MAPPER;

    // 需要忽略的字段
    public static String IGNORE_BEAN_2JSON_FILED;
    public static String IGNORE_BEAN_2JSON_ANNOTATION;
    public static String IGNORE_BEAN_2JSON_CLASS;

    //日期序列化格式
    public static String BEAN_DATE_FORMAT_ANNOTATION;
    public static String BEAN_DATE_FORMAT_CLASS;

    //日期序反列化格式
    public static String BEAN_DATE_PARSE_ANNOTATION;
    public static String BEAN_DATE_PARSE_CLASS;

    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_VO;
    public static String PACKAGE_QUERY;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_ENUMS;
    public static String PACKAGE_EXCEPTION;
    public static String PACKAGE_MAPPER;
    public static String PACKAGE_SERVICE;
    public static String PACKAGE_SERVICE_IMPL;
    public static String PACKAGE_CONTROLLER;

    public static String PATH_JAVA = "java";
    public static String PATH_RESOURCES = "resources";
    public static String PATH_BASE;
    public static String PATH_PO;
    public static String PATH_VO;
    public static String PATH_QUERY;
    public static String PATH_PARAM;
    public static String PATH_UTILS;
    public static String PATH_ENUMS;
    public static String PATH_EXCEPTION;
    public static String PATH_MAPPER;
    public static String PATH_MAPPER_XML;
    public static String PATH_SERVICE;
    public static String PATH_SERVICE_IMPL;
    public static String PATH_CONTROLLER;

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public static final String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint"};
    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};

    static {
        AUTHOR_COMMENT = PropertiesUtils.getString("author.comment");

        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));

        SUFFIX_BEAN_QUERY = PropertiesUtils.getString("suffix.bean.query");
        SUFFIX_BEAN_QUERY_FUZZY = PropertiesUtils.getString("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getString("suffix.bean.query.time.end");
        SUFFIX_MAPPER = PropertiesUtils.getString("suffix.mapper");

        // 需要忽略的字段
        IGNORE_BEAN_2JSON_FILED = PropertiesUtils.getString("ignore.bean.toJson.filed");
        IGNORE_BEAN_2JSON_ANNOTATION = PropertiesUtils.getString("ignore.bean.toJson.annotation");
        IGNORE_BEAN_2JSON_CLASS = PropertiesUtils.getString("ignore.bean.toJson.class");

        //日期序列化格式
        BEAN_DATE_FORMAT_ANNOTATION = PropertiesUtils.getString("bean.date.format.annotation");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");

        //日期序反列化格式
        BEAN_DATE_PARSE_ANNOTATION = PropertiesUtils.getString("bean.date.parse.annotation");
        BEAN_DATE_PARSE_CLASS = PropertiesUtils.getString("bean.date.parse.class");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        // PO
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.vo");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtils.getString("package.exception");
        PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mapper");
        PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtils.getString("package.service.impl");
        PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtils.getString("package.controller");

        PATH_BASE = PropertiesUtils.getString("path.base");

        PATH_PO = PATH_BASE + PATH_JAVA + "/" + PACKAGE_PO.replace(".", "/");
        PATH_VO = PATH_BASE + PATH_JAVA + "/" + PACKAGE_VO.replace(".", "/");
        PATH_QUERY = PATH_BASE + PATH_JAVA + "/" + PACKAGE_QUERY.replace(".", "/");
        PATH_UTILS = PATH_BASE + PATH_JAVA + "/" + PACKAGE_UTILS.replace(".", "/");
        PATH_ENUMS = PATH_BASE + PATH_JAVA + "/" + PACKAGE_ENUMS.replace(".", "/");
        PATH_EXCEPTION = PATH_BASE + PATH_JAVA + "/" + PACKAGE_EXCEPTION.replace(".", "/");
        PATH_MAPPER = PATH_BASE + PATH_JAVA + "/" + PACKAGE_MAPPER.replace(".", "/");
        PATH_MAPPER_XML = PATH_BASE + PATH_RESOURCES + "/" + PACKAGE_MAPPER.replace(".", "/");
        PATH_SERVICE = PATH_BASE + PATH_JAVA + "/" + PACKAGE_SERVICE.replace(".", "/");
        PATH_SERVICE_IMPL = PATH_BASE + PATH_JAVA + "/" + PACKAGE_SERVICE_IMPL.replace(".", "/");
        PATH_CONTROLLER = PATH_BASE + PATH_JAVA + "/" + PACKAGE_CONTROLLER.replace(".", "/");
    }

    public static void main(String[] args) {
        System.out.println(PATH_MAPPER_XML);
    }

}
