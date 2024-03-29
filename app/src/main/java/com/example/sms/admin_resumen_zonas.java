package com.example.sms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class admin_resumen_zonas extends AppCompatActivity {

    private ArrayList<String> zonas_list;
    private ArrayList<String> zonas_list_filter; //listview
    //listview
    private ArrayAdapter<String> zonas_adaptador; //listview
    private ArrayAdapter<String> zonas_adaptador_filtrado; //listview
    private ListView listview;
    EditText etsearch;
    List<String> testList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_resumen_zonas);
        zonas_list = new ArrayList<String>();
        zonas_list_filter = new ArrayList<String>();
        zonas_adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, zonas_list); //listview
        zonas_adaptador_filtrado = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, zonas_list_filter); //listview
        listview = (ListView) findViewById(R.id.listaV);
        etsearch = findViewById(R.id.editTextscanner);
        Button btn_actualizar = (Button) findViewById(R.id.btn_reload);


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());



            }
        });



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

                    admin_resumen_zonas.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray sample = new JSONArray(myResponse);
                                for (int i = 0; i < sample.length(); i++) {
                                    JSONObject cambio2 = sample.getJSONObject(i);
                                    //JSONArray objectfi=cambio2.getJSONArray(0);
                                    //JSONObject objectfi=cambio2.getJSONObject("[0]id");
                                    testList.add(cambio2.getString("display_name"));
                                    JSONArray item_id = new JSONArray(cambio2.getString("item_ids"));
                                    JSONArray user = new JSONArray(cambio2.getString("usuario"));
                                    String user2 = user.getString(1);
                                   zonas_list.add(cambio2.getString("display_name") + "\nESTATUS: " + cambio2.getString("status") +
                                           "\nCANTIDAD: " + item_id.length() + "\nUSUARIO: " + user2);
                                    //lista agrupada
                                    listview.setAdapter(zonas_adaptador); //listview
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

        //AQUI TERMINA MI CODIGO



        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Toast.makeText(getApplicationContext(), String.valueOf(i)+"|"+String.valueOf(i1)+"|"+String.valueOf(i2)+"|", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), String.valueOf(i)+"|"+String.valueOf(i1)+"|"+String.valueOf(i2)+"|", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                zonas_list_filter.clear();
                listview.setAdapter(zonas_adaptador_filtrado);
              /*  for(int j = 0; j < testList.size() ; j++) {
                    if(testList.get(j).substring(0,etsearch.getText().toString().length()).equals(etsearch.getText().toString())){
                zonas_list_filter.add(zonas_list.get(j));
            }
            listview.setAdapter(zonas_adaptador_filtrado);
                }*/if(etsearch.getText().toString().equals("")){
                    listview.setAdapter(zonas_adaptador);
                }else{
                int l=1;
                for(int j = 0; j < testList.size() ; j++) {
                    l=etsearch.getText().toString().length();
                    for(int k = 0; k < testList.get(j).length() + 1 - etsearch.getText().toString().length() ; k++){
                        String sample =testList.get(j).substring(k,l);
                        l++;
                        if(sample.equals(etsearch.getText().toString())){
                            zonas_list_filter.add(zonas_list.get(j));
                            listview.setAdapter(zonas_adaptador_filtrado);
                            break;
                        }
                       // if(){
                        //    zonas_list_filter.add(zonas_list.get(j));
                        //}
                    }
                    l=1;

                }}




                //Result.values = Filtered_Names;
                //Result.count = Filtered_Names.size();

                //zonas_adaptador.getFilter().filter(charSequence);
                //listview.setAdapter(zonas_adaptador);
                //  Toast.makeText(getApplicationContext(), String.valueOf(i)+"|"+String.valueOf(i1)+"|"+String.valueOf(i2)+"|", Toast.LENGTH_SHORT).show();
                //zonas_list_filter.addAll(zonas_list.fillArray());
              //  zonas_adaptador_filtrado.getFilter().filter(charSequence);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(admin_resumen_zonas.this, zonas_list_filter.get(i), Toast.LENGTH_SHORT).show();



//listview.setAdapter(zonas_adaptador);
                if(etsearch.getText().toString().equals("")) {

                    // Toast.makeText(admin_resumen_zonas.this, testList.get(i), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(admin_resumen_zonas.this, admin_SMSzonas.class);
                    //String zona_cache = null;
                    intent.putExtra("zona_cache", testList.get(i));
                    startActivity(intent);
                }else {
                    String[] parts = zonas_list_filter.get(i).split("\n");
                    Intent intent = new Intent(admin_resumen_zonas.this, admin_SMSzonas.class);
                    //String zona_cache = null;
                    intent.putExtra("zona_cache", parts[0]);
                    startActivity(intent);

                }
              //  Toast.makeText(admin_resumen_zonas.this, zonas_adaptador_filtrado.getItem(i) , Toast.LENGTH_SHORT).show();



            }
        });





    }





}