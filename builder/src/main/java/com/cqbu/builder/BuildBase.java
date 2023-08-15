package com.cqbu.builder;

import com.cqbu.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {
    private static Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute() {
        List<String> packageList = new ArrayList<>();
        // 生成date枚举
        packageList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(packageList, "DateTimePatternEnum", Constants.PATH_ENUMS);
        // 生成时间工具类
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(packageList, "DateUtils", Constants.PATH_UTILS);
        // 生成BaseMapper
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_MAPPER + ";");
        build(packageList, "BaseMapper", Constants.PATH_MAPPER);
        // 生成PageSize枚举
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(packageList, "PageSize", Constants.PATH_ENUMS);
        // 生成SimplePage
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_QUERY + ";");
        packageList.add("import " + Constants.PACKAGE_ENUMS + ".PageSize;");
        build(packageList, "SimplePage", Constants.PATH_QUERY);
        // 生成BaseQuery
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_QUERY + ";");
        build(packageList, "BaseQuery", Constants.PATH_QUERY);
        // 生成PaginationResultVo
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_VO + ";");
        build(packageList, "PaginationResultVo", Constants.PATH_VO);
        // 生成BusinessException
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_EXCEPTION + ";");
        packageList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
        build(packageList, "BusinessException", Constants.PATH_EXCEPTION);
        // 生成ResponseCodeEnum
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(packageList, "ResponseCodeEnum", Constants.PATH_ENUMS);
        // 生成BaseController
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_CONTROLLER + ";");
        packageList.add("import " + Constants.PACKAGE_VO + ".ResponseVo;");
        packageList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
        build(packageList, "BaseController", Constants.PATH_CONTROLLER);
        // 生成ResponseVo
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_VO + ";");
        build(packageList, "ResponseVo", Constants.PATH_VO);
        // 生成GlobalExceptionHandlerController
        packageList.clear();
        packageList.add("package " + Constants.PACKAGE_CONTROLLER + ";");
        packageList.add("import " + Constants.PACKAGE_VO + ".ResponseVo;");
        packageList.add("import " + Constants.PACKAGE_ENUMS + ".ResponseCodeEnum;");
        packageList.add("import " + Constants.PACKAGE_EXCEPTION + ".BusinessException;");
        build(packageList, "GlobalExceptionHandlerController", Constants.PATH_CONTROLLER);
    }

    private static void build(List<String> packageList, String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outPutPath, fileName + ".java");

        String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();

        try (FileOutputStream out = new FileOutputStream(javaFile);
             OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(outW);
             InputStream in = new FileInputStream(templatePath);
             InputStreamReader inR = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inR);
        ) {
            for (String packageInfo : packageList) {
                bw.write(packageInfo);
                bw.newLine();
                bw.newLine();
            }
            String lineInfo;
            while ((lineInfo = br.readLine()) != null) {
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("生成基础类: {} 失败", fileName, e);
        }

    }


}
