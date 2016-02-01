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
import android.widget.Toast;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener, WifiP2pManager.ConnectionInfoListener {

    private Button btnSearchDevices;
    private ListView listDevices;

    private WifiP2pManager p2pManager;
    private WifiP2pManager.Channel p2pChannel;

    private final IntentFilter p2pFilter = new IntentFilter();
    private WifiP2pReceiver p2pReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p2pManager = (WifiP2pManager) MainActivity.this.getSystemService(Context.WIFI_P2P_SERVICE);
        p2pChannel = p2pManager.initialize(this, getMainLooper(), null);

        this.p2pFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        this.p2pFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        this.btnSearchDevices = (Button)(findViewById(R.id.btnSearch));
        this.listDevices = (ListView)(findViewById(R.id.listDevices));

        this.btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.p2pManager.discoverPeers(MainActivity.this.p2pChannel, new WifiP2pManager.ActionListener() {
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

        this.listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiP2pDevice device = MainActivity.this.PeerDevices[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;
                config.wps.setup = WpsInfo.PBC;
                MainActivity.this.p2pManager.connect(MainActivity.this.p2pChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Starting P2P connection ...", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int reason) {
                        Toast.makeText(MainActivity.this, "P2P connection failed with code " + String.valueOf(reason), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.p2pReceiver = new WifiP2pReceiver();
        this.registerReceiver(this.p2pReceiver, this.p2pFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.p2pReceiver);
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
        if (info.groupFormed) {
            String hostAddress = info.groupOwnerAddress.getHostAddress();
            if (info.isGroupOwner) {
                Intent startService = new Intent(this, ConnectionService.class);
                startService.putExtra("CONN_ADDRESS", hostAddress);
                startService.putExtra("CONN_IS_SERVER", true);
                startService(startService);
            } else {
                Intent startService = new Intent(this, ConnectionService.class);
                startService.putExtra("CONN_ADDRESS", hostAddress);
                startService.putExtra("CONN_IS_SERVER", false);
                startService(startService);
            }
        }
    }

    private class WifiP2pReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                    if (MainActivity.this.p2pManager != null) {
                        MainActivity.this.p2pManager.requestPeers(MainActivity.this.p2pChannel, MainActivity.this);
                    }
                    break;
                case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                    NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                    if (networkInfo.isConnected() && MainActivity.this.p2pManager != null) {
                        MainActivity.this.p2pManager.requestConnectionInfo(MainActivity.this.p2pChannel, MainActivity.this);
                    }
                    break;
            }
        }
    };
}
