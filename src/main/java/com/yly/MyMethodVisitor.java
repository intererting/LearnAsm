package com.yly;

import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 下午4:36
 * @since 1.0
 */
public class MyMethodVisitor extends MethodVisitor {

    public MyMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM9, methodVisitor);
    }

    public static void main(String[] args) {
        try {
            ClassReader classReader = new ClassReader("com.yly.Person");
            ClassWriter classWriter = new ClassWriter(classReader, 0);
            ClassVisitor classVisitor = new ClassVisitor(ASM9, classWriter) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
//                    if (mv != null) {
//                    }
//                    return mv;
                    if (name.equals("testChangeMethod")) {
                        return new MyMethodVisitor(mv);
                    }
                    return mv;
                }
            };
            classReader.accept(classVisitor, 0);
            Utils.copyClassToFile("com/yly", "Person.class", classWriter.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void visitTypeInsn(int opcode, String type) {
        //动态修改new返回的类型
        if (opcode == Opcodes.NEW && type.equals("com/yly/PersonInnerClass")) {
            super.visitTypeInsn(opcode, "com/yly/ChangedPersonInnerClass");
        } else {
            super.visitTypeInsn(opcode, type);
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
