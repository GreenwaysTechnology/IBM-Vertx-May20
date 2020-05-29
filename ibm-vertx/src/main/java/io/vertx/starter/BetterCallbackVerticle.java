package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class BetterCallbackVerticle extends AbstractVerticle {

  private void getUser(Handler<AsyncResult<String>> aHandler) {
    String fakeUser = "Subramanian";
    if (fakeUser != null) {
      //handle success
      aHandler.handle(Future.succeededFuture(fakeUser));
    } else {
      //handle failures
      aHandler.handle(Future.failedFuture("No User Found!"));
    }

  }

  private void login(String userName, Handler<AsyncResult<String>> aHandler) {
    if (userName.equals("Subramanian")) {
      //handle success
      aHandler.handle(Future.succeededFuture("Login Success!"));
    } else {
      //handle failures
      aHandler.handle(Future.failedFuture("Login Failed"));
    }

  }

  @Override
  public void start() throws Exception {
    getUser(ar -> {
      if (ar.succeeded()) {
        System.out.println(ar.result());
        login(ar.result(), lar -> {
          String result = lar.succeeded() ? lar.result() : lar.cause().getMessage();
          System.out.println(result);
        });
      }
    });
  }
}
