package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SMScomprobarZona extends AppCompatActivity {

    private ArrayList<String> telefonos; //listview
    private ArrayAdapter<String> adaptador1; //listview
    private ListView lv1; //listview

    Button btnscanner, btscannerEnvio;
    EditText edtscanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscomprobar_zona);

        telefonos=new ArrayList<String>();
        adaptador1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,telefonos); //listview
        lv1=(ListView)findViewById(R.id.listaV); //listview
        lv1.setAdapter(adaptador1); //listview
        final String[] cadena1 = new String[1];
        final String[] cadena2 = new String[1];
        TextView tv = (TextView)findViewById(R.id.textView);
        TextView tv2 = (TextView)findViewById(R.id.textView2);
        btnscanner = findViewById(R.id.btnscanner);
        edtscanner = findViewById(R.id.editTextscanner);
        btscannerEnvio = findViewById(R.id.buttonenviar);
        btnscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(SMScomprobarZona.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector-Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        btscannerEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()){
                    Toast.makeText(SMScomprobarZona.this, "Codigo fue enviado", Toast.LENGTH_SHORT).show();

                    //AQUI COMIENZA MI CODIGO PARA LLENAR LISTVIEW


                    OkHttpClient client = new OkHttpClient();

                    String url = "http://181.198.202.181:8082/tfi/consultarzona?name="+ edtscanner.getText().toString()+"&inv=1&user=Moises";

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

                                SMScomprobarZona.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtscanner.setText("");
                                        // Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();
                                        try {
                                            JSONArray sample=new JSONArray(myResponse);

                                            //ARREGLO 1 INICIA
                                                JSONArray cambio = sample.getJSONArray(0);
                                                JSONObject cambio3 = cambio.getJSONObject(0);

                                                tv.setText(cambio3.getString("total_items"));



                                            //ARREGLO 1 TERMINA

                                            //ARREGLO 2 INICIA
                                            JSONArray cambio1 = sample.getJSONArray(1);
                                            telefonos.clear();
                                            for(int i=0;i<cambio1.length();i++) {
                                                JSONObject cambio2 = cambio1.getJSONObject(i);
                                                //JSONArray objectfi=cambio2.getJSONArray(0);
                                                //JSONObject objectfi=cambio2.getJSONObject("[0]id");
                                                telefonos.add(cambio2.getString("codigo") + "\n" + cambio2.getString("co_int") + "\n" + cambio2.getString("descrip"));
                                                //cadena2[0] = cambio3.getString("usuario_nombre");
                                            }
                                            adaptador1.notifyDataSetChanged();
                                            JSONObject cambio2 = cambio1.getJSONObject(0);
                                            tv2.setText(cambio2.getString("usuario_nombre"));
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



                }else {
                    Toast.makeText(SMScomprobarZona.this, "No existe codigo de zona", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Lector Cancel", Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                edtscanner.setText(result.getContents());

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }


    public boolean validar(){
        boolean r = true;
        String c2 = edtscanner.getText().toString();
        if (c2.isEmpty()){
            edtscanner.setError("Este campo esta vacio");
            r = false;
        }
        return r;
    }
}