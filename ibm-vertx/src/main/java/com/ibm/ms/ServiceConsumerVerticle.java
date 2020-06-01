package com.ibm.ms;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

import io.vertx.servicediscovery.types.HttpEndpoint;

public class ServiceConsumerVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    System.out.println("Service consumer starting....");

    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
      .addStore(new ConfigStoreOptions().setType("file").setFormat("yaml").setConfig(new JsonObject().put("path", "application.yaml"))));

    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    discoveryOptions.setBackendConfiguration(new JsonObject().put("connection", "127.0.0.1:2181")
      .put("ephemeral", true).put("guaranteed", true).put("basePath", "/services/my-backend"));
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

    retriever.getConfig(properties -> {
      String ServiceName = properties.result().getString("service");
      String root = properties.result().getString("api");

      vertx.createHttpServer().requestHandler(req -> {

        HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", ServiceName), ar -> {
          if (ar.succeeded()) {
            WebClient client = ar.result();
            // You need to path the complete path
            client.get(root).send(response -> {
              System.out.println(response.result().body());
              req.response().end(response.result().body());
              ServiceDiscovery.releaseServiceObject(discovery, client);
            });
          }
        });
      }).listen(9998, "localhost", myserver -> {
        System.out.println("Consumer is listening");
      });

    });


  }
}

