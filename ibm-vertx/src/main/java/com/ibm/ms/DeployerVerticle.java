package com.ibm.ms;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public class DeployerVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // vertx.deployVerticle(new ServiceDiscoveryPublisher());
    //vertx.deployVerticle(new ServiceConsumerVerticle());
    //  vertx.deployVerticle(new HttpEndPointVerticle());
    //   vertx.deployVerticle(new JDBCDiscoveryVerticle());

//    DeploymentOptions options = new DeploymentOptions()
//      .setConfig(new JsonObject()
//        .put("api.name", "posts"));
//    vertx.deployVerticle(new HTTPEndPointWithBase(), options);
    //vertx.deployVerticle(new CircuitBreakerVerticle());
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());

  }
}
