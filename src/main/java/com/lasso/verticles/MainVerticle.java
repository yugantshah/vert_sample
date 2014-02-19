package com.lasso.verticles;

import com.englishtown.vertx.jersey.JerseyConfigurator;
import com.englishtown.vertx.jersey.JerseyServer;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by YugantShah on 1/11/14.
 */
public class MainVerticle  extends Verticle {

    private final Provider<JerseyConfigurator> configuratorProvider;
    private final Provider<JerseyServer> jerseyServerProvider;

    @Inject
    public MainVerticle(Provider<JerseyServer> jerseyServerProvider, Provider<JerseyConfigurator> configuratorProvider) {
        this.jerseyServerProvider = jerseyServerProvider;
        this.configuratorProvider = configuratorProvider;
    }

    @Override
    public void start(Future<Void> startedResult) {
        super.start();
        System.out.println("Deploying JerseyModule: "+container.config());
        startJerseyServer(startedResult);
        container.deployModule("io.vertx~mod-mongo-persistor~2.1.0",container.config().getObject("mongo-persistor"),
                new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                if(!event.succeeded()) {
                    event.cause().printStackTrace();
                }
                System.out.println("Mongo Handler Invoked with :"+ event.succeeded());
            }
        });

        container.deployModule("com.campudus~session-manager~2.0.1-final",container.config().getObject("session-manager"),
                new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                if(!event.succeeded()) {
                    event.cause().printStackTrace();
                }
                System.out.println("Session manager Handler Invoked with :"+ event.succeeded());
            }
        });
        JsonObject mongoData = new JsonObject();
        mongoData.putString("testData", "test");
        System.out.println("Deployed Session Manager");
    }

    private void startJerseyServer(final Future<Void> startedResult) {
        JsonObject config = container.config();
        System.out.println("Jersey Config :"+config.getObject("rest_config").toString());
        JerseyServer jerseyServer = jerseyServerProvider.get();
        JerseyConfigurator configurator = configuratorProvider.get();

        configurator.init(config.getObject("rest_config"), vertx, container);

        jerseyServer.init(configurator, new Handler<AsyncResult<HttpServer>>() {
            @Override
            public void handle(AsyncResult<HttpServer> result) {
                if (result.succeeded()) {
                    startedResult.setResult(null);
                } else {
                    startedResult.setFailure(result.cause());
                }
            }
        });
    }
}
