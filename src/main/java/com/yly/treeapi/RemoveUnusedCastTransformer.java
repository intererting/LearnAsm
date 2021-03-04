package com.yly.treeapi;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.*;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/4 下午5:15
 * @since 1.0
 */
public class RemoveUnusedCastTransformer {
    String owner;

    public RemoveUnusedCastTransformer(String owner) {
        this.owner = owner;
    }

    public void transform(MethodNode mn) {
        Analyzer<BasicValue> a = new Analyzer<>(new SimpleVerifier());
        try {
            a.analyze(owner, mn);
            Frame<BasicValue>[] frames = a.getFrames();
            AbstractInsnNode[] insns = mn.instructions.toArray();
            for (int i = 0; i < insns.length; ++i) {
                AbstractInsnNode insn = insns[i];
                if (insn.getOpcode() == Opcodes.CHECKCAST) {
                    Frame f = frames[i];
                    if (f != null && f.getStackSize() > 0) {
                        Object operand = f.getStack(f.getStackSize() - 1);
                        Class<?> to = getClass(((TypeInsnNode) insn).desc);
                        Class<?> from = getClass(((BasicValue) operand).getType());
                        if (to.isAssignableFrom(from)) {
                            mn.instructions.remove(insn);
                        }
                    }
                }
            }
        } catch (AnalyzerException ignored) {
        }
    }

    private static Class<?> getClass(String desc) {
        try {
            return Class.forName(desc.replace("/", "."));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private static Class<?> getClass(Type t) {
        if (t.getSort() == Type.OBJECT) {
            return getClass(t.getInternalName());
        }
        return getClass(t.getDescriptor());
    }
}