package com.ibm.vertx.microservice;

import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;

class RequestFromReactiveClient extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    HttpClient client = vertx.createHttpClient();
    client.put(8085, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.handler(buf -> System.out.println(buf.toString("UTF-8")));
    }).setChunked(true).putHeader("Content-Type", "text/plain").write("hello").end();
  }

}
