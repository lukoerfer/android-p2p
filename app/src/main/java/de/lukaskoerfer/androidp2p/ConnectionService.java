package de.lukaskoerfer.androidp2p;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionService extends Service {

    public ConnectionService() {
    }

    private NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String address = intent.getStringExtra("CONN_ADDRESS");
        boolean isServer = intent.getBooleanExtra("CONN_IS_SERVER", true);
        startNotification(isServer, address);
        if (isServer) {
            openServer(address);
        } else {
            connectToServer(address);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.networkThread.stop();
        this.stopNotification();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Thread networkThread;

    private void openServer(final String address) {
        this.networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket server = new ServerSocket(8000);
                    Socket client = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while (true) {
                        Toast.makeText(ConnectionService.this, in.readLine(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(ConnectionService.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.networkThread.start();
    }

    private void connectToServer(final String address) {
        this.networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket client = new Socket(address, 8000);
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    int counter = 1;
                    while (true) {
                        out.println("Message " + String.valueOf(counter));
                        Thread.sleep(5000);
                    }
                } catch (Exception ex) {
                    Toast.makeText(ConnectionService.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.networkThread.start();
    }

    private void startNotification(boolean isServer, String address) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Wifi P2P");
        builder.setContentText(isServer ? "Server running at" : "Client connecting to" + address);
        Notification notification = builder.getNotification();
        this.notificationManager.notify(0, notification);
    }

    private void stopNotification() {
        this.notificationManager.cancelAll();
    }
}
