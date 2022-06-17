package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Parametros extends AppCompatActivity {
/*
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametros);
        EditText edt_inv = (EditText) findViewById(R.id.inv);
        EditText edt_ip = (EditText) findViewById(R.id.ip);
        EditText edt_port = (EditText) findViewById(R.id.port);

        SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
        final int id_inv = parametros.getInt("id_inv", 1);
        final String ipet = parametros.getString("ip","181.198.202.181");
        final String portet = parametros.getString("port","8082");
        final String inv = parametros.getString("inv", "inv");
        edt_ip.setText(ipet);
        edt_port.setText(portet);
        edt_inv.setText(inv);

        Button salir = (Button) findViewById(R.id.regresar);
        salir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Button grabar = (Button) findViewById(R.id.grabar);
        grabar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {




                OkHttpClient client = new OkHttpClient();

                String url = "http://"+edt_ip.getText().toString()+":"+edt_port.getText().toString()+"/tfi/inv/"+edt_inv.getText().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            Parametros.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String respuesta[] = myResponse.split(":");
                                    if(respuesta[0].equals("ok")){
                                        SharedPreferences parametros= getSharedPreferences("Parametros", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = parametros.edit();
                                        editor.putInt("id_inv", Integer.parseInt(respuesta[1]));
                                        editor.putString("ip", edt_ip.getText().toString());
                                        editor.putString("port", edt_port.getText().toString());
                                        editor.putString("inv", edt_inv.getText().toString());
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(), "Datos Actualizado con exito" , Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), myResponse , Toast.LENGTH_LONG).show();
                                    }



                                }
                            });
                        }
                    }
                });






            }
        });



    }

}