package com.ibm.vertx.microservice;

import com.ibm.ms.BaseMicroServiceVerticle;
import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class CircuitBreakerVerticle extends BaseMicroServiceVerticle {

  @Override
  public void start() throws Exception {
    super.start();
    //Create Breaker Object
//    CircuitBreakerOptions options = new CircuitBreakerOptions();
//    options.setMaxFailures(2);// no of failures will be allowed , after that , ciruit will open
//    options.setTimeout(2000); // consider a failure if the operation deos not succeed in time
//    options.setFallbackOnFailure(true); // if any failure, should i handle fallback or not
//    options.setResetTimeout(5000); // time spent in open state before attempting to retry.
//    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx, options);

    //how to employ safty to my code when failure dedecuted

    circuitBreaker.executeWithFallback(
      future -> {
        WebClient client = WebClient.create(vertx);
//
//// Send a GET request
        client
          .get("jsonplaceholder.typicode.com", "/posts")
          .send(ar -> {
            // Obtain response
            HttpResponse<Buffer> response = ar.result();
            if (response.statusCode() != 200) {
              future.fail("HTTP error");
            } else {
              future.complete(response.bodyAsJsonArray().encode());
            }

          });
      }, v -> {
        // Executed when the circuit is opened
        return "Hello, I am fallback";
      })
      .onComplete(ar -> {
        // Do something with the result
        if (ar.succeeded()) {
          System.out.println(ar.result());
        } else {
          System.out.println(ar.cause());

        }
      });


  }
}
