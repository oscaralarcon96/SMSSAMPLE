package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class inv_informacion_general extends AppCompatActivity {
    private ProgressDialog pd = null;

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
        TextView tv_u_aprox = (TextView) findViewById(R.id.tv_u_aprox);
        TextView tv_u_contadas = (TextView) findViewById(R.id.tv_u_contadas);
        TextView tv_u_diferencia = (TextView) findViewById(R.id.tv_u_diferencia);
        ProgressBar pb_porcentaje = (ProgressBar)  findViewById(R.id.pb_porcentaje);


        pd = ProgressDialog.show(inv_informacion_general.this, "Procesando", "Espere unos segundos...", true, false);
        tv_avance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           // tv_avance.setBackgroundResource(R.drawable.btn_color_redondo);tv_unidades.setBackgroundResource(R.drawable.imput_black);
          // tv_avance.setTextColor(Color.parseColor("#FFFFFF"));tv_unidades.setTextColor(Color.parseColor("#000000"));
           rl_avance.setVisibility(View.VISIBLE);
           rl_unidades.setVisibility(View.GONE);
            }

        });
        tv_unidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  tv_unidades.setBackgroundResource(R.drawable.btn_color_redondo);tv_avance.setBackgroundResource(R.drawable.imput_black);
              //  tv_unidades.setTextColor(Color.parseColor("#FFFFFF"));tv_avance.setTextColor(Color.parseColor("#000000"));
                rl_avance.setVisibility(View.GONE);
               rl_unidades.setVisibility(View.VISIBLE);

            }
        });


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
                pd.dismiss();
                e.printStackTrace();
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    inv_informacion_general.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int total_items = 0;
                                //ARREGLO 1
                                JSONArray sample = new JSONArray(myResponse);
                                JSONArray arreglo1_1 = sample.getJSONArray(0);
                                JSONObject arreglo1_2 = arreglo1_1.getJSONObject(0);
                                JSONArray tienda = new JSONArray(arreglo1_2.getString("partner_id"));
                                JSONArray arreglo2_1 = sample.getJSONArray(1);
                                //JSONArray tienda = new JSONArray(arreglo1_2.getString("items_id"));
                                //Toast.makeText(getApplicationContext(), String.valueOf(items_zona.length()), Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < arreglo2_1.length(); i++) {
                                    JSONObject arreglo2_2 = arreglo2_1.getJSONObject(i);
                                    JSONArray items_zona = new JSONArray(arreglo2_2.getString("item_ids"));
                                    if(arreglo2_2.getString("status").equals("cerrado")){
                                    total_items = total_items + items_zona.length();}
                                }
                                int valuesample = arreglo1_2.getInt("item_aprox");
                                int value =((total_items *100) / arreglo1_2.getInt("item_aprox"));
                                //tv_porcentaje.setText(String.valueOf(total_items));
                                tv_porcentaje.setText(String.valueOf(value)+"%");
                                tv_tienda.setText(tienda.getString(1));
                                tv_ciudad.setText("Ciudad \n"+arreglo1_2.getString("ciudad"));
                                tv_fa.setText("Fecha Apertura \n"+arreglo1_2.getString("fecha_apertura"));
                                tv_fc.setText("Fecha Cierre \n"+arreglo1_2.getString("fecha_cierre"));
                                tv_zonas.setText("Zonas Contadas: \n"+String.valueOf(arreglo2_1.length()));
                                pb_porcentaje.setProgress(value);
                                tv_u_aprox.setText(String.valueOf(arreglo1_2.getInt("item_aprox")));
                                tv_u_contadas.setText(String.valueOf(total_items));
                                tv_u_diferencia.setText(String.valueOf(((total_items) - arreglo1_2.getInt("item_aprox"))));
                                pd.dismiss();

                                String dtStart = (((arreglo1_2.getString("fecha_apertura")).replace("-","")).replace(":","").replace(" ",""))+"+0000";
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssZ");
                                SimpleDateFormat formatu = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                                try {
                                    Date date = format.parse(dtStart);
                                    System.out.println(date);
                                    String dateTime = formatu.format(date);
                                    System.out.println("Current Date Time : " + dateTime);
                                    tv_fa.setText("Fecha Apertura \n"+dateTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String dtStart2 = (((arreglo1_2.getString("fecha_cierre")).replace("-","")).replace(":","").replace(" ",""))+"+0000";
                                SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmssZ");
                                SimpleDateFormat formatu2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                                try {
                                    Date date = format2.parse(dtStart2);
                                    System.out.println(date);
                                    String dateTime = formatu2.format(date);
                                    System.out.println("Current Date Time : " + dateTime);
                                    tv_fc.setText("Fecha Cierre \n"+dateTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }







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