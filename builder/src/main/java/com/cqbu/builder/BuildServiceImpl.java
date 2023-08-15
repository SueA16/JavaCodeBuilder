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

public class BuildServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_SERVICE_IMPL);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String interfaceName = tableInfo.getBeanName() + "Service";
        String className = tableInfo.getBeanName() + "ServiceImpl";
        String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        String mapperBeanName = StringUtils.lowerCaseFirstLetter(mapperName);
        File file = new File(folder, className + ".java");

        try (FileOutputStream out = new FileOutputStream(file);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW)) {
            bw.write("package " + Constants.PACKAGE_SERVICE_IMPL + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVo;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + ".SimplePage;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_SERVICE + "." + interfaceName + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_MAPPER + "." + mapperName + ";");
            bw.newLine();
            bw.write("import org.springframework.stereotype.Service;");
            bw.newLine();
            bw.newLine();
            bw.write("import javax.annotation.Resource;");
            bw.newLine();
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment() + "ServiceImpl");
            bw.write("@Service(\"" + StringUtils.lowerCaseFirstLetter(interfaceName) + "\")");
            bw.newLine();
            bw.write("public class " + className + " implements " + interfaceName + " {");
            bw.newLine();
            bw.newLine();

            bw.write("\t@Resource");
            bw.newLine();

            bw.write("\tprivate " + mapperName + "<" + tableInfo.getBeanName() + ", "
                    + tableInfo.getBeanParamName() + "> " + mapperBeanName + ";");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "根据条件查询列表");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic List<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".selectList(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "根据条件查询数量");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer findCountByParam(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".selectCount(query);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "分页查询");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic PaginationResultVo<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() + " query) {");
            bw.newLine();
            bw.write("\t\tInteger count = this.findCountByParam(query);");
            bw.newLine();
            bw.write("\t\tInteger pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();");
            bw.newLine();
            bw.write("\t\tSimplePage page = new SimplePage(query.getPageNo(), count, pageSize);");
            bw.newLine();
            bw.write("\t\tquery.setSimplePage(page);");
            bw.newLine();
            bw.write("\t\tList<" + tableInfo.getBeanName() + "> list = this.findListByParam(query);");
            bw.newLine();
            bw.write("\t\treturn new PaginationResultVo<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "新增");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer add(" + tableInfo.getBeanName() + " bean) {");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insert(bean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "批量新增");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertBatch(listBean);");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();

            BuildComment.createMethodComment(bw, "批量新增或修改");
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic Integer addOrUpdateBatch(List<" + tableInfo.getBeanName() + "> listBean) {");
            bw.newLine();
            bw.write("\t\tif (listBean == null || listBean.isEmpty()) {");
            bw.newLine();
            bw.write("\t\t\treturn 0;");
            bw.newLine();
            bw.write("\t\t}");
            bw.newLine();
            bw.write("\t\treturn this." + mapperBeanName + ".insertOrUpdateBatch(listBean);");
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
                bw.write("\tpublic " + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + mapperBeanName + ".selectBy" + methodName + "(" + resultName + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 更新");
                bw.write("\tpublic Integer update" + tableInfo.getBeanName() + "By" + methodName + "(" + tableInfo.getBeanName() + " bean, " + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + mapperBeanName + ".updateBy" + methodName + "(bean, " + resultName + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据 " + methodName + " 删除");
                bw.write("\tpublic Integer delete" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ") {");
                bw.newLine();
                bw.write("\t\treturn this." + mapperBeanName + ".deleteBy" + methodName + "(" + resultName + ");");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");

            bw.flush();
        } catch (Exception e) {
            logger.error("创建serviceImpl失败", e);
        }
    }
}
