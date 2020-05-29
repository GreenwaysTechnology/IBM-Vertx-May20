package io.vertx.starter;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class ConfigVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // ConfigStoreOptions file = new ConfigStoreOptions().setType("file").setConfig(new JsonObject().put("path", "application.json"));
    ConfigStoreOptions store = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject()
        .put("path", "application.yaml")
      );
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(store));
    retriever.getConfig(conf -> {
      if (conf.succeeded()) {
//        JsonObject app = conf.result().getJsonObject("application");
//        System.out.println(app.getString("name"));
//        System.out.println(app.getInteger("port"));
        //YAML VALUES
        String app = conf.result().getString("name");
        System.out.println(app);
      } else {
        System.out.println(conf.cause());
      }
    });
  }
}
