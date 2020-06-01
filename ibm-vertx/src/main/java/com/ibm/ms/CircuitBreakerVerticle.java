package com.ibm.ms;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;


import io.vertx.circuitbreaker.HystrixMetricHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

//class CirCuitBreakerServerVerticle extends AbstractVerticle {
//
//  @Override
//  public void start() {
//    // Create a Vert.x Web router
//    Router router = Router.router(vertx);
//    // Register the metric handler
//    router.get("/").handler(req -> req.response().end("Subramanian"));
//    vertx.createHttpServer().requestHandler(router).listen(8080);
//  }
//
//}


public class CircuitBreakerVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    super.start();

    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
      new CircuitBreakerOptions()
        .setMaxFailures(5) // number of failure before opening the circuit
        .setTimeout(2000) // consider a failure if the operation does not succeed in time
        .setFallbackOnFailure(true) // do we call the fallback on failure
        .setResetTimeout(10000) // time spent in open state before attempting to re-try
    );


// ---
// Store the circuit breaker in a field and access it as follows
// ---

//    breaker.<String>execute(future -> {
//      // some code executing with the breaker
//      // the code reports failures or success on the given future.
//      // if this future is marked as failed, the breaker increased the
//      // number of failures
//      WebClient client = WebClient.create(vertx);
//
//// Send a GET request
//      client
//        .get("jsonplaceholder.typicode.com", "/postsxxx")
//        .send(ar -> {
//          // Obtain response
//          HttpResponse<Buffer> response = ar.result();
//          if (response.statusCode() != 200) {
//            future.fail("HTTP error");
//          } else {
//            future.complete(response.bodyAsJsonArray().encode());
//          }
//
//
//        });
//
//    }).onComplete(ar -> {
//      // Get the operation result.
//      if (ar.succeeded()) {
//        System.out.println(ar.result());
//      } else {
//        System.out.println(ar.cause());
//
//      }
//    });

    Router router = Router.router(vertx);
// Register the metric handler
    router.get("/hystrix-metrics").handler(HystrixMetricHandler.create(vertx));

// Create the HTTP server using the router to dispatch the requests
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8082);

    breaker.executeWithFallback(
      future -> {
        WebClient client = WebClient.create(vertx);
//
//// Send a GET request
        client
          .get("jsonplaceholder.typicode.com", "/postsxxx")
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
