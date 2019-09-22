package com.example.laujame.newproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import modelos.tienda;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrar, btnLogin, Ubicar, tienda;
    EditText txtUser, txtPass;
    String User, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUser= findViewById(R.id.TxtEmail);
        txtPass= findViewById(R.id.TxtPassword);
        btnRegistrar=findViewById(R.id.botonRegistrar);
        btnLogin=findViewById(R.id.botonLogin);
        Ubicar= findViewById(R.id.botonMapa);
        tienda=findViewById(R.id.botonMenuTienda);


      btnRegistrar.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent= new Intent(getApplicationContext(), Registrar.class);
              startActivity(intent);
          }
      });
        tienda.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });
        Ubicar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void login(View view){
        User=txtUser.getText().toString().trim();
        Password=txtPass.getText().toString();
        Toast.makeText(getApplicationContext(),
                "login", Toast.LENGTH_SHORT).show();

        //validar datos de la interfaz (usuario y password)

        if (TextUtils.isEmpty(User)){
            txtUser.setError("Usuario vacio");
            txtUser.setFocusable(true);
            return;
        }

        if(TextUtils.isEmpty(Password)){
            txtPass.setError("Contrase;a vacia");
            txtPass.setFocusable(true);
            return;
        }

        new LoginRest().execute(User,Password);

    }

    //clase AsynTask
    class LoginRest  extends AsyncTask<String, Integer, String>{
        //valiable de peticion de conexion
        URLConnection connection = null;

        // variable para el resultado
        String  result = "0";


        @Override
        protected String doInBackground(String... strings) {

            try {
                connection= new URL("http://172.18.26.67/"+
                        "cursoAndroid/vista/usuario/"+
                        "iniciarSesion.php"+
                        "?usuario="+strings[0]+"&password="+strings[1]).openConnection();

                InputStream inputStream =(InputStream) connection.getContent();
                //arreglo para consulta de informacion
                byte[] buffer= new  byte [10000];
                //indica cuantos datos son en tu cadena de respuesta
                int size= inputStream.read(buffer);

                result= new String(buffer, 0 ,size);
                Log.i("result", result);


            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("1")){
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(MainActivity.this,
                        "Usuario o Contrase;a incorrectis",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}

