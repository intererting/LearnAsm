package com.yly.example;

import com.yly.Utils;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/5 上午9:24
 * @since 1.0
 */
public class InlineMethodTransformer {

    private static List<AbstractInsnNode> nodeList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ClassReader classReader = new ClassReader("com.yly.example.InlineMethod");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor, 0);
        Utils.copyClassToFile("com/yly", "InlineMethod.class", classWriter.toByteArray());
    }

    private static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor(ClassVisitor classVisitor) {
            super(ASM9, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (name.equals("innerMethod")) {
                return new MyInnerMethodNode(mv, access, name, descriptor, signature, exceptions);
            }
            if (name.equals("outerMethod")) {
                return new MyOuterMethodVisitor(mv);
            }
            return mv;
        }
    }

    private static class MyOuterMethodVisitor extends MethodVisitor {

        public MyOuterMethodVisitor(MethodVisitor methodVisitor) {
            super(ASM9, methodVisitor);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            //把从局部变量表中的操作排除，这只是测试，如果这里是为其他变量赋值，或者是调用其他方法从局部变量表中获取
            //那就要排除，这里只是为了测试InlineMethod这种简单方法

            //这个地方要比较方法名字和方法调用前参数的数量是否对应
            if (opcode >= Opcodes.ILOAD && opcode <= Opcodes.SALOAD) {
                return;
            }
            super.visitFieldInsn(opcode, owner, name, descriptor);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (opcode != Opcodes.INVOKESTATIC) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                return;
            }
            for (AbstractInsnNode insertNode : nodeList) {
                insertNode.accept(mv);
            }
        }
    }


    private static class MyInnerMethodNode extends MethodNode {

        private MethodVisitor mv;

        public MyInnerMethodNode(MethodVisitor mv, int access, String name, String descriptor, String signature, String[] exceptions) {
            super(ASM9, access, name, descriptor, signature, exceptions);
            this.mv = mv;
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
            nodeList.clear();
            //将需要内联的方法全部保存起来
            for (AbstractInsnNode insnNode : instructions) {
                nodeList.add(insnNode);
            }
            //满足链调用
            accept(mv);
        }
    }
}
