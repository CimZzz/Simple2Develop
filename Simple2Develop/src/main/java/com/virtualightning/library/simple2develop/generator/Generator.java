package com.virtualightning.library.simple2develop.generator;

/**
 * Created by CimZzz on 11/2/16.<br>
 * Project Name : Virtual-Lightning Simple2Develop<br>
 * Since : VLSimple2Develop_0.2.3<br>
 * Description:<br>
 * Description
 */
public class Generator {
    private static String projectPath = "app/src/main";
    private static String expectPath = "";

    public static void initGenerator(String projectPath) {
        Generator.projectPath = projectPath;
    }

    public static void setExpectPath(String expectPath) {
        Generator.expectPath = expectPath;
    }

    public static void generateMVPSrcFile(Class<?> cls) {

    }
}
