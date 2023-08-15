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

public class BuildController {
    private static final Logger logger = LoggerFactory.getLogger(BuildController.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_CONTROLLER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "Controller";
        String serviceName = tableInfo.getBeanName() + "Service";
        String serviceBeanName = StringUtils.lowerCaseFirstLetter(serviceName);
        File file = new File(folder, className + ".java");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {
            bw.write("package " + Constants.PACKAGE_CONTROLLER + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".ResponseVo;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + serviceName + ";");
            bw.newLine();
            bw.write("import org.springframework.web.bind.annotation.*;");
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment() + "Controller");
            bw.write("@RestController");
            bw.newLine();
            bw.write("@RequestMapping(\"/" + StringUtils.lowerCaseFirstLetter(tableInfo.getBeanName()) + "\")");
            bw.newLine();
            bw.write("public class " + className + " extends BaseController {");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();

            bw.write("\tprivate " + serviceName + " " + serviceBeanName + ";");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\t@GetMapping(\"/getDataList\")");
            bw.newLine();
            bw.write("\tpublic ResponseVo getDataList(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn getSuccessResponseVo(" + serviceBeanName + ".findListByPage(query));");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "新增");
            bw.write("\t@PostMapping(\"/add\")");
            bw.newLine();
            bw.write("\tpublic Integer add(@RequestBody " + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + serviceBeanName + ".add(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\t@PostMapping(\"/addBatch\")");
            bw.newLine();
            bw.write("\tpublic Integer addBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\treturn this." + serviceBeanName + ".addBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\t@PostMapping(\"/addOrUpdateBatch\")");
            bw.newLine();
            bw.write("\tpublic Integer addOrUpdateBatch(@RequestBody List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\treturn this." + serviceBeanName + ".addOrUpdateBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fieldInfoList = entry.getValue();

                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                StringBuilder resultName = new StringBuilder();
                int index = 0;
                for (FieldInfo fieldInfo : fieldInfoList) {
                    index++;
                    methodName.append(StringUtils.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < fieldInfoList.size()) {
                        methodName.append("And");
                    }
                    methodParams.append(fieldInfo.getJavaType()).append(" ").append(fieldInfo.getPropertyName());
                    resultName.append(fieldInfo.getPropertyName());
                    if (index < fieldInfoList.size()) {
                        methodParams.append(", ");
                        resultName.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 查询");
                bw.write("\t@GetMapping(\"" + "/get" + tableInfo.getBeanName() + "By" + methodName+"\")");
                bw.newLine();
                bw.write("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + serviceBeanName + ".get" + tableInfo.getBeanName() + "By" + methodName + "(" + resultName + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 更新");
                bw.write("\t@PutMapping(\"" + "/update" + tableInfo.getBeanName() + "By" + methodName+"\")");
                bw.newLine();
                bw.write("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "(@RequestBody " + tableInfo.getBeanName() + " bean, " + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + serviceBeanName + ".update" + tableInfo.getBeanName() + "By" + methodName + "(bean, " + resultName + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 删除");
                bw.write("\t@DeleteMapping(\"" + "/delete" + tableInfo.getBeanName() + "By" + methodName+"\")");
                bw.newLine();
                bw.write("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + serviceBeanName + ".delete" + tableInfo.getBeanName() + "By" + methodName + "(" + resultName + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");

            bw.flush();
        } catch (Exception e) {
            logger.error("创建controller失败", e);
        }
    }
}
