package com.yly.treeapi;

import com.yly.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.List;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/3 上午11:29
 * @since 1.0
 */
public class OptimizeJumpTransformer {
    public void transform(MethodNode mn) {
        InsnList insns = mn.instructions;
        for (AbstractInsnNode in : insns) {
            if (in instanceof JumpInsnNode) {
                LabelNode label = ((JumpInsnNode) in).label;
                AbstractInsnNode target;
// while target == goto l, replace label with l
                while (true) {
                    target = label;
                    while (target != null && target.getOpcode() < 0) {
                        target = target.getNext();
                    }
                    if (target != null && target.getOpcode() == Opcodes.GOTO) {
                        label = ((JumpInsnNode) target).label;
                    } else {
                        break;
                    }
                }
// update target
                ((JumpInsnNode) in).label = label;
// if possible, replace jump with target instruction
                if (in.getOpcode() == Opcodes.GOTO && target != null) {
                    int op = target.getOpcode();
                    if ((op >= Opcodes.IRETURN && op <= Opcodes.RETURN) || op == Opcodes.ATHROW) {
// replace ’in’ with clone of ’target’
                        insns.set(in, target.clone(null));
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ClassReader cr = new ClassReader("com.yly.treeapi.TreeModel");
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);
        OptimizeJumpTransformer at = new OptimizeJumpTransformer();
        List<MethodNode> methodNodes = cn.methods;
        for (MethodNode mn : methodNodes) {
            if (mn.name.equals("setF")) {
                at.transform(mn);
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cn.accept(classWriter);
        Utils.copyClassToFile("com/yly", "TreeModel.class", classWriter.toByteArray());
    }
}