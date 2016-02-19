package de.lukaskoerfer.p2pchat;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.IBinder;

import java.util.List;

public class ChatService extends Service implements WifiP2pManager.ConnectionInfoListener {

    private P2pChatApplication Application;

    private ChatConnection Connection;

    private WifiP2pInfo PrevP2pInfo;

    public ChatService() {
        this.PrevP2pInfo = new WifiP2pInfo();
        this.PrevP2pInfo.groupFormed = false;
        this.PrevP2pInfo.isGroupOwner = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.Application = (P2pChatApplication) this.getApplication();
        this.Application.P2pHandler.Manager.requestConnectionInfo(this.Application.P2pHandler.Channel, this);
        return START_NOT_STICKY;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
        if (!areInfosEquals(this.PrevP2pInfo, p2pInfo)) {
            // stop possible previous connection
            if (this.Connection != null) {
                this.Connection.Stop();
            }
            if (p2pInfo.groupFormed) {
                // start new connection
                if (p2pInfo.isGroupOwner) {
                    this.Connection = new ChatServer(this.Receiver);
                } else {
                    this.Connection = new ChatClient(p2pInfo.groupOwnerAddress, this.Receiver);
                }
            }
        }
    }

    private static boolean areInfosEquals(WifiP2pInfo info1, WifiP2pInfo info2) {
        return (info1.groupFormed == info2.groupFormed) && (info1.isGroupOwner == info2.isGroupOwner) && (info1.groupOwnerAddress.equals(info2.groupOwnerAddress));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.ServiceBinder;
    }

    private final IBinder ServiceBinder = new ChatBinder();

    public class ChatBinder extends Binder {

        public boolean IsConnected() {
            return false;
        }

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

    private ReceiveCallback Receiver = new ReceiveCallback() {
        @Override
        public void ReceiveMessage(ChatMessage message) {

        }
    };

    private void addMessageNotification(ChatMessage message) {

    }
}
