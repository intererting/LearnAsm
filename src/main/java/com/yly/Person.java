package com.yly;

import java.util.concurrent.TimeUnit;

/**
 * @author yiliyang
 * @version 1.0
 * @date 2020/10/16 上午10:56
 * @since 1.0
 */
public class Person {
//    public final static String name = "yuliyang";

//    public void test() {
//        System.out.println("test in Person");
//    }
//
//    public String test(int a) {
//        if (a > 3) {
//            return ">3";
//        } else {
//            return "<3";
//        }
//
//    }

//    public void test1() {
//        System.out.println("test1 in Person");
//    }
//
//    public void testException() {
//        try {
//            throw new RuntimeException();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void testChangeMethod() {
//        System.out.println("after new");
//        PersonInnerClass innerClass = new PersonInnerClass();
//    }

//    public void measureTime() throws Exception {
//        int a = 3;
//        TimeUnit.SECONDS.sleep(1);
//        int b = a + 1;
//    }

    /**
     * 0: iconst_3
     * 1: istore_1
     * 2: iload_1
     * 3: iconst_0
     * 4: iadd
     * 5: istore_1
     * 6: return
     */
    public void removeAddZero() {
        int a = 3;
        int b = a + 1 + 0;
        System.out.println(b);
    }
}

class PersonInnerClass {

}

class ChangedPersonInnerClass {

}
