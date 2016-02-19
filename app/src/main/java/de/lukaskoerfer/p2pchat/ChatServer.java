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

    @Override
    public void SendMessage(ChatMessage message) {

    }

    /*

    private Thread mainThread;
    private Socket mainSocket;

    private List<Thread> clientThreads;
    private List<Socket> clientSockets;

    private void openServer() {
        this.mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8000);
                    while (true) {
                        final Socket clientSocket = serverSocket.accept();
                        ChatService.this.clientSockets.add(clientSocket);
                        Thread clientThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                    while (true) {
                                        //receiveMessage(in.readLine());
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        clientThread.start();
                        ChatService.this.clientThreads.add(clientThread);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.mainThread.start();
    }

    private void connectToServer(final String address) {
        this.mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket clientSocket = new Socket(address, 8000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    while (true) {
                        //receiveMessage(in.readLine());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.mainThread.start();

    }*/

}
