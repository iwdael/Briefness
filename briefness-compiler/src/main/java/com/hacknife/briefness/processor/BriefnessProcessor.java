package com.hacknife.briefness.processor;

import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;
import com.hacknife.briefness.Briefnessor;
import com.hacknife.briefness.Constant;
import com.hacknife.briefness.JavaInjector;
import com.hacknife.briefness.util.Logger;
import com.google.auto.service.AutoService;
import com.hacknife.briefness.util.StringUtil;

import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
@AutoService(Processor.class)
public class BriefnessProcessor extends AbstractBriefnessProcessor {
    boolean inited = false;
    String buidPath;
    String packages;


    @Override
    protected void processClick(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindClick.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, BindClick.class)) continue;
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            String fullClassName = typeElement.getQualifiedName().toString();
            Briefnessor briefnessor = mProxyMap.get(fullClassName);
            if (briefnessor == null) {
                briefnessor = new Briefnessor(elementUtils, typeElement);
                mProxyMap.put(fullClassName, briefnessor);
            }
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
            Briefnessor briefnessor = mProxyMap.get(fullClassName);
            if (briefnessor == null) {
                briefnessor = new Briefnessor(elementUtils, classElement);
                mProxyMap.put(fullClassName, briefnessor);
            }

        }
    }


    @Override
    protected void processLayout(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsWithBind = roundEnv.getElementsAnnotatedWith(BindLayout.class);
        for (Element element : elementsWithBind) {
            if (!checkAnnotationValid(element, BindLayout.class)) continue;
            String fullClassName = element.asType().toString();
            Briefnessor briefnessor = mProxyMap.get(fullClassName);
            if (briefnessor == null) {
                briefnessor = new Briefnessor(elementUtils, (TypeElement) element);
                mProxyMap.put(fullClassName, briefnessor);
            }
        }
    }


    @Override
    protected void process() {
        for (String key : mProxyMap.keySet()) {
            Briefnessor briefnessor = mProxyMap.get(key);
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        briefnessor.getProxyClassFullName(),
                        briefnessor.getTypeElement()
                );
                if (!inited) {
                    packages = StringUtil.findPackage(jfo.toUri().getPath());
                    if (packages == null)
                        error(briefnessor.getTypeElement(), "Unable to find module package");
                    else
                        Logger.v("find package:" + packages);
                    try {

                        JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(packages + Constant.dot + Constant.briefnessInjector, briefnessor.getTypeElement());
                        //找到当前module
                        buidPath = StringUtil.findBuildDir(fileObject.toUri().getPath());
                        Logger.v("find build directory:" + buidPath);
                        //ViewInjector
                        JavaInjector injector = new JavaInjector();
                        injector.witeCode(buidPath, packages);
                        //BriefnessInjector
                        Writer openWriter = fileObject.openWriter();
                        openWriter.write(injector.getBriefnessInjectorCode(buidPath, packages));
                        openWriter.flush();
                        openWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    inited = true;
                }
                Writer writer = jfo.openWriter();
                writer.write(briefnessor.generateJavaCode(buidPath, packages));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                error(briefnessor.getTypeElement(), "Unable to write injector for type %s: %s", briefnessor.getTypeElement(), e.getMessage());
                e.printStackTrace();
            }
        }

    }


}
