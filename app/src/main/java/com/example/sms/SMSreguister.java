package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SMSreguister extends AppCompatActivity {

    EditText edtCorreo, edtUsuario, edtPassword;
    Button btnReguistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreguister);

        edtUsuario = findViewById(R.id.edtNomUsuario);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtPassword = findViewById(R.id.edtPassword);
        btnReguistro= (Button) findViewById(R.id.btnreguister);
        btnReguistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()){
                    Toast.makeText(SMSreguister.this, "OK", Toast.LENGTH_SHORT).show();
                    String name = edtUsuario.getText().toString();
                    String correo = edtCorreo.getText().toString();
                    String pass = edtPassword.getText().toString();

                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody = new FormBody.Builder().add("name", name).add("correo", correo).add("pass", pass).build();
                    try {
                        Request request = new Request.Builder().url("").post(formbody).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SMSreguister.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String mMessage = response.body().string();

                                if(response.code()==200){
                                    Intent intent = new Intent(SMSreguister.this, SMSlogin.class);
                                    startActivity(intent);

                                }else if (response.code()==400){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(SMSreguister.this, "mal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    }catch (Exception e){

                    }
                }
            }
        });
    }

    public boolean validar(){
        boolean r = true;
        String c1 = edtCorreo.getText().toString();
        String c2 = edtUsuario.getText().toString();
        String c3 = edtPassword.getText().toString();
        if(c1.isEmpty()){
            edtCorreo.setError("Este campo Correo esta vacio");
            r = false;
        }else if (!PatternsCompat.EMAIL_ADDRESS.matcher(c1).matches()){
            edtCorreo.setError("Correo invalido");
            r = false;
        }
        if (c2.isEmpty()){
            edtUsuario.setError("Este campo Usuario esta vacio");
            r = false;
        }
        if(c3.isEmpty()){
            edtPassword.setError("Este campo Password esta vacio");
            r = false;
        }
        return r;
    }
}