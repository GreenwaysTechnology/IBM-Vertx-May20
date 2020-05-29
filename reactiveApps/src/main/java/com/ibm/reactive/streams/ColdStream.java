package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class ColdStream {
    public static void main(String[] args) throws InterruptedException {

        //Source Stream : stream broad cast data to multiple subscribers
        Observable<Integer> coldStream = Observable.create(source -> {
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                source.onNext(i);
            }
            source.onComplete();
        });
        //subscriber 1
        coldStream.subscribe(data->{
            System.out.println("Subramanian's Data " + data);
        },System.out::println,()->System.out.println("Subramanian done"));

        //subscriber 2
        coldStream.subscribe(data->{
            System.out.println("James's Data " + data);
        },System.out::println,()->System.out.println("James done"));

        //Late joining
        Thread.sleep(5000);
        //subscriber 3
        coldStream.subscribe(data->{
            System.out.println("Karthik's Data " + data);
        },System.out::println,()->System.out.println("Kathik done"));


    }
}
