package de.lukaskoerfer.p2pchat.helper;

import android.net.wifi.p2p.WifiP2pInfo;

/**
 * Created by Koerfer on 10.03.2016.
 */
public class WifiP2pInfoHelper {

    public static boolean areInfosEquals(WifiP2pInfo info1, WifiP2pInfo info2) {
        if (info1 == null || info2 == null) {
            return false;
        } else {
            return (info1.groupFormed == info2.groupFormed) && (info1.isGroupOwner == info2.isGroupOwner) && (info1.groupOwnerAddress.equals(info2.groupOwnerAddress));
        }
    }

}
