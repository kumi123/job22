package com.kuang.tvolatile;

/**
 * @author kumi
 * @version V1.0
 * @program: juc_kumi
 * @create 2021-02-28-22:06
 * @Description: TODO
 */
/**
 * 不保证原子性
 * number <=2w
 *
 */
public class VDemo03{

    private static volatile int number = 0;

    public static void add(){
        number++;
        //++ 不是一个原子性操作，是两个~3个操作
        //
    }

    public static void main(String[] args) {
        //理论上number  === 20000

        for (int i = 1; i <= 20; i++) {
            new Thread(()->{
                for (int j = 1; j <= 1000 ; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2){
            //main  gc
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+",num="+number);
    }
}
