package de.lukaskoerfer.androidp2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    private WifiP2pHandler wifiP2pHandler;

    private TextView lblInfo;
    private Button btnSearchDevices;
    private ListView listDevices;
    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.wifiP2pHandler = WifiP2pHandler.Single(this);

        this.lblInfo = (TextView)(findViewById(R.id.lblInfo));
        this.btnSearchDevices = (Button)(findViewById(R.id.btnSearch));
        this.listDevices = (ListView)(findViewById(R.id.listDevices));
        this.btnTest = (Button)(findViewById(R.id.btnTest));

        this.btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiP2pHandler.Manager.discoverPeers(wifiP2pHandler.Channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Started P2P search ...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(MainActivity.this, "P2P search failed with code " + String.valueOf(reason), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private WifiP2pDevice[] PeerDevices = new WifiP2pDevice[0];

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        this.PeerDevices = new WifiP2pDevice[peers.getDeviceList().size()];
        int counter = 0;
        for (WifiP2pDevice device : peers.getDeviceList()) {
            this.PeerDevices[counter] = device;
            adapter.add(device.deviceName);
        }
        this.listDevices.setAdapter(adapter);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        String hostAddress = info.groupOwnerAddress.getHostAddress();
        this.lblInfo.setText(generateInfo(info.groupFormed, info.isGroupOwner, hostAddress));
        Intent serviceIntent = new Intent(this, ConnectionService.class);
        serviceIntent.putExtra("ACTION", "START");
        serviceIntent.putExtra("IS_SERVER", info.isGroupOwner);
        if (!info.isGroupOwner) {
            serviceIntent.putExtra("IP", hostAddress);
        }
        startService(serviceIntent);
    }

    private String generateInfo(boolean groupFormed, boolean groupOwner, String ip) {
        return "Is group formed? " + String.valueOf(groupFormed) +
                "\nAm i group owner? " + String.valueOf(groupOwner) +
                "\nGroup owner ip: " + ip;
    }

}
