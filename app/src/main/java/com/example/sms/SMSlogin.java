package com.example.sms;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.PatternsCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    TextView mostrar_imei;
    Button btn_obtener;
    String imei;

    static final Integer PHONESTATS = 0x1;
    private final String TAG=SMSlogin.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //agregamos el layout
        setContentView(R.layout.activity_smslogin);
        //asignamos los valores de los campos a las variables
        edtUsuario=(EditText) findViewById(R.id.edtUsuario);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        //consultarPermiso(Manifest.permission.READ_PHONE_STATE, PHONESTATS);

        //edtUsuario.setText(imei);

        //consultar_us();




        //creamos el evento
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultar_us();
            }
        });


        //codigo IMEI

    }




    //codigo IMEI




    private void consultarPermiso(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SMSlogin.this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SMSlogin.this, permission)) {

                ActivityCompat.requestPermissions(SMSlogin.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SMSlogin.this, new String[]{permission}, requestCode);
            }
        } else {
            imei = obtenerIMEI();
            //Toast.makeText(this,permission + " El permiso a la aplicación esta concedido.", Toast.LENGTH_SHORT).show();
        }
    }


    // Con este método consultamos al usuario si nos puede dar acceso a leer los datos internos del móvil
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // Validamos si el usuario acepta el permiso para que la aplicación acceda a los datos internos del equipo, si no denegamos el acceso
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    imei = obtenerIMEI();

                } else {

                    Toast.makeText(SMSlogin.this, "Has negado el permiso a la aplicación", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private String obtenerIMEI() {
        final TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Hacemos la validación de métodos, ya que el método getDeviceId() ya no se admite para android Oreo en adelante, debemos usar el método getImei()
            return telephonyManager.getImei();
        }
        else {
            return telephonyManager.getDeviceId();
        }

    }

  //codigo IMEI

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

    public boolean consultar_us(){

        //agregamos y convertimos los valores capturados a las nuevas variables
        String password = edtPassword.getText().toString().trim();
        String name = edtUsuario.getText().toString().trim();
        SharedPreferences parametros = getSharedPreferences("Parametros", MODE_PRIVATE);
        final int id_inv = parametros.getInt("id_inv", 1);
        final String ipet = parametros.getString("ip","181.198.202.181");
        final String portet = parametros.getString("port","8082");
        //hacemos una comprobacion para verificar que los campos no esten vacios
        if (validar() /*&& password != null*/){
            //Creamos la variable y el cuerpo de consulta
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formbody = new FormBody.Builder().add("name", name).build();//.add("password", password).build();
            try{
                //Ruta para realizar la consulta
                Request request = new Request.Builder().url("http://"+ipet+":"+portet+"/tfi/usuario").post(formbody).build();
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
                            //String zona_cache = null;
                            intent.putExtra("usuario", edtUsuario.getText().toString().trim());
                            startActivity(intent);

                        }else if (response.code()==400){
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(SMSlogin.this, "Usuario o contraseña no encontrados", Toast.LENGTH_SHORT).show();
                                    edtUsuario.setText("");
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


        return true;
    }

}