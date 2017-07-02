package de.lukaskoerfer.p2pchat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PeerActivity extends AppCompatActivity {

    private P2pChatApplication Application;

    private PeerDeviceAdapter peerDeviceAdapter;

    private Button btnSearch;
    private ListView listDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_peer);

        this.Application = (P2pChatApplication) this.getApplication();

        this.btnSearch = (Button)(findViewById(R.id.btnSearch));
        this.listDevices = (ListView)(findViewById(R.id.listDevices));

        this.peerDeviceAdapter = new PeerDeviceAdapter(this, new ArrayList<PeerDevice>());
        this.listDevices.setAdapter(this.peerDeviceAdapter);

        this.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeerActivity.this.startPeerDiscovery();
            }
        });

        this.listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PeerDevice peerDevice = (PeerDevice) PeerActivity.this.peerDeviceAdapter.getItem(position);
                PeerActivity.this.connectPeer(peerDevice.getDevice());
            }
        });

    }

    private Intent generateServiceIntent() {
        Intent serviceIntent = new Intent(this, P2pChatService.class);
        serviceIntent.setAction("PEER_ACTIVITY");
        return serviceIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.bindService(this.generateServiceIntent(), this.ServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.Binder.unregisterActivity();
        this.unbindService(this.ServiceConn);
    }

    private P2pChatService.PeerBinder Binder;

    private ServiceConnection ServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            PeerActivity.this.Binder = (P2pChatService.PeerBinder) binder;
            PeerActivity.this.Binder.registerActivity(PeerActivity.this);
            PeerActivity.this.updateDevices();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            PeerActivity.this.Binder = null;
        }
    };

    public void updateDevices() {
        if (this.Binder != null) {
            this.peerDeviceAdapter.updatePeerDevices(this.Binder.getDevices());
        }
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

}
