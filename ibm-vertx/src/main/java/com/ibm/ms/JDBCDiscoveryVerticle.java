package com.ibm.ms;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.JDBCDataSource;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

public class JDBCDiscoveryVerticle extends AbstractVerticle {
  private static final String SQL_CREATE_PAGES_TABLE = "create table if not exists Pages (Id integer identity primary key, Name varchar(255) unique, Content clob)";
  private static final String SQL_GET_PAGE = "select Id, Content from Pages where Name = ?"; // <1>
  private static final String SQL_CREATE_PAGE = "insert into Pages values (NULL, ?, ?)";
  private static final String SQL_SAVE_PAGE = "update Pages set Content = ? where Id = ?";
  private static final String SQL_ALL_PAGES = "select Name from Pages";
  private static final String SQL_DELETE_PAGE = "delete from Pages where Id = ?";

  @Override
  public void start() throws Exception {

    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    discoveryOptions.setBackendConfiguration(new JsonObject().put("connection", "127.0.0.1:2181")
      .put("ephemeral", true).put("guaranteed", true).put("basePath", "/services/my-backend"));
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

    Record jdbcRecord = JDBCDataSource.createRecord(
      "dbc-service", // The service name
      new JsonObject().put("url", "jdbc:hsqldb:file:db/wiki"), // The location
      new JsonObject().put("diver_class", "org.hsqldb.jdbcDriver")
    );

    discovery.publish(jdbcRecord, ar -> {
      if (ar.succeeded()) {
        System.out.println("successfully published to zookeeper>>>>> " + ar.result().toJson());
      } else {
        ar.cause().printStackTrace();
      }
    });
    ///////////////////////////////////////////////////////////////////////////////////////
    discovery.getRecord(
      new JsonObject().put("name", "dbc-service"),
      ar -> {
        if (ar.succeeded() && ar.result() != null) {
          // Retrieve the service reference
          ServiceReference reference = discovery.getReferenceWithConfiguration(
            ar.result(), // The record
            new JsonObject()); // Some additional metadata

          // Retrieve the service object
          JDBCClient client = reference.getAs(JDBCClient.class);

          // ...
          System.out.println("JDBC is ready");
          System.out.println(client.getConnection(conn -> {
            System.out.println("Connection is ready");

          }));


          // when done
          reference.release();
        } else {
          System.out.println(ar.cause());
        }
      });


  }
}

