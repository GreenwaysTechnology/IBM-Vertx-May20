package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class BasicFutureVerticle extends AbstractVerticle {

  //methods which return future
  private Future<Void> getEmptyFuture() {
    //Create Future Object
    Future future = Future.future();
    //Invoke async completion event
    future.complete();
    return future;

  }

  //return the basic Future with payload(data)
  private Future<String> getDataFuture() {
    //Create Future Object
    Future future = Future.future();
    //Invoke async completion event
    future.complete("Hello I am Async Result");
    return future;
  }

  //return only error
  private Future<String> getError() {
    Future future = Future.future();
    //Invoke async completion event
    future.fail("Something went wrong!!!!");
    return future;
  }

  //send success or failur based on biz use case

  private Future<String> validate(String userName, String password) {
    Future<String> future = Future.future();
    //biz logic
    if (userName.equals("admin") && password.equals("admin")) {
      //set completion
      future.complete("Login Success");
    } else {
      future.fail("Login failed");
    }
    return future;

  }


  @Override
  public void start() throws Exception {
    System.out.println("Future Starts");
    //Declare Future reference to handle Future
    Future future = null;
    future = getEmptyFuture();

    //future returns empty result
    if (future.succeeded()) {
      System.out.println("Future return success message");
    } else {
      System.out.println("Future not returned any thing!");
    }
    //get string data
    future = getDataFuture();
    //old java style
    future.onComplete(new Handler<AsyncResult>() {
      @Override
      public void handle(AsyncResult event) {
        if (event.succeeded()) {
          System.out.println(event.result());
        }

      }
    });
    //lambda style
    getDataFuture().onComplete(h -> {
      if (h.succeeded()) {
        System.out.println(h.result());
      }
    });

    //replacer for setHanlder :this only for your api is returning success only
    getDataFuture().onSuccess(result -> System.out.println(result));
    getDataFuture().onSuccess(System.out::println);

    //Handle error using setHandler
    getError().onComplete(handler -> {
      if (handler.failed()) {
        //grab error messages
        System.out.println(handler.cause());

      }
    });
    getError().onFailure(System.out::println);

    //validate method
    validate("admin", "admin").onComplete(h -> {
      //grab status
      if (h.succeeded()) {
        System.out.println(h.result());
      } else {
        System.out.println(h.cause());
      }
    });

  }
}
