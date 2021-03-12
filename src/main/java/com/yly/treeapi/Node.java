package com.yly.treeapi;

import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Value;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/5 下午3:36
 * @since 1.0
 */
class Node<V extends Value> extends Frame<V> {
    Set<Node<V>> successors = new HashSet<>();

    public Node(int nLocals, int nStack) {
        super(nLocals, nStack);
    }

    public Node(Frame<? extends V> src) {
        super(src);
    }
}