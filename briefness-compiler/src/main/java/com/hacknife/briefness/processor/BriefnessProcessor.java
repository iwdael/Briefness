package com.hacknife.briefness.processor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.validator.chunks.ModifierValidator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.hacknife.briefness.AbsJavaInfo;
import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;
import com.hacknife.briefness.BindViews;
import com.hacknife.briefness.JavaInjector;
import com.hacknife.briefness.JavaInfo;
import com.hacknife.briefness.databinding.JavaLayout;
import com.hacknife.briefness.util.Logger;
import com.google.auto.service.AutoService;

import java.io.File;
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
    static final String briefnessInjector = "com.hacknife.briefness.BriefnessInjector";
    boolean process = false;
    String path;
    String build = "build";

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
            BindLayout bindViewAnnotation = element.getAnnotation(BindLayout.class);
            proxyInfo.bindLayout.add(new JavaLayout(bindViewAnnotation.value()));
        }
    }


    @Override
    protected void process() {
        for (String key : mProxyMap.keySet()) {
            AbsJavaInfo proxyInfo = mProxyMap.get(key);
            if (!process) {
                JavaInjector injector = new JavaInjector();

                try {
                    JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(briefnessInjector, proxyInfo.getTypeElement());
                    //找到当前module
                    {
                        File file = new File(fileObject.toUri().getPath());
                        while (!file.getParentFile().getPath().endsWith(build)) {
                            file = file.getParentFile();
                        }
                        path = file.getParentFile().getParentFile().getPath();
                    }
                    injector.witeCode(path);
                    Writer openWriter = fileObject.openWriter();
                    openWriter.write(injector.getBriefnessInjectorCode(path));
                    openWriter.flush();
                    openWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                process = true;
            }
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        proxyInfo.getProxyClassFullName(),
                        proxyInfo.getTypeElement()
                );
                Logger.v(jfo.toUri().getPath());

                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateJavaCode(path));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                error(proxyInfo.getTypeElement(), "Unable to write injector for type %s: %s", proxyInfo.getTypeElement(), e.getMessage());
                e.printStackTrace();
            }
        }

    }


    public class FieldVisitor extends VoidVisitorAdapter<Void> {


        @Override
        public void visit(FieldDeclaration n, Void arg) {
            n.setModifier(Modifier.STATIC, false);
//            Logger.v(n.toString());
        }
    }

}
