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
        System.out.println("Deploying JerseyModule");
        startJerseyServer(startedResult);
        JsonObject config = new JsonObject();
        config.putString("address", "test.session-manager");
        config.putNumber("timeout",15 * 60 * 1000 );
        config.putString("cleaner", "test.session-cleanup");
        config.putString("prefix", "session-client");
        container.deployModule("com.campudus~session-manager~2.0.1-final",config,
                new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                System.out.println("Handler Invoked with :"+ event.succeeded());
            }
        });

        System.out.println("Deployed Session Manager");
    }

    private void startJerseyServer(final Future<Void> startedResult) {
        JsonObject config = container.config();
        JerseyServer jerseyServer = jerseyServerProvider.get();
        JerseyConfigurator configurator = configuratorProvider.get();

        configurator.init(config, vertx, container);

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
