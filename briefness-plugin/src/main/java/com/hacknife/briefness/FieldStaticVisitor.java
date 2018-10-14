package com.hacknife.briefness;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class FieldStaticVisitor extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(FieldDeclaration n, Void arg) {
        n.setModifier(Modifier.STATIC, true);
        n.setModifier(Modifier.FINAL, true);
    }
}
