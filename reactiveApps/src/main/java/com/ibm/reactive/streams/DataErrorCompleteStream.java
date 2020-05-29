package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class DataErrorCompleteStream {
    public static void main(String[] args) {

        //create stream from scratch
        Observable<String> stream = Observable.create(source -> {
            //push data into stream
            source.onNext("Subramanian");// emit data event
            source.onNext("Ram");


            //push error
           // source.onError(new Exception("Something went wrong")); //emit error event
            source.onNext("Karthik");
            source.onComplete(); // emit complete event

        });

        //subscriber :three signal -data,error,complete
        //stream.subscribe(data -> System.out.println(data), err -> System.out.println(err), () -> System.out.println("done!"));
        stream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));
    }
}
