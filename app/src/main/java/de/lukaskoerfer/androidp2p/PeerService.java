package de.lukaskoerfer.androidp2p;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collection;

public class PeerService extends Service implements WifiP2pManager.PeerListListener {

    public PeerService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WifiP2pHandler wifiP2pHandler = WifiP2pHandler.Single(this);
        wifiP2pHandler.Manager.requestPeers(wifiP2pHandler.Channel, this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Collection<WifiP2pDevice> lastDeviceList = new ArrayList<WifiP2pDevice>();

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        Collection<WifiP2pDevice> devices = peers.getDeviceList();
        if (!this.lastDeviceList.containsAll(devices)) {
            showNotification();
        }
        this.lastDeviceList = devices;
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Peers found!");
        builder.setContentText("Click here to show the peers ...");
        Intent showPeersIntent = new Intent(this, MainActivity.class);
        PendingIntent showPeersPending = PendingIntent.getActivity(this, 0, showPeersIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(showPeersPending);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
