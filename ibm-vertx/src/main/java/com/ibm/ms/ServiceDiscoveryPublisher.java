package com.ibm.ms;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class ServiceDiscoveryPublisher extends AbstractVerticle {

  Record httpRecord;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
      .addStore(new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", "application.yaml"))));

    retriever.getConfig(properties -> {

      System.out.println(properties.result());
      int port = properties.result().getInteger("port");
      String serviceName = properties.result().getString("service");
      String host = properties.result().getString("host");
      String root = properties.result().getString("api");
      System.out.println(port + " " + serviceName + " " + host);
      httpRecord = HttpEndpoint.createRecord(serviceName, host, port, root);

      //Create HTTP Resource and register inside Discorvery
      //Record httpRecord = HttpEndpoint.createRecord("hello", "localhost", 3000, "/greet");
      ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
      discoveryOptions.setBackendConfiguration(new JsonObject().put("connection", "localhost:2181")
        .put("ephemeral", true).put("guaranteed", true).put("basePath", "/services/my-backend"));

      ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

      discovery.publish(httpRecord, r -> {
        if (r.succeeded()) {
          System.out.println("successfully published to zookeeper>>>>> " + r.result().toJson());
        } else {
          r.cause().printStackTrace();
        }
      });
      HttpServer server = vertx.createHttpServer();
      server.requestHandler(req -> req.response().end("Hello"));
      server.listen(8999, "localhost");

    });


  }
}
