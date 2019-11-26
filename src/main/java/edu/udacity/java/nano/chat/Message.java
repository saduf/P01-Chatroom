package edu.udacity.java.nano.chat;

/**
 * WebSocket message model
 */
public class Message {
    // TODO: add message model.
    private String msg;
    private String username;
    private String type;
    private String onlineCount;

    @Override
    public String toString() {
        return super.toString();
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setContent(String content) {
        this.msg = content;
    }

    public String getContent() {
        return msg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setOnline(Integer online) {
        this.onlineCount = Integer.toString(online);
    }

    public String getOnline() {
        return onlineCount;
    }

}

