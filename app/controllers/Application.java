package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import messaging.MessagingWebSocket;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import redis.clients.jedis.Jedis;
import views.html.index;

import static messaging.MessagingWebSocket.MESSAGES;

public class Application extends Controller {

    private static final Jedis jedisPublisher = new Jedis("localhost");

    public static Result index() {
        return ok(index.render("Hello"));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result postPublish() {
        final JsonNode json = request().body().asJson();
        String message = json.findPath("message").asText();
        if (message == null) {
            return badRequest("Missing parameter [message]");
        } else {
            return publish(message);
        }
    }

    private static Result publish(String message) {
        jedisPublisher.publish(MESSAGES, message);
        jedisPublisher.rpush(MESSAGES, message);
        return ok("published");
    }

    public static WebSocket<String> subscribe() {
        return new MessagingWebSocket();
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result archive() {
        ObjectNode result = Json.newObject();
        ArrayNode archive = result.putArray(MESSAGES);
        for (String each : jedisPublisher.lrange(MESSAGES, 0, -1)) {
            archive.add(each);
        }
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result cleanArchive() {
        jedisPublisher.del(MESSAGES);
        return ok("cleaned");
    }


}
