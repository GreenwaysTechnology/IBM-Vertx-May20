package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class TimerAsyncVerticle extends AbstractVerticle {

  private void syncApi(String message) {
    System.out.println(message);
  }

  //async api
  private Future<String> delay(long time, String message) {
    Future future = Future.future();
    //trigger async call
    vertx.setTimer(time, cb -> {
      //wrap async result into Future
      future.complete("Timer is Ready");
    });
    return future;
  }

  @Override
  public void start() throws Exception {
    //sync call
    syncApi("start");
    //async
    //caller need to grab async result
    delay(5000, "hello").onComplete(h -> {
      String asyncResult = h.succeeded() ? h.result() : h.cause().getMessage();
      System.out.println(asyncResult);
    });
    //sync call
    syncApi("going");
  }
}
