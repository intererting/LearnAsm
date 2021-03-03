package com.yly.treeapi;

import com.yly.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/3 下午1:54
 * @since 1.0
 */
public class AddTimerTransformer {

    public void transform(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if ("<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
                continue;
            }
            InsnList insns = mn.instructions;
            if (insns.size() == 0) {
                continue;
            }
            for (AbstractInsnNode in : insns) {
                int op = in.getOpcode();
                if ((op >= Opcodes.IRETURN && op <= Opcodes.RETURN) || op == Opcodes.ATHROW) {
                    InsnList il = new InsnList();
                    il.add(new FieldInsnNode(Opcodes.GETSTATIC, cn.name, "timer", "J"));
                    il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J"));
                    il.add(new InsnNode(Opcodes.LADD));
                    il.add(new FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "timer", "J"));
                    insns.insert(in.getPrevious(), il);
                }
            }
            InsnList il = new InsnList();
            il.add(new FieldInsnNode(Opcodes.GETSTATIC, cn.name, "timer", "J"));
            il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J"));
            il.add(new InsnNode(Opcodes.LSUB));
            il.add(new FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "timer", "J"));
            insns.insert(il);
            mn.maxStack += 4;
        }
        int acc = Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
        cn.fields.add(new FieldNode(acc, "timer", "J", null, null));
    }

    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader("com.yly.treeapi.TreeModel");
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        AddTimerTransformer at = new AddTimerTransformer();
        at.transform(cn);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cn.accept(classWriter);
        Utils.copyClassToFile("com/yly", "TreeModel.class", classWriter.toByteArray());
    }
}