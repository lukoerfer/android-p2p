package de.lukaskoerfer.p2pchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private P2pChatApplication Application;

    private Button btnPeers;
    private ListView listHistory;
    private EditText txtInput;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_chat);

        this.Application = (P2pChatApplication) getApplication();

        this.btnPeers = (Button)findViewById(R.id.btnPeers);
        this.listHistory = (ListView)findViewById(R.id.lblHistory);
        this.txtInput = (EditText)findViewById(R.id.txtInput);
        this.btnSend = (Button)findViewById(R.id.btnSend);

        this.btnPeers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.openPeerActivity();
            }
        });

        this.listHistory.setEmptyView(findViewById(R.id.txtEmptyHistory));

        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.Application.ChatActivityOpen = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.Application.ChatActivityOpen = false;
    }

    private void openPeerActivity() {
        Intent peerActivityIntent = new Intent(this, PeerActivity.class);
        this.startActivity(peerActivityIntent);
    }
}
