package de.lukaskoerfer.p2pchat.data;

import de.lukaskoerfer.p2pchat.ClientInfo;

public class ChatMessage extends BaseMessage {

    private ClientInfo Client;
    private String Content;

    public ChatMessage(ClientInfo client, String content) {
        this.Client = client;
        this.Content = content;
    }

    public ClientInfo getClient() {
        return this.Client;
    }

    public String getContent() {
        return this.Content;
    }

    public String serialize() {
        return "CHAT" + this.Client.serialize() + "#" + this.Content;
    }

    public static ChatMessage Deserialize(String part) {
        int index = part.lastIndexOf("#");
        String clientString = part.substring(0, index - 1);
        ClientInfo client = ClientInfo.Deserialize(clientString);
        String content = part.substring(index + 1);
        return new ChatMessage(client, content);
    }

}

