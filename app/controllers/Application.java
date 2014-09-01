package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Hello"));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result message() {
        final JsonNode json = request().body().asJson();
        final String name = json.findPath("message").textValue();
        System.out.println(name);
        if(name == null) {
            return badRequest("Missing parameter [message]");
        } else {
            return ok("Hello " + name);
        }
    }

}
