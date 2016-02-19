package de.lukaskoerfer.p2pchat;

public abstract class ChatConnection {

    public static final int CHAT_PORT = 8080;

    protected ReceiveCallback Callback;

    protected ChatConnection(ReceiveCallback callback) {
        this.Callback = callback;
    }

    public abstract void SendMessage(ChatMessage message);

    public abstract void Stop();
}
