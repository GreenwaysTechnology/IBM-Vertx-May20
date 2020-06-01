package com.ibm.ms;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class HttpEndPointVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    //discovery back end
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    discoveryOptions.setBackendConfiguration(new JsonObject().put("connection", "127.0.0.1:2181")
      .put("ephemeral", true).put("guaranteed", true).put("basePath", "/services/my-backend"));
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);
    example1(discovery);
    //  restapicalls();
  }

  //some rest api calls
  public void restapicalls() {
    WebClient client = WebClient.create(vertx);

// Send a GET request
    client
      .get("jsonplaceholder.typicode.com", "/posts")
      .send(ar -> {
        if (ar.succeeded()) {
          // Obtain response
          HttpResponse<Buffer> response = ar.result();

          System.out.println(response.bodyAsJsonArray());

          System.out.println("Received response with status code" + response.statusCode());

        } else {
          System.out.println("Something went wrong " + ar.cause().getMessage());
        }
      });
  }

  public void example1(ServiceDiscovery discovery) {
//    Record record1 = HttpEndpoint.createRecord(
//      "post-http-service", // The service name,
//      "jsonplaceholder.typicode.com", // The host
//      58304, // the port
//      "/posts" // the root of the service
//    );
    Record record1 = HttpEndpoint.createRecord("http-posts-service", true, "jsonplaceholder.typicode.com", 443, "/posts", new JsonObject());

    discovery.publish(record1, ar -> {
      if (ar.succeeded()) {
        System.out.println("successfully published to zookeeper>>>>> " + ar.result().toJson());
      } else {
        System.out.println("posts-http-service error!");
      }
    });

    ///////////////////////////////////////
    // Get the record
    vertx.setTimer(5000, res -> {
      HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "http-posts-service"), ar -> {

        if (ar.succeeded()) {
          WebClient client = ar.result();

          client.get("/posts").send(response -> {
            System.out.println("Response is ready!");
            System.out.println(response.succeeded());
            System.out.println(response.result().bodyAsJsonArray());
            ServiceDiscovery.releaseServiceObject(discovery, client);
          });
        }
      });
    });


  }
}
