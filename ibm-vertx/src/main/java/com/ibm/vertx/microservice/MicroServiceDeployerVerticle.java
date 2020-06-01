package com.ibm.vertx.microservice;

import com.ibm.ms.ReactiveHTTPServer;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public class MicroServiceDeployerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    //vertx.deployVerticle(new FileSystemConfigVerticle());

//    ConfigStoreOptions options = new ConfigStoreOptions();
//    options.setType("file");
//    // options.setFormat("json");
//    options.setConfig(new JsonObject().put("path", "application.json"));
//    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(options));
//
//    retriever.getConfig(config -> {
//      //once config ready deploy the verticle
//      JsonObject jsonObject = config.result().getJsonObject("application");
//      vertx.deployVerticle(new FileSystemConfigOutsideVerticle(), new DeploymentOptions().setConfig(jsonObject));
//    });

    //webclient deployment:
    // vertx.deployVerticle(new ThirdPartyRESTCallVerticle());
    //vertx.deployVerticle(new ServiceDiscoveryVerticle());
    vertx.deployVerticle(new ReactiveHTTPServer());
    vertx.deployVerticle(new RequestFromReactiveClient());

  }
}
