package com.blackchopper.briefness;

import com.blackchopper.briefness.databinding.JavaLayout;
import com.blackchopper.briefness.util.ClassUtil;
import com.blackchopper.briefness.util.Logger;
import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileManager;
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
            AbstractJavaProxyInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaProxyInfo(elementUtils, classElement);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            BindViews bindViewAnnotation = variableElement.getAnnotation(BindViews.class);

            Logger.v(element.toString());
            Logger.v(bindViewAnnotation.toString());
            Logger.v(fullClassName);

            String[] id=ClassUtil.findViewsById(fullClassName, element.toString());
            for (String s : id) {
                Logger.v(s);
            }
            proxyInfo.bindView.put(id, variableElement);
        }
    }

    @Override
    protected void processClick(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindClick.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, BindClick.class)) continue;
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String fullClassName = typeElement.getQualifiedName().toString();
            AbstractJavaProxyInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaProxyInfo(elementUtils, typeElement);
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
            AbstractJavaProxyInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaProxyInfo(elementUtils, classElement);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            BindView bindViewAnnotation = variableElement.getAnnotation(BindView.class);
            Logger.v(element.toString());
            Logger.v(bindViewAnnotation.toString());
            Logger.v(fullClassName);
            Logger.v(ClassUtil.findViewById(fullClassName, element.toString()));
            String id = ClassUtil.findViewById(fullClassName, element.toString());
            proxyInfo.bindView.put(new String[]{id}, variableElement);
        }
    }


    @Override
    protected void processLayout(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindLayout.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, JavaLayout.class)) continue;
            String fullClassName = element.asType().toString();
            AbstractJavaProxyInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new JavaProxyInfo(elementUtils, (TypeElement) element);
                mProxyMap.put(fullClassName, proxyInfo);
            }
            BindLayout bindViewAnnotation = element.getAnnotation(BindLayout.class);
            proxyInfo.bindLayout.add(new JavaLayout(bindViewAnnotation.value()));
        }
    }


    @Override
    protected void process() {
        for (String key : mProxyMap.keySet()) {
            AbstractJavaProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaInjector injector = new JavaInjector();
                injector.witeCode();

                if (!injector.isViewInjectorExits()) {
                    JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(
                            JavaInjector.PACKAGE_NAME + ".briefness.ViewInjector",
                            proxyInfo.getTypeElement());
                    Writer writer = fileObject.openWriter();
                    writer.write(injector.getViewInjectorCode());
                    writer.flush();
                    writer.close();
                }


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
