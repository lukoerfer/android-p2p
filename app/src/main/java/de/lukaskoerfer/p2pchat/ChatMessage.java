package de.lukaskoerfer.p2pchat;


public class ChatMessage {

    private String Content;

    public ChatMessage(String content) {
        this.Content = content;
    }

    public String getContent() {
        return this.Content;
    }

    public String Serialize() {
        return this.Content;
    }

    public static ChatMessage Deserialize(String serialized) {
        return new ChatMessage(serialized);
    }
}
