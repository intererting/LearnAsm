package com.yly;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

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

    //BIPUSH, SIPUSH,NEWARRAY
//    @Override
//    public void visitIntInsn(int opcode, int operand) {
//        mv.visitIntInsn(opcode, operand);
//    }
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

class RemoveZeroMethodVisitor extends PatternMethodAdapter {

    private final static int SEEN_ICONST_0 = 1;

    public RemoveZeroMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM9, methodVisitor);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == ICONST_0 && state == SEEN_NOTHING) {
            state = SEEN_ICONST_0;
            return;
        }
        if (state == SEEN_ICONST_0) {
            if (opcode == IADD) {
                state = SEEN_NOTHING;
                return;
            }
        }
        mv.visitInsn(opcode);
    }
}
