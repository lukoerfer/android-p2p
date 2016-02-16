package de.lukaskoerfer.androidp2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

public class WifiP2pChangesReceiver extends BroadcastReceiver {

    public WifiP2pChangesReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                Intent peerServiceIntent = new Intent(context, PeerService.class);
                context.startService(peerServiceIntent);
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                // request connection info

                break;
        }
    }
}
