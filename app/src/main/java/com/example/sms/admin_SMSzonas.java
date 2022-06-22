package com.example.sms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class admin_SMSzonas extends AppCompatActivity {

    private ArrayList<String> items_list; //listview
    private ArrayAdapter<String> items_adaptador; //listview
    private ListView listview;
    List<String> id_item = new ArrayList<String>();
    String id_zona ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_smszonas);
        items_list = new ArrayList<String>();
        items_adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items_list); //listview
        listview = (ListView) findViewById(R.id.listaV);
        Bundle extras = getIntent().getExtras();
        String zona_cache =(String) extras.get("zona_cache");
        //Toast.makeText(admin_SMSzonas.this, zona_cache, Toast.LENGTH_SHORT).show();
        TextView tv_zona = (TextView) findViewById(R.id.tv_zona);
        TextView tv_usuario = (TextView) findViewById(R.id.tv_usuario);
        TextView tv_totalitem = (TextView) findViewById(R.id.tv_totalitems);
        Button btnstatus = (Button) findViewById(R.id.btn_status);
        Button btn_eliminar_zona = (Button) findViewById(R.id.btn_eliminar_zona);

        tv_zona.setText(zona_cache);


        btn_eliminar_zona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(admin_SMSzonas.this);
                dialogo1.setTitle("Atencion");
                dialogo1.setMessage("Desea Eliminar la zona "+tv_zona.getText().toString());
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        OkHttpClient client = new OkHttpClient();
                        SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
                        final int id_inv = parametros.getInt("id_inv", 1);
                        final String ipet = parametros.getString("ip", "181.198.202.181");
                        final String portet = parametros.getString("port", "8082");
                        String url = "http://" + ipet + ":" + portet + "/tfi/eliminarzona?name="+tv_zona.getText().toString();

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

                                    admin_SMSzonas.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();

                                        }
                                    });
                                }
                            }
                        });
                        finish();

                    }


                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();



            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){
                                            //Toast.makeText(getApplicationContext(), id_item.get(position), Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(admin_SMSzonas.this);
                                            int item_restante;
                                            item_restante = Integer.parseInt(tv_totalitem.getText().toString())-1;
                                            dialogo1.setTitle("Atencion");
                                            dialogo1.setMessage("Eliminar Item? Items restantes "+item_restante);
                                            dialogo1.setCancelable(false);
                                            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                    OkHttpClient client = new OkHttpClient();
                                                    SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
                                                    final int id_inv = parametros.getInt("id_inv", 1);
                                                    final String ipet = parametros.getString("ip", "181.198.202.181");
                                                    final String portet = parametros.getString("port", "8082");
                                                    String url = "http://" + ipet + ":" + portet + "/tfi/eliminaritem?name="+id_item.get(position);

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

                                                                admin_SMSzonas.this.runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                         Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();
                                                                        items_list.remove(position);
                                                                        listview.setAdapter(items_adaptador);
                                                                        tv_totalitem.setText(String.valueOf(item_restante));

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                    finish();
                                                    startActivity(getIntent());


                                                }


                                            });
                                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                }
                                            });
                                            dialogo1.show();

                                        }
                                    });

        btnstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
                final int id_inv = parametros.getInt("id_inv", 1);
                final String ipet = parametros.getString("ip", "181.198.202.181");
                final String portet = parametros.getString("port", "8082");
                String url;
                if(btnstatus.getText().equals("cerrado")){
                    btnstatus.setText("abierto");
                    url = "http://" + ipet + ":" + portet + "/tfi/abrirzona?name="+id_zona+"&inv=1&user=us1";

                }else{
                    btnstatus.setText("cerrado");
                    url = "http://" + ipet + ":" + portet + "/tfi/cerrarzona?name="+id_zona+"&inv=1&user=us1";
                }


                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();

                           admin_SMSzonas.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });

            }
        });

        OkHttpClient client = new OkHttpClient();
        SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
        final int id_inv = parametros.getInt("id_inv", 1);
        final String ipet = parametros.getString("ip", "181.198.202.181");
        final String portet = parametros.getString("port", "8082");
        String url = "http://" + ipet + ":" + portet + "/tfi/consultarzonafull?name="+zona_cache+"&inv=1&user=us1";

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

                    admin_SMSzonas.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray sample = new JSONArray(myResponse);
                                //ARREGLO 1
                                JSONArray cambio = sample.getJSONArray(0);
                                JSONObject cambio3 = cambio.getJSONObject(0);
                                JSONArray user = new JSONArray(cambio3.getString("usuario"));
                                String user2 = user.getString(1);
                                tv_usuario.setText(user2);
                                btnstatus.setText(cambio3.getString("status"));
                                id_zona = cambio3.getString("id");
                                //Toast.makeText(getApplicationContext(), cambio2.getString("status"), Toast.LENGTH_SHORT).show();
                                JSONArray arreglo_item = sample.getJSONArray(1);
                                tv_totalitem.setText(String.valueOf(arreglo_item.length()));
                                //ARREGLO 2 COMIENZA
                                for(int i=0;i<arreglo_item.length();i++) {
                                    JSONObject cambio2 = arreglo_item.getJSONObject(i);
                                    id_item.add(cambio2.getString("id"));

                                    items_list.add(cambio2.getString("codigo") + "\n" + cambio2.getString("co_int") + "\n" + cambio2.getString("descrip"));
                                    listview.setAdapter(items_adaptador);


                                }
                                //ARREGLO 2 TERMINA
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });




    }
}