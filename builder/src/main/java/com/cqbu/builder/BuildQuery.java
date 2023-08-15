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

public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;
        File file = new File(folder, className + ".java");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {
            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();
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
            }
            // 构建类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + " 查询对象");

            bw.write("public class " + className + " extends BaseQuery {");
            bw.newLine();

            List<FieldInfo> fieldInfoList = new ArrayList<>();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                fieldInfoList.add(fieldInfo);
                // 构建字段注释
                BuildComment.createFieldComment(bw, fieldInfo.getComment());

                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
                // String类型参数
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    String PropertyName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;

                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + PropertyName + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(PropertyName);
                    fieldInfoList.add(fuzzyField);
                }
                // Date类型参数
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) ||
                        ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    String startName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START;
                    String endName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END;

                    bw.write("\tprivate String " + startName + ";");
                    bw.newLine();
                    bw.newLine();
                    bw.write("\tprivate String " + endName + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo startField = new FieldInfo();
                    startField.setJavaType("String");
                    startField.setPropertyName(startName);
                    fieldInfoList.add(startField);

                    FieldInfo endField = new FieldInfo();
                    endField.setJavaType("String");
                    endField.setPropertyName(endName);
                    fieldInfoList.add(endField);
                }

            }
            // get and set
            for (FieldInfo fieldInfo : fieldInfoList) {
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

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建query失败", e);
        }

    }
}
