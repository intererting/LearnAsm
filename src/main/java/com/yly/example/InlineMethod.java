package com.yly.example;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2021/3/5 上午9:18
 * @since 1.0
 * 联系内连方法
 */
public class InlineMethod {

    static void innerMethod(String name) {
        System.out.println(name);
    }

    static void outerMethod() {
        String name = "haha";
        innerMethod(name);
    }

    public static void main(String[] args) {
        InlineMethod.outerMethod();
    }
}
