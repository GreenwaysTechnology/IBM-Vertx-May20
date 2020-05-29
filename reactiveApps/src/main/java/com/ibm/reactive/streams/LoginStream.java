package com.ibm.reactive.streams;

import io.reactivex.Observable;

public class LoginStream {
    public static void main(String[] args) {

        //create stream from scratch
        Observable<String> stream = Observable.create(source -> {
            //biz logic
            String userName = "admin";
            String password = "admin";
            if (userName.equals("admin") && password.equals("admin")) {
                //emit success message
                source.onNext("Login Success");
                source.onComplete();
            } else {
                source.onError(new Exception("Login Failed"));
            }

        });

        //subscriber :three signal -data,error,complete
        //stream.subscribe(data -> System.out.println(data), err -> System.out.println(err), () -> System.out.println("done!"));
        stream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));


    }
}
