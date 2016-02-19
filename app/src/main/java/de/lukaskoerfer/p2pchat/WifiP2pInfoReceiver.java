package de.lukaskoerfer.p2pchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

public class WifiP2pInfoReceiver extends BroadcastReceiver {

    public WifiP2pInfoReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                this.startPeerService(context);
                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                this.startConnectionService(context);
                break;
        }
    }

    private void startPeerService(Context context) {
        Intent peerServiceIntent = new Intent(context, PeerService.class);
        context.startService(peerServiceIntent);
    }

    private void startConnectionService(Context context) {
        Intent connectionServiceIntent = new Intent(context, ChatService.class);
        context.startService(connectionServiceIntent);
    }
}
