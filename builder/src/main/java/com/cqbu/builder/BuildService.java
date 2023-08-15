package com.cqbu.builder;

import com.cqbu.bean.Constants;
import com.cqbu.bean.FieldInfo;
import com.cqbu.bean.TableInfo;
import com.cqbu.utils.DateUtils;
import com.cqbu.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class BuildService {
    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "Service";
        File file = new File(folder, className + ".java");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {
            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVo;");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment() + "Service");
            bw.write("public interface " + className + " {");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "根据条件查询数量");
            bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "分页查询");
            bw.write("\tPaginationResultVo<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "新增");
            bw.write("\tInteger add(" + tableInfo.getBeanName() + " bean);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean);");
            bw.newLine();
            bw.newLine();

            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
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
                    methodParams.append(fieldInfo.getJavaType()).append(" ").append(fieldInfo.getPropertyName());
                    if (index < fieldInfoList.size()) {
                        methodParams.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 查询");
                bw.write("\t" + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 更新");
                bw.write("\tInteger update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParams + ");");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 删除");
                bw.write("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");

            bw.flush();
        } catch (Exception e) {
            logger.error("创建service失败", e);
        }
    }
}
