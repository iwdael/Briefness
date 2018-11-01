package com.hacknife.briefness.util;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public class Logger {
    public static void v(String msg) {
        System.out.print(":briefness:compileJava: " + msg + "\n");
    }

    public static void p(String msg) {
        System.out.print(":briefness:parser: " + msg + "\n");
    }
}
