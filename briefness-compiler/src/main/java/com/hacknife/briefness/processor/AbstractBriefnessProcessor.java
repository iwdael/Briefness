package com.hacknife.briefness.processor;

import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindField;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;
import com.hacknife.briefness.Briefnessor;
import com.hacknife.briefness.util.ClassValidator;

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
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */


public abstract class AbstractBriefnessProcessor extends AbstractProcessor {
    protected Messager messager;
    protected Elements elementUtils;
    protected Map<String, Briefnessor> mProxyMap = new LinkedHashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process...");
        mProxyMap.clear();
        processLayout(annotations, roundEnv);
        processView(annotations, roundEnv);
        processClick(annotations, roundEnv);
        processField(annotations, roundEnv);
        process();
        return true;
    }

    protected abstract void processField(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);


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
        supportType.add(BindClick.class.getCanonicalName());
        supportType.add(BindField.class.getCanonicalName());
        return supportType;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    protected boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
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
