package com.ibm.vertx.microservice;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class FileSystemConfigVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    super.start();

    //Config Store Options
    //Add Storage options: type, format,file path

    ConfigStoreOptions options = new ConfigStoreOptions();
    options.setType("file");
    // options.setFormat("json");
    options.setConfig(new JsonObject().put("path", "application.json"));
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(options));

    retriever.getConfig(config -> {
      if (config.succeeded()) {
        System.out.println("Config is Ready");
        //System.out.println(config.result());
        JsonObject configRes = config.result();
        System.out.println(configRes.getJsonObject("application").getString("name"));
        System.out.println(configRes.getJsonObject("application").getInteger("port"));

      } else {
        System.out.println("Config Error : " + config.cause());
      }
    });

    //YAML Reader
    ConfigStoreOptions optionsYaml = new ConfigStoreOptions();
    optionsYaml.setType("file");
    optionsYaml.setFormat("yaml");
    optionsYaml.setConfig(new JsonObject().put("path", "application.yaml"));
    ConfigRetriever retrieverYaml = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(optionsYaml));

    retrieverYaml.getConfig(yamlProps -> {
      System.out.println("Yaml Properties");
      System.out.println((yamlProps.result()));
      System.out.println(yamlProps.result().getInteger("port"));
    });

  }
}
