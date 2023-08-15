package com.cqbu.builder;

import com.cqbu.bean.Constants;
import com.cqbu.bean.FieldInfo;
import com.cqbu.bean.TableInfo;
import com.cqbu.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File file = new File(folder, className + ".java");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {
            bw.write("package " + Constants.PACKAGE_MAPPER + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();
            // 构建类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + " Mapper");

            bw.write("public interface " + className + "<T, P> extends BaseMapper<T, P> {");
            bw.newLine();
            bw.newLine();

            // method
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();

                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                int index = 0;
                for (FieldInfo fieldInfo : fieldInfoList) {
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < fieldInfoList.size()) {
                        methodName.append("And");
                    }
                    methodParams.append("@Param(\"").append(fieldInfo.getPropertyName()).append("\") ")
                            .append(fieldInfo.getJavaType()).append(" ").append(fieldInfo.getPropertyName());
                    if (index < fieldInfoList.size()) {
                        methodParams.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 查询");
                bw.write("\tT selectBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 更新");
                bw.write("\tInteger updateBy" + methodName + "(" +"@Param(\"bean\") T t, " +methodParams + ");");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 删除");
                bw.write("\tInteger deleteBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建mapper失败", e);
        }

    }
}
