package com.yly.treeapi;

import com.yly.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/3 下午2:02
 * @since 1.0
 */
public class RemoveGetFieldPutFieldTransformer {

    public void transform(MethodNode mn) {
        InsnList insns = mn.instructions;
        Iterator i = insns.iterator();
        while (i.hasNext()) {
            AbstractInsnNode i1 = (AbstractInsnNode) i.next();
            if (isALOAD0(i1)) {
                AbstractInsnNode i2 = getNext(i);
                if (i2 != null && isALOAD0(i2)) {
                    AbstractInsnNode i3 = getNext(i);
                    while (i3 != null && isALOAD0(i3)) {
                        i1 = i2;
                        i2 = i3;
                        i3 = getNext(i);
                    }
                    if (i3 != null && i3.getOpcode() == Opcodes.GETFIELD) {
                        AbstractInsnNode i4 = getNext(i);
                        if (i4 != null && i4.getOpcode() == Opcodes.PUTFIELD) {
                            if (sameField(i3, i4)) {
                                insns.remove(i1);
                                insns.remove(i2);
                                insns.remove(i3);
                                insns.remove(i4);
                            }
                        }
                    }
                }
            }
        }
    }

    private static AbstractInsnNode getNext(Iterator i) {
        while (i.hasNext()) {
            AbstractInsnNode in = (AbstractInsnNode) i.next();
            if (!(in instanceof LineNumberNode)) {
                return in;
            }
        }
        return null;
    }

    private static boolean isALOAD0(AbstractInsnNode i) {
        return i.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) i).var == 0;
    }

    private static boolean sameField(AbstractInsnNode i, AbstractInsnNode j) {
        return ((FieldInsnNode) i).name.equals(((FieldInsnNode) j).name);
    }

    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader("com.yly.treeapi.TreeModel");
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        RemoveGetFieldPutFieldTransformer at = new RemoveGetFieldPutFieldTransformer();
        for (MethodNode methodNode : cn.methods) {
            at.transform(methodNode);
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cn.accept(classWriter);
        Utils.copyClassToFile("com/yly", "TreeModel.class", classWriter.toByteArray());
    }
}