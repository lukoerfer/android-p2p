package de.lukaskoerfer.p2pchat;

import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PeerActivity extends AppCompatActivity {

    private P2pChatApplication Application;

    private Button btnSearch;
    private ListView listDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_peer);

        this.Application = (P2pChatApplication) this.getApplication();

        this.btnSearch = (Button)(findViewById(R.id.btnSearch));
        this.listDevices = (ListView)(findViewById(R.id.listDevices));

        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeerActivity.this.startPeerDiscovery();
            }
        });

        this.listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PeerActivity.this.connectPeer(PeerActivity.this.FoundPeers[position]);
            }
        });


    }

    private void connectPeer(WifiP2pDevice peer) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = peer.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        this.Application.P2pHandler.Manager.connect(this.Application.P2pHandler.Channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(PeerActivity.this, "Connecting to peer ...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int reason) {
                Toast.makeText(PeerActivity.this, "Peer connection failed with code " + Integer.toString(reason), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrievePeerList() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.Application.PeerActivityOpen = true;
        this.showFoundPeers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.Application.PeerActivityOpen = false;
    }

    private void startPeerDiscovery() {
        this.Application.P2pHandler.Manager.discoverPeers(this.Application.P2pHandler.Channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(PeerActivity.this, "Started P2P search ...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int reason) {
                Toast.makeText(PeerActivity.this, "P2P search failed with code " + String.valueOf(reason), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private WifiP2pDevice[] FoundPeers = new WifiP2pDevice[0];

    public void showFoundPeers() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for (WifiP2pDevice device : this.FoundPeers) {
            adapter.add(device.deviceName);
        }
        this.listDevices.setAdapter(adapter);
    }

}
