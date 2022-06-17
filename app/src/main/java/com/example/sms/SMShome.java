package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SMShome extends AppCompatActivity {
    Button btncode, btnstatus, btnadmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smshome);
        btncode = findViewById(R.id.button2);
        btncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMShome.this, SMScomprobarZona.class);
                startActivity(intent);
            }
        });

        btnstatus = findViewById(R.id.btnstatus);
        btnstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMShome.this, SMScomprobarZona.class);
                startActivity(intent);
            }
        });

        btnadmin = findViewById(R.id.btnupdate);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMShome.this, SMScomprobarZona.class);
                startActivity(intent);
            }
        });
    }
}