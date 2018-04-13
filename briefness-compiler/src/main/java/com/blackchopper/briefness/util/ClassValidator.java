package com.blackchopper.briefness.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

final class ClassValidator {
    static boolean isPrivate(Element element) {
        return element.getModifiers().contains(Modifier.PRIVATE);
    }

    static String getClassName(Element type, String packageName) {
        int packageLen = packageName.length() + 1;
        return ((TypeElement) type).getQualifiedName().toString().substring(packageLen).replace('.', '$');
     }
}
