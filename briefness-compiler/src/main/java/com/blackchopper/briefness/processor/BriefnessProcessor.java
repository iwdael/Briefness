package com.blackchopper.briefness.processor;

import com.blackchopper.briefness.AbsJavaInfo;
import com.blackchopper.briefness.BindClick;
import com.blackchopper.briefness.BindLayout;
import com.blackchopper.briefness.BindView;
import com.blackchopper.briefness.BindViews;
import com.blackchopper.briefness.JavaInjector;
import com.blackchopper.briefness.JavaInfo;
import com.blackchopper.briefness.databinding.JavaLayout;
import com.blackchopper.briefness.util.Logger;
import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
@AutoService(Processor.class)
public class BriefnessProcessor extends AbstractBriefnessProcessor {
    @Override
    protected void processViews(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindViews.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, BindViews.class)) continue;
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            AbsJavaInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaInfo(elementUtils, classElement);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            BindViews bindViewAnnotation = variableElement.getAnnotation(BindViews.class);
//
//            Logger.v(element.toString());
//            Logger.v(bindViewAnnotation.toString());
//            Logger.v(fullClassName);
//
//            String[] id=ClassUtil.findViewsById(fullClassName, element.toString());
//            for (String s : id) {
//                Logger.v(s);
//            }
            proxyInfo.bindView.put(bindViewAnnotation.value(), variableElement);
        }
    }

    @Override
    protected void processClick(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindClick.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, BindClick.class)) continue;
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String fullClassName = typeElement.getQualifiedName().toString();
            AbsJavaInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaInfo(elementUtils, typeElement);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            BindClick bindViewAnnotation = element.getAnnotation(BindClick.class);
            int[] id = bindViewAnnotation.value();
            proxyInfo.bindClick.put(id, element);
        }
    }

    @Override
    protected void processView(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, BindView.class)) continue;
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            AbsJavaInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaInfo(elementUtils, classElement);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            BindView bindViewAnnotation = variableElement.getAnnotation(BindView.class);
//            Logger.v(element.toString());
//            Logger.v(bindViewAnnotation.toString());
//            Logger.v(fullClassName);
//            Logger.v(ClassUtil.findViewById(fullClassName, element.toString()));
            int id = bindViewAnnotation.value();
            proxyInfo.bindView.put(new int[]{id}, variableElement);
        }
    }


    @Override
    protected void processLayout(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindLayout.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, JavaLayout.class)) continue;
            String fullClassName = element.asType().toString();
            AbsJavaInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaInfo(elementUtils, (TypeElement) element);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            System.out.print(":briefness:generateClass:"+fullClassName+"Briefnessor");
            BindLayout bindViewAnnotation = element.getAnnotation(BindLayout.class);
            proxyInfo.bindLayout.add(new JavaLayout(bindViewAnnotation.value()));
        }
    }


    @Override
    protected void process() {
        for (String key : mProxyMap.keySet()) {
            AbsJavaInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaInjector injector = new JavaInjector();

                try {
                    JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(
                            "com.blackchopper.briefness.BriefnessInjector",
                            proxyInfo.getTypeElement());
                    Writer openWriter = fileObject.openWriter();
                    openWriter.write(injector.getBriefnessInjectorCode());
                    openWriter.flush();
                    openWriter.close();
                } catch (Exception e) {
                    Logger.v(e.getMessage());
                }
                injector.witeCode();

                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement()
                );
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                error(proxyInfo.getTypeElement(), "Unable to write injector for type %s: %s", proxyInfo.getTypeElement(), e.getMessage());
                e.printStackTrace();
            }
        }

    }

}
