package com.wingjay.titan_android.jni;

public class Counter {
    static {
        System.loadLibrary("test-lib");
    }

    public int count = 0;
    public static String staticCount = "static: before jni";
    private int innerCount = 20;

    public native int increase();
    public native void processStatic();
    public native void innerIncrease();

    public native int sumOf(int[] array);
    public native Person TenYearsYounger(Person oldPerson);

    public native String autoRegisterFun(String input);

    class Person {
        int age;
        String name;
        public Person(int age_, String name_) {
            age = age_;
            name = name_;
        }

        @Override
        public String toString() {
            return "age: " + age + " ; name: " + name;
        }
    }
}
