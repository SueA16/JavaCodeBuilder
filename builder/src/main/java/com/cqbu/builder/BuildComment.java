package com.cqbu.builder;

import com.cqbu.bean.Constants;
import com.cqbu.utils.DateUtils;

import java.io.BufferedWriter;
import java.util.Date;

public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String comment) throws Exception {
        bw.write("/**");
        bw.newLine();
        bw.write(" * description:" + comment);
        bw.newLine();
        bw.write(" * author:" + Constants.AUTHOR_COMMENT);
        bw.newLine();
        bw.write(" * date:" + DateUtils.format(new Date(), DateUtils.YYYY_MM_DD_SLASH));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }

    public static void createFieldComment(BufferedWriter bw, String comment) throws Exception {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + (comment == null ? "" : comment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }

    public static void createMethodComment(BufferedWriter bw, String comment) throws Exception {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + (comment == null ? "" : comment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }
}
