package com.yly;

import org.objectweb.asm.*;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 上午10:44
 * @since 1.0
 */
public class ClassVisitorDemo extends ClassVisitor {

    private ClassVisitorDemo() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //version 55
        //0
        //java/lang/String
        //Ljava/lang/Object;Ljava/io/Serializable;Ljava/lang/Comparable<Ljava/lang/String;>;Ljava/lang/CharSequence;
        //java/lang/Object
        //java/io/Serializable
        //java/lang/Comparable
        //java/lang/CharSequence

//        System.out.println("version " + version);
//        System.out.println(access & Opcodes.ACC_PUBLIC);
//        System.out.println(name);
//        System.out.println(signature);
//        System.out.println(superName);
//        for (String sign : interfaces) {
//            System.out.println(sign);
//        }
    }

    @Override
    public void visitSource(String source, String debug) {
//        String.java
//        System.out.println(source);
        super.visitSource(source, debug);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
//        System.out.println(attribute.type);
        super.visitAttribute(attribute);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//        System.out.println(access & Opcodes.ACC_PRIVATE);
//        System.out.println(name);
//        System.out.println(descriptor);
//        System.out.println(value);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        //<init>
//()V
//test
//()V
//<clinit>
//()V

//        System.out.println(name);
//        System.out.println(descriptor);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    public static void main(String[] args) {
        try {
//            ClassReader classReader = new ClassReader("java.lang.String");
            ClassReader classReader = new ClassReader("com.yly.Person");
            classReader.accept(new ClassVisitorDemo(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

