package com.yly;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 下午3:24
 * @since 1.0
 */
//开始编辑方法
//        mv.visitCode();
// ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET
//        mv.visitVarInsn();
//GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
//        mv.visitFieldInsn();
//Visits a zero operand instruction.不含操作数的指令
//        mv.visitInsn();
//设置栈帧和局部变量表大小
//        mv.visitMaxs();
//编辑完毕
//        mv.visitEnd();
//IFEQ,
//   *     IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT,
//   *     IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL
//        mv.visitJumpInsn();

//        mv.visitLabel();
//        mv.visitFrame();
//NEW, ANEWARRAY, CHECKCAST or INSTANCEOF
//        mv.visitTypeInsn();
//INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or INVOKEINTERFACE
//        mv.visitMethodInsn();
public class GenerateMethod {

    public static void main(String[] args) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(V1_8,
                ACC_PUBLIC
                , "com/yly/MethodGenerate"
                , null,
                "java/lang/Object"
                , null);
        /*
         *       final int access,
         *       final String name,
         *       final String descriptor,
         *       final String signature,
         *       final String[] exceptions
         */
//        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "test", "(I)Ljava/lang/String;", null, null);

        //label 用于分支，visitlabel代表进入一个立即执行的分支
//        mv.visitCode();
//        mv.visitVarInsn(ILOAD, 1);
//        mv.visitInsn(ICONST_3);
//        Label label = new Label();
//        mv.visitJumpInsn(IF_ICMPLE, label);
//        mv.visitLabel(label);
//        mv.visitLdcInsn(">3");
//        mv.visitInsn(ARETURN);
//        mv.visitLabel(label);
//        mv.visitLdcInsn("<3");
//        mv.visitInsn(ARETURN);
////        mv.visitMaxs(2, 2);
//        mv.visitEnd();
//        Utils.copyClassToFile("com/yly", "MethodGenerate.class", writer.toByteArray());

        ///////////////GOTO和异常的使用
        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "setF", "(I)V", null, null);
        writer.visitField(ACC_PRIVATE, "f", "I", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 1);
        Label label = new Label();
        mv.visitJumpInsn(IFLT, label);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitFieldInsn(PUTFIELD, "com/yly/MethodGenerate", "f", "I");
        Label end = new Label();
        mv.visitJumpInsn(GOTO, end);
        mv.visitLabel(label);
//        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/lang/IllegalArgumentException", "<init>", "()V", false);
        mv.visitInsn(ATHROW);
        mv.visitLabel(end);
//        mv.visitFrame(F_SAME, 0, null, 0, null);
        mv.visitInsn(RETURN);
//        mv.visitMaxs(2, 2);
        mv.visitEnd();
        Utils.copyClassToFile("com/yly", "MethodGenerate.class", writer.toByteArray());


    }
}
