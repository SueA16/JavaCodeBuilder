package com.cqbu.builder;

import com.cqbu.bean.Constants;
import com.cqbu.bean.FieldInfo;
import com.cqbu.bean.TableInfo;
import com.cqbu.utils.JsonUtils;
import com.cqbu.utils.PropertiesUtils;
import com.cqbu.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection con = null;
    private static final String SQL_SHOW_TABLE_STATUS = "show table status";
    private static final String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
    private static final String SQL_SHOW_TABLE_INDEX = "show index from %s";


    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String username = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("数据库连接失败...", e);
        }
    }

    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
        try {
            ps = con.prepareStatement(SQL_SHOW_TABLE_STATUS);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("name");
                String comment = rs.getString("comment");

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(tableName.indexOf("_") + 1);
                }
                beanName = processFiled(beanName, true);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_QUERY);

                readFieldInfo(tableInfo);
                getKeyIndexInfo(tableInfo);

                tableInfoList.add(tableInfo);
            }
        } catch (Exception e) {
            logger.error("读取表失败", e);
        } finally {
            closeRsPs(ps, rs);
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("{}", JsonUtils.convertObj2Json(tableInfoList));
        return tableInfoList;
    }

    private static void readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        List<FieldInfo> fieldExtendList = new ArrayList<>();
        try {
            ps = con.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("auto_increment".equals(extra));
                fieldInfo.setPropertyName(processFiled(field, false));
                fieldInfo.setJavaType(processJavaType(type));
                fieldInfoList.add(fieldInfo);

                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
                    tableInfo.setHaveDateTime(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    tableInfo.setHaveDate(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
                    tableInfo.setHaveBigDecimal(true);
                }

                // String类型参数
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    String PropertyName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY;

                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(PropertyName);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setSqlType(fieldInfo.getSqlType());
                    fieldExtendList.add(fuzzyField);
                }
                // Date类型参数
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) ||
                        ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    String startName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START;
                    String endName = fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END;

                    FieldInfo startField = new FieldInfo();
                    startField.setJavaType("String");
                    startField.setPropertyName(startName);
                    startField.setFieldName(fieldInfo.getFieldName());
                    startField.setSqlType(fieldInfo.getSqlType());
                    fieldExtendList.add(startField);

                    FieldInfo endField = new FieldInfo();
                    endField.setJavaType("String");
                    endField.setPropertyName(endName);
                    endField.setFieldName(fieldInfo.getFieldName());
                    endField.setSqlType(fieldInfo.getSqlType());
                    fieldExtendList.add(endField);
                }
            }
            tableInfo.setFieldList(fieldInfoList);
            tableInfo.setFieldExtendList(fieldExtendList);
        } catch (Exception e) {
            logger.error("读取字段失败", e);
        } finally {
            closeRsPs(ps, fieldResult);
        }
    }

    private static void getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet result = null;

        Map<String,FieldInfo> fieldInfoMap = new HashMap<>();

        for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
            fieldInfoMap.put(fieldInfo.getFieldName(),fieldInfo);
        }

        try {
            ps = con.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            result = ps.executeQuery();
            while (result.next()) {
                String keyName = result.getString("key_name");
                int nonUnique = result.getInt("non_unique");
                String columnName = result.getString("column_name");
                if (nonUnique == 1) {
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (keyFieldList == null) {
                    keyFieldList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                keyFieldList.add(fieldInfoMap.get(columnName));
            }
        } catch (Exception e) {
            logger.error("读取索引失败", e);
        } finally {
            closeRsPs(ps, result);
        }
    }

    private static String processFiled(String field, Boolean upperCaseFirstLetter) {
        StringBuilder sb = new StringBuilder();
        String[] fields = field.split("_");
        fields[0] = upperCaseFirstLetter ? StringUtils.upperCaseFirstLetter(fields[0]) : fields[0];
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            fields[i] = StringUtils.upperCaseFirstLetter(fields[i]);
            sb.append(fields[i]);
        }
        return sb.toString();
    }

    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPES, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPES, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, type)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPES, type)) {
            return "BigDecimal";
        } else {
            throw new RuntimeException("无法识别类型:" + type);
        }
    }

    private static void closeRsPs(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
