package com.yly;

import org.objectweb.asm.Type;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 下午2:16
 * @since 1.0
 */
public class LearnType {
    public static void main(String[] args) {
        System.out.println(Type.getType(Person.class).getDescriptor());//Lcom/yly/Person;
    }
}
