package com.ibm.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;

public class RequestBodyVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {

    //Set server configuration
    HttpServerOptions config = new HttpServerOptions();
    config.setPort(3001);
    HttpServer server = vertx.createHttpServer(config);
    //handling client request
    server.requestHandler(context -> {
      //How to read client Request
      context.handler(chunk -> {
        System.out.println(chunk);
        //send a response:
        context.response().end(chunk);
      });
    });

    //start the httpserver
    server.listen(serverHandler -> {
      if (serverHandler.succeeded()) {
        System.out.println("Server is Up " + serverHandler.result());
      } else {
        System.out.println(serverHandler.cause());
      }
    });
  }
}
