package de.lukaskoerfer.p2pchat;

public abstract class ChatConnection {

    protected ReceiveCallback Callback;

    protected ChatConnection(ReceiveCallback callback) {
        this.Callback = callback;
    }

    public abstract void SendMessage(ChatMessage message);

}
