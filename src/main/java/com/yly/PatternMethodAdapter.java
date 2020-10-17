package com.yly;

import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public abstract class PatternMethodAdapter extends MethodVisitor {

    public static void main(String[] args) {
        try {
            ClassReader classReader = new ClassReader("com.yly.Person");
            ClassWriter classWriter = new ClassWriter(classReader, 0);
            ClassVisitor classVisitor = new RemoveZeroClassVisitor(classWriter);
            classReader.accept(classVisitor, 0);
            Utils.copyClassToFile("com/yly", "Person.class", classWriter.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected final static int SEEN_NOTHING = 0;
    protected              int state;

    public PatternMethodAdapter(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {
        visitInsn();
        mv.visitInsn(opcode);
    }

    //BIPUSH, SIPUSH,NEWARRAY
//    @Override
    public void visitIntInsn(int opcode, int operand) {
        visitInsn();
        mv.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        visitInsn();
        mv.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitLabel(Label label) {
        visitInsn();
        mv.visitLabel(label);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        visitInsn();
        mv.visitMaxs(maxStack, maxLocals);
    }

    protected abstract void visitInsn();
}

class RemoveZeroMethodVisitor extends PatternMethodAdapter {

    private final static int SEEN_ICONST_0 = 1;

    public RemoveZeroMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM9, methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {
        if (state == SEEN_ICONST_0) {
            if (opcode == IADD) {
                state = SEEN_NOTHING;
                return;
            }
        }
        visitInsn();
        if (opcode == ICONST_0) {
            state = SEEN_ICONST_0;
            return;
        }

        mv.visitInsn(opcode);
    }

    @Override
    protected void visitInsn() {
        if (state == SEEN_ICONST_0) {
            mv.visitInsn(ICONST_0);
        }
        state = SEEN_NOTHING;
    }
}

class RemoveZeroClassVisitor extends ClassVisitor {

    public RemoveZeroClassVisitor(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if (methodVisitor != null && name.equals("removeAddZero")) {
            return new RemoveZeroMethodVisitor(methodVisitor);
        }
        return methodVisitor;
    }
}

