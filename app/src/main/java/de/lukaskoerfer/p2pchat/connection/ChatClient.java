package de.lukaskoerfer.p2pchat.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import de.lukaskoerfer.p2pchat.ClientInfo;
import de.lukaskoerfer.p2pchat.data.BaseMessage;
import de.lukaskoerfer.p2pchat.ReceiveCallback;
import de.lukaskoerfer.p2pchat.data.JoinMessage;

public class ChatClient extends ChatConnection {

    private Thread RunThread;
    private Socket Socket;
    private PrintWriter Writer;

    private InetAddress Target;

    private ClientInfo Info;

    public ChatClient(InetAddress address, ReceiveCallback callback, ClientInfo info) {
        super(callback);
        this.Info = info;
        this.Target = address;
        this.RunThread = new Thread(this.ClientLoop);
        this.RunThread.start();
    }

    private Runnable ClientLoop = new Runnable() {
        @Override
        public void run() {
            while (!ChatClient.this.ShouldStop) {
                try {
                    if (ChatClient.this.Socket != null) {
                        ChatClient.this.Socket.close();
                    }
                    ChatClient.this.Socket = new Socket();
                    ChatClient.this.Socket.connect(new InetSocketAddress(ChatClient.this.Target, CHAT_PORT), 1000);
                    ChatClient.this.IsConnected = true;
                    ChatClient.this.Writer = new PrintWriter(ChatClient.this.Socket.getOutputStream());
                    ChatClient.this.Writer.println(new JoinMessage(ChatClient.this.Info));
                    ChatClient.this.Writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(ChatClient.this.Socket.getInputStream()));
                    String message = reader.readLine();
                    while (!(ChatClient.this.ShouldStop || message == null)) {
                        BaseMessage received = BaseMessage.Deserialize(message);
                        ChatClient.this.Callback.ReceiveMessage(received);
                        message = reader.readLine();
                    }
                    reader.close();
                } catch (IOException ioex) {
                    ChatClient.this.IsConnected = false;
                }
            }
        }
    };

    @Override
    public void SendMessage(BaseMessage message) {
        if (this.Writer != null) {
            this.Writer.println(message.serialize());
            this.Writer.flush();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void Stop() {
        super.Stop();
        try {
            if (this.Socket != null) {
                this.Socket.close();
            }
            if (this.RunThread != null) {
                this.RunThread.join();
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
            // Log internal error while closing
        } catch (InterruptedException irex) {
            irex.printStackTrace();
            // if the thread is interrupted for any reason, stop it
            this.RunThread.stop();
        }
    }

}
