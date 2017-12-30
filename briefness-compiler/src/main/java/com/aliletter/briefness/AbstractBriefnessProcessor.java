package com.aliletter.briefness;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/9/15.
 */


public abstract class AbstractBriefnessProcessor extends AbstractProcessor {
    protected Messager messager;
    protected Elements elementUtils;
    protected Map<String, ProxyInfo> mProxyMap = new LinkedHashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process...");
        mProxyMap.clear();
        processLayout(annotations, roundEnv);
        processView(annotations, roundEnv);
        processViews(annotations, roundEnv);
        processClick(annotations, roundEnv);
        processClass(annotations, roundEnv);
        processField(annotations, roundEnv);
        process();
        return true;
    }

    protected abstract void processField(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void processClass(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void processViews(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void processClick(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void processView(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void processLayout(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

    protected abstract void process();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportType = new LinkedHashSet<>();
        supportType.add(BindLayout.class.getCanonicalName());
        supportType.add(BindView.class.getCanonicalName());
        supportType.add(BindViews.class.getCanonicalName());
        supportType.add(BindClick.class.getCanonicalName());
        supportType.add(BindClass.class.getCanonicalName());
        supportType.add(BindField.class.getCanonicalName());

        return supportType;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    protected boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
//        if (annotatedElement.getKind() != ElementKind.FIELD) {
//            error(annotatedElement, "%s must be declared on field.", clazz.getSimpleName());
//            return false;
//        }
        if (ClassValidator.isPrivate(annotatedElement)) {
            error(annotatedElement, "%s() must can not be private.", annotatedElement.getSimpleName());
            return false;
        }

        return true;
    }

    protected void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }
}
