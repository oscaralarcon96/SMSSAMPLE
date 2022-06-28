package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class admin_prod extends AppCompatActivity {
    private ArrayList<String> prod_list, usuario_array,fecha_array; //listview
    //listview
    private ArrayAdapter<String> prod_adaptador; //listview
    private ListView listview;
    private ArrayList<Integer> cant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prod);
        prod_list= new ArrayList<String>();
        prod_adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, prod_list);
        listview = (ListView) findViewById(R.id.listaV);
        TextView tv_prod = (TextView) findViewById(R.id.editTextscanner);
        cant= new ArrayList<Integer>();
        usuario_array= new ArrayList<String>();
        fecha_array= new ArrayList<String>();

        OkHttpClient client = new OkHttpClient();
        SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
        final int id_inv = parametros.getInt("id_inv", 1);
        final String ipet = parametros.getString("ip", "181.198.202.181");
        final String portet = parametros.getString("port", "8082");
        String url = "http://" + ipet + ":" + portet + "/tfi/consultarzonatotal?name=" + id_inv;

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

                    admin_prod.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray sample = new JSONArray(myResponse);
                                for (int i = 0; i < sample.length(); i++) {
                                    JSONObject cambio2 = sample.getJSONObject(i);
                                    //JSONArray objectfi=cambio2.getJSONArray(0);
                                    //JSONObject objectfi=cambio2.getJSONObject("[0]id");
                                    JSONArray item_id = new JSONArray(cambio2.getString("item_ids"));
                                    JSONArray user = new JSONArray(cambio2.getString("usuario"));
                                    String user2 = user.getString(1);
                                    String dtStart = (((cambio2.getString("fecha_apertura")).replace("-","")).replace(":","").replace(" ",""))+"+0000";
                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssZ");
                                    SimpleDateFormat format_f = new SimpleDateFormat("yyyyMMddHH");
                                    String dateTime = null;
                                    Date date;
                                    try {
                                        date = format.parse(dtStart);
                                        dateTime = format_f.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //ArrayList<String> fechas = new ArrayList<>();
                                    // Le agregamos datos
                                    int indice = usuario_array.indexOf(user2);
                                    if (indice != -1) {} else {usuario_array.add(user2);}
                                    indice = fecha_array.indexOf(dateTime);
                                    if (indice != -1) {
                                        System.out.println("El elemento SÍ existe en la lista");
                                        int ind = indice;
                                        int suma = cant.get(ind);
                                        int suma2 = item_id.length() + suma;
                                        cant.set(ind, suma2);
                                    } else {
                                        fecha_array.add(dateTime);
                                        cant.add(item_id.length());

                                    }

                                    /*prod_list.add(cambio2.getString("display_name") + "\nFECHA: " + dateTime +
                                            "\nCANTIDAD: " + item_id.length() + "\nUSUARIO: " + user2);
                                   */ //lista agrupada


                                }

                                for(int l = 0; l < fecha_array.size() ; l++){
                                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
                                    SimpleDateFormat format_f = new SimpleDateFormat(
                                            "dd 'de' MMMM 'de' yyyy 'hora' HH", new Locale("es_ES"));
                                    

                                    Date date;
                                    String dateTime = null;
                                    try {
                                        date = format.parse(fecha_array.get(l));
                                        dateTime = format_f.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    prod_list.add("Hora/Fecha :"+dateTime+":00"
                                           // +"\n" +"Unidades contadas:"+ cant.get(l) +"\n" +"Usuarios Contando:"+ usuario_array.size()
                                            +"\n" +"Productividad: " +(cant.get(l)/usuario_array.size()) );
                                }

                                listview.setAdapter(prod_adaptador); //listview

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