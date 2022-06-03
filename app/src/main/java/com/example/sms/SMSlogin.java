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

public class SMSlogin extends AppCompatActivity {
    //declaramos las id de los campos de texto y el boton pertenecientes a la pantalla de login
    EditText edtUsuario, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //agregamos el layout
        setContentView(R.layout.activity_smslogin);
        //asignamos los valores de los campos a las variables
        edtUsuario=(EditText) findViewById(R.id.edtUsuario);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        //creamos el evento
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //agregamos y convertimos los valores capturados a las nuevas variables
                String password = edtPassword.getText().toString().trim();
                String name = edtUsuario.getText().toString().trim();
                //hacemos una comprobacion para verificar que los campos no esten vacios
                if (validar() /*&& password != null*/){
                    //Creamos la variable y el cuerpo de consulta
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formbody = new FormBody.Builder().add("name", name).build();//.add("password", password).build();
                    try{
                        //Ruta para realizar la consulta
                        Request request = new Request.Builder().url("http://181.198.202.181:8082/tfi/usuario").post(formbody).build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    //cambiar e.getMessage por agun comentario para que no se vea cual es el error
                                    public void run() {
                                        Toast.makeText(SMSlogin.this, "Fallo al conectarse", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String mMessage = response.body().string();

                                if(response.code()==200){
                                    Intent intent = new Intent(SMSlogin.this, SMShome.class);
                                    startActivity(intent);

                                }else if (response.code()==400){
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(SMSlogin.this, "Usuario o contraseña no encontrados", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }






                        });

                    }catch (Exception e){

                    }



                }else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SMSlogin.this, "Los campos estan vacios", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }



    public boolean validar(){
        boolean r = true;
        String c2 = edtUsuario.getText().toString();
        String c3 = edtPassword.getText().toString();
        /*if(c1.isEmpty()){
            edtCorreo.setError("Este campo Correo esta vacio");
            r = false;
        }else if (!PatternsCompat.EMAIL_ADDRESS.matcher(c1).matches()){
            edtCorreo.setError("Correo invalido");
            r = false;
        }*/
        if (c2.isEmpty()){
            edtUsuario.setError("Este campo Usuario esta vacio");
            r = false;
        }
        /*if(c3.isEmpty()){
            edtPassword.setError("Este campo Password esta vacio");
            r = false;
        }*/
        return r;
    }
}