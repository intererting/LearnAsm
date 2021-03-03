package com.yly;

import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.Opcodes.*;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 上午11:07
 * @since 1.0
 */
public class ClassWriterDemo {

    public static void main(String[] args) {

        //自动计算栈帧大小和映射，以下两个方法将会失效,但是还是要手动调用visitMax方法,参数任意被忽略
        //MethodVisitor#visitFrame 20%效率影响
        //MethodVisitor#visitMaxs 10%效率影响
        ClassWriter classWriter = new ClassWriter(COMPUTE_FRAMES);
        /*
         *       final int version,
         *       final int access,
         *       final String name,
         *       final String signature,
         *       final String superName,
         *       final String[] interfaces
         */
        classWriter.visit(V1_8,
                ACC_PUBLIC + ACC_INTERFACE + ACC_ABSTRACT
                , "pkg/Comparable"
                , null,
                "java/lang/Object",
                null);
//        new String[]{"pkg/Measurable"}
        /*
         *   final int access,
         *       final String name,
         *       final String descriptor,
         *       final String signature,
         *       final Object value
         */
        classWriter.visitField(ACC_PUBLIC + ACC_STATIC + ACC_FINAL
                , "LEVEL"
                , "I"
                , null
                , 100);
        classWriter.visitEnd();

        /*
         *  final int access,
         *       final String name,
         *       final String descriptor,
         *       final String signature,
         *       final String[] exceptions
         */
        classWriter.visitMethod(
                ACC_PUBLIC + ACC_ABSTRACT
                , "compareTo"
                , "(Ljava/lang/Object;)I"
                , null
                , null);

        classWriter.visitEnd();
        classWriter.visitEnd();

        byte[] data = classWriter.toByteArray();

//        Utils.copyClassToFile("pkg", "Comparable.class", data);
        //自定义ClassLoader加载
        MyClassLoader myClassLoader = new MyClassLoader();
        Class c = myClassLoader.defineClass("pkg.Comparable", data);
        try {
            System.out.println(c.getDeclaredField("LEVEL").get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}