package de.lukaskoerfer.p2pchat;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by Koerfer on 10.03.2016.
 */
public class PeerDevice {

    private WifiP2pDevice Device;
    private boolean AlreadyInConversation;

    public PeerDevice(WifiP2pDevice device, boolean alreadyInConversation) {
        this.Device = device;
        this.AlreadyInConversation = alreadyInConversation;
    }

    public WifiP2pDevice getDevice() {
        return this.Device;
    }

    public boolean isAlreadyInConversation() {
        return this.AlreadyInConversation;
    }
}
