package de.lukaskoerfer.p2pchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.util.Collection;

public class PeerService extends Service implements WifiP2pManager.PeerListListener {

    private P2pChatApplication Application;

    public PeerService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.Application = (P2pChatApplication) getApplication();
        this.Application.P2pHandler.Manager.requestPeers(this.Application.P2pHandler.Channel, this);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.ServiceBinder;
    }

    private final IBinder ServiceBinder = new PeerBinder();

    public class PeerBinder extends Binder {
        public WifiP2pDeviceList getPeers() {
            return PeerService.this.lastDeviceList;
        }
    }

    private WifiP2pDeviceList lastDeviceList = new WifiP2pDeviceList();

    @Override
    public void onPeersAvailable(WifiP2pDeviceList deviceList) {
        Collection<WifiP2pDevice> devices = deviceList.getDeviceList();
        if (!this.lastDeviceList.getDeviceList().containsAll(devices)) {
            Intent mainActivityIntent = buildPeerActivityIntent();
            if (this.Application.PeerActivityOpen) {
                this.startActivity(mainActivityIntent);
            } else {
                this.showNotification(mainActivityIntent);
            }
        }
        this.lastDeviceList = deviceList;
    }

    private Intent buildPeerActivityIntent() {
        Intent intent = new Intent(this, PeerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void showNotification(Intent peerActivityIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Peers found!");
        builder.setContentText("Click here to show the peers ...");
        PendingIntent peerActivityPending = PendingIntent.getActivity(this, 0, peerActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(peerActivityPending);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
