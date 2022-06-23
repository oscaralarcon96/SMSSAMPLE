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
        Bundle extras = getIntent().getExtras();
        String usuario =(String) extras.get("usuario");

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
                Intent intent = new Intent(SMShome.this, inv_informacion_general.class);
                startActivity(intent);
            }
        });

        btnadmin = findViewById(R.id.btnupdate);
        btnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMShome.this, admin_resumen_zonas.class);
                startActivity(intent);
            }
        });

        if(usuario.equals("root")){
            btnadmin.setVisibility(View.VISIBLE);
            btnstatus.setVisibility(View.VISIBLE);


        }
    }
}