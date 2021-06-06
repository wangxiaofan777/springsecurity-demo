package com.wxf.springsecuritydemo;

import java.util.concurrent.TimeUnit;

public class TestDeadLock {

    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        new Thread(()->{
            synchronized (A){
                try {
                    TimeUnit.SECONDS.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    System.out.println("1");
                }
            }
        }).start();

        new Thread(()->{
            synchronized (B){
                synchronized (A) {
                    System.out.println(2);
                }
            }
        }).start();

    }
}
