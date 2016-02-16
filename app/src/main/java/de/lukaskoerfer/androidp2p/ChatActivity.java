package de.lukaskoerfer.androidp2p;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    private TextView lblHistory;
    private EditText txtInput;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.lblHistory = (TextView)findViewById(R.id.lblHistory);
        this.lblHistory.setMovementMethod(new ScrollingMovementMethod());

        this.txtInput = (EditText)findViewById(R.id.txtInput);

        this.btnSend = (Button)findViewById(R.id.btnSend);
        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = txtInput.getText().toString();
                sendMessage(message);
                lblHistory.append("\nMe: " + message);
            }
        });

        if (getIntent().hasExtra("MESSAGE")) {
            String message = getIntent().getStringExtra("MESSAGE");
            lblHistory.append("\nOther: " + message);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().hasExtra("MESSAGE")) {
            String message = getIntent().getStringExtra("MESSAGE");
            lblHistory.append("\nOther: " + message);
        }
    }

    private void sendMessage(String message) {
        Intent serviceIntent = new Intent(this, ConnectionService.class);
        serviceIntent.putExtra("ACTION", "SEND");
        serviceIntent.putExtra("MESSAGE", message);
        startService(serviceIntent);
    }
}
