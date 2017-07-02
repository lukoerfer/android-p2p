package de.lukaskoerfer.p2pchat.connection;

import de.lukaskoerfer.p2pchat.data.BaseMessage;
import de.lukaskoerfer.p2pchat.ReceiveCallback;

public abstract class ChatConnection {

    public static final int CHAT_PORT = 8050;

    public boolean IsConnected = false;

    protected boolean ShouldStop = false;

    protected ReceiveCallback Callback;

    protected ChatConnection(ReceiveCallback callback) {
        this.Callback = callback;
    }

    public abstract void SendMessage(BaseMessage message);

    public void Stop() {
        this.IsConnected = false;
        this.ShouldStop = true;
    }

}

