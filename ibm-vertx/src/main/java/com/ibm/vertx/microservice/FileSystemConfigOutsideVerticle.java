package com.ibm.vertx.microservice;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;


public class FileSystemConfigOutsideVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    //System.out.println(config().getString("name") + config().getString("Version"));

    vertx.createHttpServer().
      requestHandler(res -> res.response().end(config().getString("name")))
      .listen(config().getInteger("port"));
  }


}
