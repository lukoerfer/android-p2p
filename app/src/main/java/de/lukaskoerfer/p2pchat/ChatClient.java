package de.lukaskoerfer.p2pchat;

import java.net.Socket;

public class ChatClient extends ChatConnection {

    private Thread ConnThread;
    private Socket ConnSocket;

    public ChatClient(String address, ReceiveCallback callback) {
        super(callback);

    }



    @Override
    public void SendMessage(ChatMessage message) {

    }

}
