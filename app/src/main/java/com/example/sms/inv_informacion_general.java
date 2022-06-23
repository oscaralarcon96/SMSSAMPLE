package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class inv_informacion_general extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_informacion_general);
        RelativeLayout rl_avance = (RelativeLayout) findViewById(R.id.rl_avance);
        RelativeLayout rl_unidades = (RelativeLayout) findViewById(R.id.rl_unidades);
        TextView tv_tienda = (TextView) findViewById(R.id.tv_tienda);
        TextView tv_fa = (TextView) findViewById(R.id.tv_fa);
        TextView tv_fc = (TextView) findViewById(R.id.tv_fc);
        TextView tv_ciudad = (TextView) findViewById(R.id.tv_ciudad);
        TextView tv_zonas = (TextView) findViewById(R.id.tv_zonas);
        TextView tv_avance = (TextView) findViewById(R.id.tv_avance);
        TextView tv_unidades = (TextView) findViewById(R.id.tv_unidades);
        TextView tv_porcentaje = (TextView) findViewById(R.id.tv_porcentaje);
        ProgressBar pb_porcentaje = (ProgressBar)  findViewById(R.id.pb_porcentaje);

        OkHttpClient client = new OkHttpClient();
        SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
        final int id_inv = parametros.getInt("id_inv", 1);
        final String ipet = parametros.getString("ip", "181.198.202.181");
        final String portet = parametros.getString("port", "8082");
        String url = "http://" + ipet + ":" + portet + "/tfi/consultarinv?name=inv";

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

                    inv_informacion_general.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //ARREGLO 1
                                JSONArray sample = new JSONArray(myResponse);
                                JSONArray arreglo1_1 = sample.getJSONArray(0);
                                JSONObject arreglo1_2 = arreglo1_1.getJSONObject(0);
                                JSONArray tienda = new JSONArray(arreglo1_2.getString("partner_id"));
                                JSONArray arreglo2_1 = sample.getJSONArray(1);
                                tv_tienda.setText(tienda.getString(1));
                                tv_ciudad.setText("Ciudad \n"+arreglo1_2.getString("ciudad"));
                                tv_fa.setText("Fecha Apertura \n"+arreglo1_2.getString("fecha_apertura"));
                                tv_fc.setText("Fecha Cierre \n"+arreglo1_2.getString("fecha_cierre"));
                                tv_zonas.setText("Zonas Contadas: \n"+String.valueOf(arreglo2_1.length()));







                              // Toast.makeText(getApplicationContext(), String.valueOf(arreglo2_1.length()), Toast.LENGTH_SHORT).show();
                                //ARREGLO 1
                                /*JSONArray cambio = sample.getJSONArray(0);
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


                                }*/
                                //ARREGLO 2 TERMINA
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });






    }
}