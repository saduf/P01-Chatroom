package edu.udacity.java.nano.chat;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint(value="/chat/{username}", decoders = { MessageDecoder.class }, encoders = { MessageEncoder.class} )
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message msg) throws IOException, EncodeException {
        //TODO: add send message method.

        for (String key : onlineSessions.keySet()) {
            try {
                onlineSessions.get(key).getBasicRemote().sendObject(msg);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        //TODO: add on open connection.
        onlineSessions.put(username, session);
        Message message = new Message();
        message.setUsername(username);
        message.setContent("Connected!");
        message.setOnline(onlineSessions.size());
        message.setType("SPEAK");
        //System.out.println( username + " just joined the chat!");
        sendMessageToAll(message);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) throws IOException, EncodeException {
        //TODO: add se
        //
        // nd message.
        Gson gson = new Gson();
        Message message = gson.fromJson(jsonStr, Message.class);
        message.setType("SPEAK");
        sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        //TODO: add close connection.
        for (String key : onlineSessions.keySet()) {
            try {
                if (onlineSessions.get(key).getId().equals(session.getId())) {
                    onlineSessions.remove(key);
                    Message message = new Message();
                    message.setUsername(key);
                    message.setContent("Disconnected!");
                    message.setOnline(onlineSessions.size());
                    message.setType("SPEAK");
                    sendMessageToAll(message);

                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
