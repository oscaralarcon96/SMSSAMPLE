package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SMScomprobarZona extends AppCompatActivity {

    private ArrayList<String> telefonos; //listview
    private ArrayList<String> telefonos2; //listview
    private ArrayAdapter<String> adaptador1; //listview
    private ArrayAdapter<String> adaptador2; //listview
    private ListView lv1, lv2; //listview

    private ListView listview;
    private ArrayList<String> names;

    Button btnscanner, btscannerEnvio, btngeneral, btnagrupado;
    Boolean ctrlbtn;
    EditText edtscanner;
    String requestactual="";
    ArrayList<String> lista = new ArrayList<>();
    ArrayList<String> lista2 = new ArrayList<>();
    TextView tv, tv2, tv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smscomprobar_zona);
        telefonos=new ArrayList<String>();
        telefonos2=new ArrayList<String>();
        adaptador1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,telefonos); //listview
        adaptador2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,telefonos2); //listview
        tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        btnscanner = findViewById(R.id.btnscanner);
        edtscanner = findViewById(R.id.editTextscanner);
        btscannerEnvio = findViewById(R.id.buttonenviar);
        btngeneral = findViewById(R.id.general);
        btnagrupado = findViewById(R.id.agrupado);
        ctrlbtn = true;


        listview = (ListView) findViewById(R.id.listaV);

        btngeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctrlbtn = true;
                controladorboton();
            }
        });

        btnagrupado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctrlbtn = false;
                controladorboton();
            }
        });

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
                    tv.setText("");
                    tv2.setText("");
                    tv3.setText("");
                    Toast.makeText(SMScomprobarZona.this, "Codigo fue enviado", Toast.LENGTH_SHORT).show();

                    //AQUI COMIENZA MI C    ODIGO PARA LLENAR LISTVIEW


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

                                        tv3.setText(edtscanner.getText().toString());
                                        edtscanner.setText("");
                                        requestactual = myResponse;

                                        // Toast.makeText(getApplicationContext(), myResponse, Toast.LENGTH_SHORT).show();
                                        try {
                                            JSONArray sample=new JSONArray(myResponse);

                                            //ARREGLO 1 INICIA
                                                JSONArray cambio = sample.getJSONArray(0);
                                                JSONObject cambio3 = cambio.getJSONObject(0);

                                                tv.setText("Unidades:\n"+cambio3.getString("total_items"));

                                            //ARREGLO 1 TERMINA

                                            //ARREGLO 2 INICIA
                                            JSONArray cambio1 = sample.getJSONArray(1);
                                            telefonos.clear();
                                            telefonos2.clear();
                                            lista.clear();
                                            lista2.clear();
                                            //lista general
                                            for(int i=0;i<cambio1.length();i++) {
                                                JSONObject cambio2 = cambio1.getJSONObject(i);
                                                //JSONArray objectfi=cambio2.getJSONArray(0);
                                                //JSONObject objectfi=cambio2.getJSONObject("[0]id");
                                                telefonos.add(cambio2.getString("codigo") + "\n" + cambio2.getString("co_int") + "\n" + cambio2.getString("descrip"));
                                               // telefonos2.add(cambio2.getString("codigo") + "\n" + cambio2.getString("co_int") + "\n" + cambio2.getString("descrip"));
                                                //cadena2[0] = cambio3.getString("usuario_nombre");
                                                tv.setText("");
                                                tv.setText("Unidades:\n"+ (i+1));
                                                //lista agrupada
                                                boolean existe = lista.contains(cambio2.getString("co_int"));
                                                if(!(existe)){
                                                    lista.add(cambio2.getString("co_int"));
                                                    lista2.add("1");
                                                    telefonos2.add(cambio2.getString("codigo") + "\n" + cambio2.getString("co_int") + "\n" + cambio2.getString("descrip"));
                                                }else{
                                                    String busqueda = cambio2.getString("co_int");
                                                    int indice = lista.indexOf(busqueda);
                                                    int indice2 = 1 + Integer.parseInt(lista2.get(indice));
                                                    lista2.set(indice, String.valueOf(indice2));
                                                }

                                            }

                                            for(int i=0;i<lista.size();i++) {
                                                //telefonos2.set(i, telefonos2.get(i)+"\nCantidad: "+ lista2.get(i));
                                                lista2.set(i, "Cant:\n"+lista2.get(i));

                                            }


                                            ctrlbtn = false;
                                            controladorboton();
                                            JSONObject cambio2 = cambio1.getJSONObject(0);
                                            tv2.setText("Usuario:\n"+cambio2.getString("usuario_nombre"));
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

    public boolean controladorboton(){
        if(requestactual==""){
            Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT).show();}else{
        if(ctrlbtn){
            //BOTON GENERAL
            int myColor = Color.parseColor("#A2224C");
            btngeneral.setBackgroundColor(myColor);
            btngeneral.setTextColor(Color.WHITE);
            //BOTON AGRUPADO
            btnagrupado.setBackgroundColor(Color.WHITE);
            btnagrupado.setTextColor(Color.BLACK);
            lv2=findViewById(R.id.listaV); //listview
            adaptador1.notifyDataSetChanged();
            lv2.setAdapter(adaptador1); //listview


        }else{
            //BOTON GENERAL
            int myColor = Color.parseColor("#A2224C");
            btnagrupado.setBackgroundColor(myColor);
            btnagrupado.setTextColor(Color.WHITE);
            //BOTON AGRUPADO
            btngeneral.setBackgroundColor(Color.WHITE);
            btngeneral.setTextColor(Color.BLACK);


            lv1=findViewById(R.id.listaV); //listview
            adaptador1.notifyDataSetChanged();
            lv1.setAdapter(adaptador1); //listview

            MyAdapter myAdapter = new MyAdapter(getApplicationContext(), R.layout.list_item, telefonos2,lista2);
            listview.setAdapter(myAdapter);

        }
    }
        return true;

    }
}