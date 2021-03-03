package com.yly.treeapi;

import com.yly.Utils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

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
                "version",
                "Ljava/lang/String;",
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
                "()V",
                null,
                null
        );

        classNode.methods.add(methodNode);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        Utils.copyClassToFile("com/yly", "TreeApi.class", classWriter.toByteArray());

    }
}
