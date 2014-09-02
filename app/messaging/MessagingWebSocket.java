package messaging;

import play.libs.F;
import play.mvc.WebSocket;
import redis.clients.jedis.Jedis;

public class MessagingWebSocket extends WebSocket<String> {

    public static final String MESSAGES = "messages";
    public static final String LOCALHOST = "localhost";

    public void onReady(WebSocket.In<String> in, final WebSocket.Out<String> out) {
        final Jedis jedisSubscriber = new Jedis(LOCALHOST);
        in.onMessage(new F.Callback<String>() {
            public void invoke(String event) {
                System.out.println("Received: " + event);
            }
        });

        final Thread s = new Thread(new Runnable() {
            @Override
            public void run() {
                jedisSubscriber.subscribe(new MessagingJedisPub(out), MESSAGES);
            }
        });
        s.start();

        in.onClose(new F.Callback0() {
            public void invoke() {
                jedisSubscriber.close();
            }
        });
    }
}
