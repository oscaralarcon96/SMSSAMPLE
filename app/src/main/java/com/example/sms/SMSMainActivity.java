package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SMSMainActivity extends AppCompatActivity {
    Button ir_login, ir_reguister, ir_parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsmain);
        ir_login=(Button) findViewById(R.id.ir_login);
        ir_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View login) {
                Intent intent = new Intent(SMSMainActivity.this, SMSlogin.class);
                startActivity(intent);
            }
        });
        ir_reguister=(Button) findViewById(R.id.ir_reguister);
        ir_reguister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View reguistrate) {
                Intent intent2 = new Intent(SMSMainActivity.this, SMSreguister.class);
                startActivity(intent2);
            }
        });

        ir_parametros=(Button) findViewById(R.id.ir_parametros);
        ir_parametros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(SMSMainActivity.this, Parametros.class);
                startActivity(intent2);
            }
        });

    }
}