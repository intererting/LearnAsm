package com.yly;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 下午1:22
 * @since 1.0
 */
public class TransforDemo {
    public static void main(String[] args) {

        try {
//            testRemoveName();
            testChangeName();
//            testAddField();
//            testAddMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testAddMethod() throws Exception {
        ClassReader reader = new ClassReader("com.yly.Person");
        //copy meta info reader to writer
        ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new AddMethodAdapter(writer, ACC_PUBLIC, "test2", "()V"), 0);
        byte[] data = writer.toByteArray();
        Utils.copyClassToFile("com/yly", "Person.class", data);
    }

    private static void testAddField() throws Exception {
        ClassReader reader = new ClassReader("com.yly.Person");
        //copy meta info reader to writer
        ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new AddFieldAdapter(writer, ACC_PUBLIC, "age", "I"), 0);
        byte[] data = writer.toByteArray();
        Utils.copyClassToFile("com/yly", "Person.class", data);
    }

    private static void testRemoveName() throws Exception {
        ClassReader reader = new ClassReader("com.yly.Person");
        //copy meta info reader to writer
        ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new RemoveMethodAdapter(writer), 0);
        byte[] data = writer.toByteArray();
        Utils.copyClassToFile("com/yly", "Person.class", data);
    }

    private static void testChangeName() throws Exception {
        ClassReader reader = new ClassReader("com.yly.Person");
        ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new ChangeNameAdapter(writer), 0);
        MyClassLoader classLoader = new MyClassLoader();
        byte[] data = writer.toByteArray();
        //名字已经被修改
        Class clazz = classLoader.defineClass("com.yly.Changed", data);
//        Utils.copyClassToFile("com/yly", "Person.class", data);
//        System.out.println(clazz.getDeclaredField("name").get(null));
    }

}

class ChangeNameAdapter extends ClassVisitor {
    ChangeNameAdapter(ClassVisitor cv) {
        super(ASM9, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println(name);//com/yly/Person
        super.visit(version, access, "com/yly/Changed", signature, superName, interfaces);
    }
}

class RemoveMethodAdapter extends ClassVisitor {

    RemoveMethodAdapter(ClassVisitor classVisitor) {
        super(ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals("test")) {
            return null;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}

class AddFieldAdapter extends ClassVisitor {
    private int     fAcc;
    private String  fName;
    private String  fDesc;
    private boolean isFieldPresent;

    AddFieldAdapter(ClassVisitor cv, int fAcc, String fName, String fDesc) {
        super(ASM9, cv);
        this.fAcc = fAcc;
        this.fName = fName;
        this.fDesc = fDesc;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (name.equals(fName)) {
            isFieldPresent = true;
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        //属性或者方法的添加最好在visitEnd方法完成
        if (!isFieldPresent) {
            FieldVisitor fv = cv.visitField(fAcc, fName, fDesc, null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        cv.visitEnd();
    }
}


class AddMethodAdapter extends ClassVisitor {
    private int     mAcc;
    private String  mName;
    private String  mDesc;
    private boolean isMethodPresent;

    AddMethodAdapter(ClassVisitor cv, int fAcc, String fName, String fDesc) {
        super(ASM9, cv);
        this.mAcc = fAcc;
        this.mName = fName;
        this.mDesc = fDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(mName)) {
            isMethodPresent = true;
        }
        return cv.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        //属性或者方法的添加最好在visitEnd方法完成
        if (!isMethodPresent) {
            MethodVisitor mv = cv.visitMethod(mAcc, mName, mDesc, null, null);
            if (mv != null) {
                mv.visitEnd();
            }
        }
        cv.visitEnd();
    }
}