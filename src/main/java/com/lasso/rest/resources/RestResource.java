package com.lasso.rest.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * Created by YugantShah on 1/11/14.
 */
@Path("app")
public class RestResource {

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User login(User user) {
        user.setToken(UUID.randomUUID().toString());
        return user;
    }

}

class User {
    String token;
    String userName;
    String password ;

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
