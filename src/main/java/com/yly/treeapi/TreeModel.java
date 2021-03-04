package com.yly.treeapi;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/3 上午11:28
 * @since 1.0
 */
public class TreeModel {
    private int f;

    public int getF() {
        return f;
    }

    public void setF(int f) {
        if (f >= 0) {
            this.f = f;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
