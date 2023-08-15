package com.cqbu.builder;

import com.cqbu.bean.Constants;
import com.cqbu.bean.FieldInfo;
import com.cqbu.bean.TableInfo;
import com.cqbu.utils.DateUtils;
import com.cqbu.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, tableInfo.getBeanName() + ".java");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {
            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import java.io.Serializable;");
            bw.newLine();
            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            // 如果有日期时间类 导包
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS);
                bw.newLine();
                bw.write(Constants.BEAN_DATE_PARSE_CLASS);
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtils;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
                bw.newLine();
            }
            // 序列化需要忽略的字段  导包
            boolean haveIgnoreBean = false;
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_2JSON_FILED.split(","),
                        tableInfo.getTableName() + "." + fieldInfo.getFieldName())) {
                    haveIgnoreBean = true;
                    break;
                }
            }
            if (haveIgnoreBean) {
                bw.write(Constants.IGNORE_BEAN_2JSON_CLASS);
                bw.newLine();
            }
            bw.newLine();

            // 构建类注释
            BuildComment.createClassComment(bw, tableInfo.getComment());

            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 构建字段注释
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                // 为datetime类型添加注解 导包
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    // 序列化
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_ANNOTATION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                    // 反序列化
                    bw.write("\t" + String.format(Constants.BEAN_DATE_PARSE_ANNOTATION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }
                // 为date类型添加注解 导包
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    // 序列化
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_ANNOTATION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    // 反序列化
                    bw.write("\t" + String.format(Constants.BEAN_DATE_PARSE_ANNOTATION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                // 序列化需要忽略的字段 表名.字段名
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_2JSON_FILED.split(","),
                        tableInfo.getTableName() + "." + fieldInfo.getFieldName())) {
                    bw.write("\t" + Constants.IGNORE_BEAN_2JSON_ANNOTATION);
                    bw.newLine();
                }
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
            }
            // get and set
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                String tempField = StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName());
                // get
                bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tempField + "() {");
                bw.newLine();
                bw.write("\t\treturn " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                // set
                bw.write("\tpublic void set" + tempField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bw.newLine();
                bw.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }
            // toString
            StringBuilder toStr = new StringBuilder();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                String dateName = fieldInfo.getPropertyName();
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    dateName = "DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD.getPattern())";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    dateName = "DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())";
                }
                toStr.append("\"").append(fieldInfo.getComment()).append(":\" + (")
                        .append(fieldInfo.getPropertyName())
                        .append(" == null ? \"空\" : ")
                        .append(dateName)
                        .append(")")
                        .append(" + \", \" + \n\t\t\t\t");
            }
            String res = toStr.substring(0, toStr.lastIndexOf(" + \", \" +"));
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn " + res + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建po失败", e);
        }

    }
}
