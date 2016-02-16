package de.lukaskoerfer.androidp2p;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

/**
 * Created by Koerfer on 16.02.2016.
 */
public class WifiP2pHandler {

    // Singleton
    private static WifiP2pHandler _Single;
    public static WifiP2pHandler Single(Context context) {
        if (WifiP2pHandler._Single == null) {
            WifiP2pHandler._Single = new WifiP2pHandler(context);
        }
        return WifiP2pHandler._Single;
    }

    public WifiP2pManager Manager;
    public WifiP2pManager.Channel Channel;

    public WifiP2pHandler(Context context) {
        this.Manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        this.Channel = this.Manager.initialize(context, Looper.getMainLooper(), null);
    }
}
