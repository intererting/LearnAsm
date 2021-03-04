package com.yly.treeapi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/4 下午3:20
 * @since 1.0
 */
public class MyClassAdapter extends ClassNode {

    @Override
    public void visitEnd() {
        super.visitEnd();
//        accept();
    }
}
