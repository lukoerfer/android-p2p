package de.lukaskoerfer.p2pchat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.lukaskoerfer.p2pchat.data.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private P2pChatApplication Application;

    private TextView lblState;
    private Button btnPeers;
    private ListView listHistory;
    private EditText txtInput;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_chat);

        this.Application = (P2pChatApplication) getApplication();

        this.lblState = (TextView)findViewById(R.id.lblState);
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
                String text = txtInput.getText().toString();
                txtInput.getText().clear();
                ChatMessage message = new ChatMessage(new ClientInfo("deviceAddress", "username"), text);
                if (ChatActivity.this.Binder != null) {
                    boolean success = ChatActivity.this.Binder.sendMessage(message);
                    if (!success) {
                        Toast.makeText(ChatActivity.this, "Could not send message!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private Intent generateServiceIntent() {
        Intent serviceIntent = new Intent(this, P2pChatService.class);
        serviceIntent.setAction("CHAT_ACTIVITY");
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
        if (this.Binder != null) {
            this.Binder.unregisterActivity();
        }
        this.unbindService(this.ServiceConn);
    }

    private P2pChatService.ChatBinder Binder;

    private ServiceConnection ServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChatActivity.this.Binder = (P2pChatService.ChatBinder) service;
            ChatActivity.this.Binder.registerActivity(ChatActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            ChatActivity.this.Binder = null;
        }
    };

    private void setErrorState(int errorState) {
        this.lblState.setVisibility((errorState == 0) ? View.GONE : View.VISIBLE);
        String[] errorMessages = Resources.getSystem().getStringArray(R.array.error_messages);
        this.lblState.setText(errorMessages[errorState + 1]);
    }

    private void addMessages(List<ChatMessage> messages) {

    }

    private void openPeerActivity() {
        Intent peerActivityIntent = new Intent(this, PeerActivity.class);
        this.startActivity(peerActivityIntent);
    }
}
