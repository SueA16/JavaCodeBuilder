package com.cqbu.builder;

import com.cqbu.bean.Constants;
import com.cqbu.bean.FieldInfo;
import com.cqbu.bean.TableInfo;
import com.cqbu.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildMapperXml {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);

    private static final String BASE_COLUMN_LIST = "base_column_list";

    private static final String BASE_QUERY_CONDITION = "base_query_condition";

    private static final String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";

    private static final String QUERY_CONDITION = "query_condition";

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPER_XML);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File file = new File(folder, className + ".xml");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPER + "." + className + "\">");
            bw.newLine();
            bw.newLine();

            bw.write("\t<!--实体映射-->");
            bw.newLine();
            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\"" + poClass + "\">");
            bw.newLine();
            // 主键是否自增
            FieldInfo idField = null;
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())
                        && entry.getValue().size() == 1) {
                    idField = entry.getValue().get(0);
                    break;
                }
            }

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t<!-- " + fieldInfo.getComment() + " -->");
                bw.newLine();
                String key = "";
                if (idField != null && fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();
            bw.newLine();

            /**
             * 通用查询
             */
            bw.write("\t<!--通用查询结果列-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            bw.newLine();
            StringBuilder columnBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                columnBuilder.append(fieldInfo.getFieldName()).append(",");
            }
            String columnStr = columnBuilder.substring(0, columnBuilder.lastIndexOf(","));
            bw.write("\t\t" + columnStr);
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            /**
             * 基础查询条件
             */
            bw.write("\t<!--基础查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                String flagStr = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    flagStr = " and query." + fieldInfo.getPropertyName() + " !=''";
                }
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + flagStr + "\">");
                bw.newLine();
                bw.write("\t\t\tand id = #{query." + fieldInfo.getPropertyName() + "}");
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            /**
             * 扩展查询条件
             */
            bw.write("\t<!--扩展查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldExtendList()) {
                String flagStr = " and query." + fieldInfo.getPropertyName() + " !=''";
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + flagStr + "\">");
                bw.newLine();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t\t\tand " + fieldInfo.getFieldName() + " like concat('%', #{query."
                            + fieldInfo.getPropertyName() + "}, '%') ");
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) ||
                        ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)) {
                        bw.write("\t\t\tand " + fieldInfo.getFieldName() + " &gt;= str_to_date(#{query."
                                + fieldInfo.getPropertyName() + "}, '%Y-%m-%d')");
                    } else if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_END)) {
                        bw.write("\t\t\tand " + fieldInfo.getFieldName() + " &lt; date_add(str_to_date(#{query."
                                + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'),interval 1 day)");
                    }
                }
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            /**
             * 通用查询条件
             */
            bw.write("\t<!--通用查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();

            /**
             * 查询列表
             */
            bw.write("\t<!--查询列表-->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT <include refid=\"" + BASE_COLUMN_LIST + "\"/> FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy != null\"> order by $(query.orderBy) </if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage != null\"> limit #{query.simplePage.start}, #{query.simplePage.end} </if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            /**
             * 查询数量
             */
            bw.write("\t<!--查询数量-->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName() + " <include refid=\"" + QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();
            bw.newLine();

            /**
             * 单挑插入
             */
            bw.write("\t<!--单挑插入-->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\"" + poClass + "\">");
            bw.newLine();

            // 如果主键自增
            if (idField != null && idField.getAutoIncrement()) {
                // bean.id 属性将会被赋值为最后插入行的自增主键值。可以在插入操作完成后读取 bean.id 的值，即可获取到插入行的主键值
                // order：指定 <selectKey> 在插入语句之前（BEFORE）或之后（AFTER）执行，默认是 AFTER，即在插入操作之后执行
                bw.write("\t\t<selectKey keyProperty=\"bean." + idField.getFieldName() + "\" resultType=\""
                        + idField.getJavaType() + "\" " + "order=\"AFTER\">");
                bw.newLine();
                bw.write("\t\t\tSELECT LAST_INSERT_ID()");
                bw.newLine();
                bw.write("\t\t</selectKey>");
                bw.newLine();
            }

            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + " ");
            bw.newLine();

            // suffixOverrides：指定要删除的末尾字符，如果 SQL 语句的结尾有该字符，则会被删除掉。
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();

            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "}  ,");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();

            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            /**
             * 插入或更新单条数据
             */
            bw.write("\t<!--插入或更新单条数据-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poClass + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + " ");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();

            // 不更新唯一索引
            List<String> keyList = new ArrayList<>();
            for (Map.Entry<String, List<FieldInfo>> keyField : tableInfo.getKeyIndexMap().entrySet()) {
                for (FieldInfo item : keyField.getValue()) {
                    keyList.add(item.getFieldName());
                }
            }

            bw.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (keyList.contains(fieldInfo.getFieldName())) {
                    continue;
                }
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + " = " + "VALUES (" + fieldInfo.getFieldName() + "), ");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            /**
             * 批量插入
             */
            bw.write("\t<!--批量插入-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertBatch\" parameterType=\"" + poClass + "\">");
            bw.newLine();

            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder insertBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldInfo.getAutoIncrement()) {
                    continue;
                }
                stringBuilder.append(fieldInfo.getFieldName()).append(",");
                insertBuilder.append("#{item.").append(fieldInfo.getPropertyName()).append("},");
            }
            String insertFieldStr = stringBuilder.substring(0, stringBuilder.lastIndexOf(","));
            String insertBuilderStr = insertBuilder.substring(0, insertBuilder.lastIndexOf(","));

            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + " (" + insertFieldStr + ") values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();

            bw.write("\t\t\t(" + insertBuilderStr+")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            /**
             * 批量插入或更新
             */
            bw.write("\t<!--批量插入或更新-->");
            bw.newLine();
            bw.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poClass + "\">");
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName() + " (" + insertFieldStr + ") values");
            bw.newLine();
            bw.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bw.newLine();
            bw.write("\t\t\t(" + insertBuilderStr+")");
            bw.newLine();
            bw.write("\t\t</foreach>");
            bw.newLine();
            bw.write("\t\ton DUPLICATE key update");
            bw.newLine();
            StringBuilder insertOrUpdateBuilder = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                insertOrUpdateBuilder.append(fieldInfo.getFieldName()).append(" = VALUES (")
                        .append(fieldInfo.getFieldName()).append("),");
            }
            String insertOrUpdateBuilderStr = insertOrUpdateBuilder.substring(0, insertOrUpdateBuilder.lastIndexOf(","));
            bw.write("\t\t" + insertOrUpdateBuilderStr);
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();
            bw.newLine();

            /**
             * 根据主键/索引完成 单挑数据的 添加 更新 删除
             */
            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                int index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder paramNames = new StringBuilder();
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    paramNames.append(fieldInfo.getFieldName()).append("=#{" + fieldInfo.getPropertyName() + "}");
                    if (index < keyFieldInfoList.size()) {
                        methodName.append("And");
                        paramNames.append(" and ");
                    }
                }
                bw.write("\t<!--" + "根据 " + methodName + " 查询" + "-->");
                bw.newLine();
                bw.write("\t<select id=\"selectBy" + methodName + "\" resultMap=\"base_result_map\">");
                bw.newLine();
                bw.write("\t\tselect <include refid=\"" + BASE_COLUMN_LIST + "\"/> from " + tableInfo.getTableName()
                        + " where " + paramNames);
                bw.newLine();
                bw.write("\t</select>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!--" + "根据 " + methodName + " 更新" + "-->");
                bw.newLine();
                bw.write("\t<update id=\"updateBy" + methodName + "\" parameterType=\"" + poClass + "\">");
                bw.newLine();
                bw.write("\t\tupdate " + tableInfo.getTableName());
                bw.newLine();
                bw.write("\t\t<set>");
                bw.newLine();
                for (FieldInfo fieldInfo:tableInfo.getFieldList()){
                    bw.write("\t\t\t<if test=\"bean."+fieldInfo.getPropertyName()+" != null\">");
                    bw.newLine();
                    bw.write("\t\t\t\t"+fieldInfo.getFieldName()+" = #{bean."+fieldInfo.getPropertyName()+"}");
                    bw.newLine();
                    bw.write("\t\t\t</if>");
                    bw.newLine();
                }
                bw.write("\t\t</set>");
                bw.newLine();
                bw.write("\t\twhere "+paramNames);
                bw.newLine();
                bw.write("\t</update>");
                bw.newLine();
                bw.newLine();

                bw.write("\t<!--" + "根据 " + methodName + " 删除" + "-->");
                bw.newLine();
                bw.write("\t<delete id=\"deleteBy" + methodName + "\">");
                bw.newLine();
                bw.write("\t\tdelete from " + tableInfo.getTableName() + " where " + paramNames);
                bw.newLine();
                bw.write("\t</delete>");
                bw.newLine();
                bw.newLine();
            }


            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建mapper XML失败", e);
        }

    }
}
