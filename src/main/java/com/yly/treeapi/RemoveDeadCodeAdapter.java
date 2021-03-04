package com.yly.treeapi;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/4 下午5:05
 * @since 1.0
 */
public class RemoveDeadCodeAdapter extends MethodVisitor {
    String        owner;
    MethodVisitor next;

    public RemoveDeadCodeAdapter(String owner, int access, String name, String desc, MethodVisitor mv) {
        super(ASM9, new MethodNode(access, name, desc, null, null));
        this.owner = owner;
        next = mv;
    }

    @Override
    public void visitEnd() {
        MethodNode mn = (MethodNode) mv;
        Analyzer<BasicValue> a = new Analyzer<>(new BasicInterpreter());
        try {
            a.analyze(owner, mn);
            Frame<BasicValue>[] frames = a.getFrames();
            AbstractInsnNode[] insns = mn.instructions.toArray();
            for (int i = 0; i < frames.length; ++i) {
                if (frames[i] == null && !(insns[i] instanceof LabelNode)) {
                    mn.instructions.remove(insns[i]);
                }
            }
        } catch (AnalyzerException ignored) {
        }
        mn.accept(next);
    }
}
