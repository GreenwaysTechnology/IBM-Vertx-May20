package com.ibm.reactive.streams;

import io.reactivex.Completable;

public class CompletableDemo {
    public static void main(String[] args) throws InterruptedException  {

        Completable.complete().subscribe(() -> System.out.println("done!"));
        Thread.sleep(5000);
    }
}
