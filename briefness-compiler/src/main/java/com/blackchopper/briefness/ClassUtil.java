package com.blackchopper.briefness;

import static com.blackchopper.briefness.XmlProxyInfo.SPLIT;
import static com.blackchopper.briefness.XmlProxyInfo.readTextFile;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

public class ClassUtil {

    public static String getSuperClass(String qualifiedName) {
        String module = System.getProperty("user.dir") + readTextFile(System.getProperty("user.dir") + "/BriefnessConfig") + SPLIT;
        String path = qualifiedName.substring(0, qualifiedName.lastIndexOf(".")).replace(".", SPLIT);
        String clazzName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
        String file=module + path + SPLIT + clazzName+".java";
        file=file.replace("/","\\");
        System.out.print(file);
        return readTextFile(file);
    }
}
