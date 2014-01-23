package com.lasso.rest.resources;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


/**
 * Created by YugantShah on 1/11/14.
 */
@Path("app")
public class RestResource {

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void login(final User user, @Context Vertx vertx, @Suspended final AsyncResponse response) throws InterruptedException {
        JsonObject startSession = new JsonObject();
        startSession.putString("action", "start");
        final EventBus eventBus = vertx.eventBus();
        final JsonObject reply = new JsonObject();
        final String smAddress = "test.session-manager";
        vertx.runOnContext(new Handler<Void>() {
            @Override
            public void handle(Void aVoid) {
                eventBus.send(smAddress, new JsonObject().putString("action", "start"),
                        new Handler<Message<JsonObject>>() {
                            @Override
                            public void handle(Message<JsonObject> event) {
                                JsonObject replyObject = event.body();
                                System.out.println("start: " + replyObject.getString("sessionId"));
                                reply.putString("userName", user.getUserName());
                                reply.putString("auth-token", replyObject.getString("sessionId"));
                                response.resume("auth-token:" + replyObject.getString("sessionId"));
                            }
                        });
            }
        });
    }

}

class User {
    String token;
    String userName;
    String password;

    public User() {
    }

    private User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

/*final JsonObject reply = new JsonObject();
        Handler<Message<JsonObject>> replyHandler = new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                JsonObject replyObject = event.body();
                System.out.println("----:" + replyObject);
                reply.mergeIn(replyObject);
            }
        };
        Handler<Message<String>> replyHandler1 = new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                System.out.println("Handler-------------" + event.body());
            }
        };
        vertx.eventBus().send("campudus.session", startSession, replyHandler1);
        vertx.eventBus().send("campudus.session", startSession, replyHandler);
        vertx.eventBus().send("sample.session-manager", startSession, replyHandler);
        vertx.eventBus().send("sample.session-manager", startSession, replyHandler1);
        vertx.eventBus().send("sample.session-manager", new JsonObject().putString("action","status").putString("report", "connections"));*/