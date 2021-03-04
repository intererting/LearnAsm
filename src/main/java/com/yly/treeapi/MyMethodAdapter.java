package com.yly.treeapi;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/4 下午4:35
 * @since 1.0
 */
//MethodVisitor和MethodNode的相互转换
public class MyMethodAdapter extends MethodVisitor {
    MethodVisitor next;

    public MyMethodAdapter(int access, String name, String desc,
                           String signature, String[] exceptions, MethodVisitor mv) {
        super(ASM9, new MethodNode(access, name, desc, signature, exceptions));
        next = mv;
    }

    @Override
    public void visitEnd() {
        MethodNode mn = (MethodNode) mv;
// put your transformation code here
        mn.accept(next);
    }
}