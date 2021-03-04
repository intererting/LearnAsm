package com.yly.treeapi;

import com.yly.Utils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import org.objectweb.asm.Opcodes.*;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_8;

/**
 * tree api效率更低
 */
public class CreateClass {
    public static void main(String[] args) {
        ClassNode classNode = new ClassNode();
        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC;
        classNode.name = "com/yly/TreeApi";
        classNode.superName = "java/lang/Object";

        /*
         *    final int access,
         *       final String name,
         *       final String descriptor,
         *       final String signature,
         *       final Object value
         */
        FieldNode fieldNode = new FieldNode(
                ACC_PUBLIC,
                "f",
                "I",
                null,
                null
        );
        classNode.fields.add(fieldNode);

        /*
         *  final int access,
         *       final String name,
         *       final String descriptor,
         *       final String signature,
         *       final String[] exceptions
         */
        MethodNode methodNode = new MethodNode(
                ACC_PUBLIC,
                "test",
                "(I)V",
                null,
                null
        );

        classNode.methods.add(methodNode);

        InsnList il = methodNode.instructions;
        il.add(new VarInsnNode(Opcodes.ILOAD, 1));
        LabelNode label = new LabelNode();
        il.add(new JumpInsnNode(Opcodes.IFLT, label));
        il.add(new VarInsnNode(Opcodes.ALOAD, 0));
        il.add(new VarInsnNode(Opcodes.ILOAD, 1));
        il.add(new FieldInsnNode(Opcodes.PUTFIELD, "com/yly/TreeApi", "f", "I"));
        LabelNode end = new LabelNode();
        il.add(new JumpInsnNode(Opcodes.GOTO, end));
        il.add(label);
//        il.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        il.add(new TypeInsnNode(Opcodes.NEW, "java/lang/IllegalArgumentException"));
        il.add(new InsnNode(Opcodes.DUP));
        il.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "()V"));
        il.add(new InsnNode(Opcodes.ATHROW));
        il.add(end);
//        il.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        il.add(new InsnNode(Opcodes.RETURN));
//        methodNode.maxStack = 2;
//        methodNode.maxLocals = 2;

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        Utils.copyClassToFile("com/yly", "TreeApi.class", classWriter.toByteArray());

    }
}
