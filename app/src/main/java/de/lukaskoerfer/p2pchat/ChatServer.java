package de.lukaskoerfer.p2pchat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends ChatConnection {

    private Thread ServerThread;
    private ServerSocket ServerSocket;

    private List<Thread> ConnThreads = new ArrayList<Thread>();
    private List<Socket> ConnSockets = new ArrayList<Socket>();

    public ChatServer(ReceiveCallback callback) {
        super(callback);

    }

    private void startServer() {

    }

    private Runnable ServerLoop = new Runnable() {

        @Override
        public void run() {

        }
    };

    private class ClientLoop implements Runnable {

        private Socket socket;

        public ClientLoop(Socket client) {
            this.socket = client;
        }

        @Override
        public void run() {

        }
    }

    @Override
    public void SendMessage(ChatMessage message) {

    }

    @Override
    public void Stop() {

    }

}
