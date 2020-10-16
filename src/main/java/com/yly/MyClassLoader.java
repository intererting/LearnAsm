package com.yly;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 下午1:39
 * @since 1.0
 */
public class MyClassLoader extends ClassLoader {
    Class defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}
