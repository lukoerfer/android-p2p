package de.lukaskoerfer.androidp2p;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionService extends Service {

    public ConnectionService() {
    }

    private NotificationManager notificationManager;

    private boolean IsStarted = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String action = intent.getStringExtra("ACTION");
        switch (action) {
            case "START":
                if (!IsStarted) {
                    IsStarted = true;
                    boolean isServer = intent.getBooleanExtra("IS_SERVER", false);
                    if (!isServer) {
                        String ip = intent.getStringExtra("IP");
                        connectToServer(ip);
                    } else {
                        openServer();
                    }
                }
                break;
            case "SEND":
                if (IsStarted) {
                    String message = intent.getStringExtra("MESSAGE");
                } else {
                    Toast.makeText(this, "Not connected!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.stopNotification();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

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
                        ConnectionService.this.clientSockets.add(clientSocket);
                        Thread clientThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                    while (true) {
                                        receiveMessage(in.readLine());
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        clientThread.start();
                        ConnectionService.this.clientThreads.add(clientThread);
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
                        receiveMessage(in.readLine());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.mainThread.start();
    }

    private void showMessageNotification(boolean isServer, String address) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Wifi P2P");
        builder.setContentText(isServer ? "Server running" : "Client connected to " + address);
        Notification notification = builder.getNotification();
        this.notificationManager.notify(0, notification);
    }

    private void stopNotification() {
        this.notificationManager.cancelAll();
    }

    private void receiveMessage(final String message) {
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("MESSAGE", message);
        startActivity(chatIntent);
    }
}
