package com.yly;

import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ASM9;

/**
 * 练习添加代码测试方法运行时间
 * public class C {
 * *    public static long timer;
 * <p>
 * *   public void m() throws Exception {
 * *        timer -= System.currentTimeMillis();
 * *       Thread.sleep(100);
 * *        timer += System.currentTimeMillis();
 * <p>
 * }
 * }
 */
public class MeasureMethodTime {
    public static void main(String[] args) {

        try {
            ClassReader classReader = new ClassReader("com.yly.Person");
            ClassWriter classWriter = new ClassWriter(classReader, 0);
            ClassVisitor classVisitor = new MeasureTimeClassVisitor(classWriter);
            classReader.accept(classVisitor, 0);
            Utils.copyClassToFile("com/yly", "Person.class", classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class MeasureTimeClassVisitor extends ClassVisitor {

    private boolean isInterface;
    private String  owner;

    public MeasureTimeClassVisitor(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (!isInterface && methodVisitor != null && name.equals("measureTime")) {
            return new MeasureTimeVisitor(owner, methodVisitor);
        }
        return methodVisitor;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public void visitEnd() {
//        super.visitEnd();
        if (!isInterface) {
            //添加timer
            /*
             *       final int access,
             *       final String name,
             *       final String descriptor,
             *       final String signature,
             *       final Object value
             */
            FieldVisitor fieldVisitor = cv.visitField(
                    ACC_PUBLIC + ACC_STATIC,
                    "timer",
                    "J"
                    , null
                    , null
            );
            if (fieldVisitor != null) {
                fieldVisitor.visitEnd();
            }
            cv.visitEnd();
        }
    }
}

class MeasureTimeVisitor extends MethodVisitor {

    private String owner;

    public MeasureTimeVisitor(String owner, MethodVisitor methodVisitor) {
        super(ASM9, methodVisitor);
        this.owner = owner;
    }

    @Override
    public void visitCode() {
//        super.visitCode();
        mv.visitCode();
        /*
         *  final int opcode,
         *  final String owner,
         *  final String name,
         *  final String descriptor
         */
        mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
        /*
         *      final int opcode,
         *       final String owner,
         *       final String name,
         *       final String descriptor,
         *       final boolean isInterface
         */
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis",
                "()J", false);

        mv.visitInsn(LSUB);

        mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode >= IRETURN && opcode <= RETURN || opcode == ATHROW) {
            //遇到异常或者正常返回
            mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis",
                    "()J", false);
            mv.visitInsn(LADD);
            mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 4, maxLocals);
    }
}

