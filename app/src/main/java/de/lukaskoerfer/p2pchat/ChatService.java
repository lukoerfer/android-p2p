package de.lukaskoerfer.p2pchat;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ChatService extends Service implements WifiP2pManager.ConnectionInfoListener {

    private P2pChatApplication Application;

    private ChatConnection Connection;

    public ChatService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.Application = (P2pChatApplication) this.getApplication();
        this.Application.P2pHandler.Manager.requestConnectionInfo(this.Application.P2pHandler.Channel, this);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.ServiceBinder;
    }

    private final IBinder ServiceBinder = new ChatBinder();

    public class ChatBinder extends Binder {

        public List<ChatMessage> CheckoutMessages() {
            return null;
        }

        public boolean SendMessage(ChatMessage message) {
            if (ChatService.this.Connection != null) {
                ChatService.this.Connection.SendMessage(message);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {

    }

    private ReceiveCallback Receiver = new ReceiveCallback() {
        @Override
        public void ReceiveMessage(ChatMessage message) {

        }
    };

    private void addMessageNotification(ChatMessage message) {

    }
}
