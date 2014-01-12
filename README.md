# Vert.x Jersey REST Maven Project


This example uses vertx-mod-jersey.

main Verticle -com.lasso.verticles.MainVerticle.

This verticle starts the jersey server and listens to requests.

mod.json
    Added "includes": "com.englishtown~vertx-mod-jersey~2.0.0-SNAPSHOT"

config.json
    Contains jersey configuration.

To run module:
mvn vertx:runMod -Dvertx.langs.java=com.englishtown~vertx-mod-hk2~1.0.0-final:com.englishtown.vertx.hk2.HK2VerticleFactory

Using any rest client:

url: http://localhost:8080/app/login
Content-type:application/json
Method:POST

Json Request String  :
{
     "userName":"yushah",
     "password":"assaa"
}






