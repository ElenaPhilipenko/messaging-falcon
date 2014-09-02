package messaging;


import play.mvc.WebSocket;
import redis.clients.jedis.JedisPubSub;

public class MessagingJedisPub extends JedisPubSub {
    private final WebSocket.Out<String> out;

    public MessagingJedisPub(WebSocket.Out<String> out) {
        this.out = out;
    }

    @Override
    public void onMessage(String channel, String message) {
        out.write(message);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}
