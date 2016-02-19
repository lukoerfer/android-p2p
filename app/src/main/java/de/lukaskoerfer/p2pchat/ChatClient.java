package de.lukaskoerfer.p2pchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient extends ChatConnection {

    private boolean ShouldStop = false;

    private Thread Thread;
    private Socket Socket;
    private PrintWriter Writer;

    private InetAddress Target;

    public ChatClient(InetAddress address, ReceiveCallback callback) {
        super(callback);
        this.Target = address;
        this.connectClient();
    }

    private void connectClient() {
        this.Thread = new Thread(this.ClientLoop);
        this.Thread.start();
    }

    private Runnable ClientLoop = new Runnable() {
        @Override
        public void run() {
            try {
                if (ChatClient.this.Socket != null) {
                    ChatClient.this.Socket.close();
                }
                ChatClient.this.Socket = new Socket(ChatClient.this.Target, CHAT_PORT);
                ChatClient.this.Writer = new PrintWriter(ChatClient.this.Socket.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(ChatClient.this.Socket.getInputStream()));
                while (!ChatClient.this.ShouldStop) {
                    ChatMessage received = ChatMessage.Deserialize(reader.readLine());
                    ChatClient.this.Callback.ReceiveMessage(received);
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
                if (!ChatClient.this.ShouldStop) {
                    // restart
                }
            }
        }
    };

    @Override
    public void SendMessage(ChatMessage message) {
        if (this.Writer != null) {
            this.Writer.println(message.Serialize());
        }
    }

    @Override
    public void Stop() {
        this.ShouldStop = true;
        if (this.Writer != null) {
            this.Writer.close();
            this.Writer = null;
        }
        if (this.Socket != null) {
            try {
                this.Socket.close();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
        if (this.Thread != null) {
            try {
                this.Thread.join();
            } catch (InterruptedException irex) {
                irex.printStackTrace();
            }
        }
    }

}
