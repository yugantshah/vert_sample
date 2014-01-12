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
        container.deployModule("com.englishtown~vertx-mod-jersey~2.0.0-SNAPSHOT");
//        startJerseyServer(startedResult);
        System.out.println("Deployed JerseyModule");
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
