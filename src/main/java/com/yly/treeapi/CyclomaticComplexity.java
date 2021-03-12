package com.yly.treeapi;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.*;

import java.io.IOException;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/5 下午3:35
 * @since 1.0
 * 计算复杂度
 */
public class CyclomaticComplexity {
    public static int getCyclomaticComplexity(String owner, MethodNode mn) throws AnalyzerException {
        Analyzer<BasicValue> a = new Analyzer<>(new BasicInterpreter()) {
            protected Frame<BasicValue> newFrame(int nLocals, int nStack) {
                return new Node<>(nLocals, nStack);
            }

            protected Frame<BasicValue> newFrame(Frame<? extends BasicValue> src) {
                return new Node<>(src);
            }

            protected void newControlFlowEdge(int src, int dst) {
                System.out.println("src  " + src);
                System.out.println(dst);
                Node<BasicValue> s = (Node<BasicValue>) getFrames()[src];
                s.successors.add((Node<BasicValue>) getFrames()[dst]);
            }
        };
        a.analyze(owner, mn);
        Frame<BasicValue>[] frames = a.getFrames();
        int edges = 0;
        int nodes = 0;
        for (Frame<BasicValue> frame : frames) {
            if (frame != null) {
                edges += ((Node<BasicValue>) frame).successors.size();
                nodes += 1;
            }
        }
        return edges - nodes + 2;
    }

    public static void main(String[] args) throws IOException, AnalyzerException {
        ClassReader classReader = new ClassReader("com.yly.treeapi.TreeModel");
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("setF")) {
                System.out.println(getCyclomaticComplexity("com.yly.treeapi.TreeModel", methodNode));
            }
        }
    }
}
