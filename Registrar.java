package com.example.laujame.newproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import modelos.Usuario;

public class Registrar extends AppCompatActivity {

    EditText txtUser,
            txtName,
            txtEmail,
            txtAdress,
            txtPassword;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        txtUser=findViewById(R.id.txtUsuario);
        txtName=findViewById(R.id.txtNombre);
        txtEmail=findViewById(R.id.txtCorreo);
        txtAdress=findViewById(R.id.txtDireccion);
        txtPassword=findViewById(R.id.txtPass);
        btnAdd=findViewById(R.id.btnRegistrar);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandar llamar datos validados
                if(
                validarDatos(txtUser.getText().toString().trim(),
                txtName.getText().toString().trim(),
                txtEmail.getText().toString().trim(),
                txtAdress.getText().toString().trim(),
                txtPassword.getText().toString().trim())){

                }
                //crear el objeto de tipo Usuario
                Usuario usuario= new Usuario();
                usuario.setNicknames(txtUser.getText().toString().trim());
                usuario.setNombre(txtName.getText().toString().trim());
                usuario.setCorreo(txtEmail.getText().toString().trim());
                usuario.setDireccion(txtAdress.getText().toString().trim());
                usuario.setPassword(txtPassword.getText().toString().trim());


                //crear la instancia  de la subclase asynTask para realizar la conexion

                new AddUser().execute(usuario);

            }
        });

    }


    public Boolean  validarDatos(String nickname, String nombre, String correo, String Direccion, String Password){
        //validar datos que no esten vacios
        if(nombre.isEmpty()){
            txtName.setError("Campo vacio");
            txtName.setFocusable(true);
            return false ;
        }
        if(nickname.isEmpty()){
            txtUser.setError("Campo vacio");
            txtUser.setFocusable(true);
            return false ;
        }
        if(correo.isEmpty()){
            txtEmail.setError("Campo vacio");
            txtEmail.setFocusable(true);
            return false ;
        }
        if(Direccion.isEmpty()){
            txtAdress.setError("Campo vacio");
            txtAdress.setFocusable(true);
            return false ;
        }
        if(Password.isEmpty()){
            txtPassword.setError("Campo vacio");
            txtPassword.setFocusable(true);
            return false  ;
        }

        return true;



    }







    // se crea un hilo el cual se obtendran los datos de lo que se esta solicitando, es una ayuda para el momento de ejecucion

    class AddUser extends AsyncTask<Usuario, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Usuario... usuarios) {


            //prepara los datos de la insersion
            String params="";// variable que integra los datos que se estan requiriendo
            params= "user="+ usuarios[0].getNicknames()+"&"+
                    "nombre="+usuarios[0].getNombre()+"&"+
                    "correo="+ usuarios[0].getCorreo()+"&"+
                    "direccion="+ usuarios[0].getDireccion()+"&"+
                    "password="+ usuarios[0].getPassword();
            //prepara la conexion

            try {
                URL url= new URL("http://172.18.26.67/cursoAndroid/vista/Usuario/crearUsuario.php");
                HttpURLConnection connection= (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                // se necesita esta para indicar que lleva parametros, estos muestran datos de entra y salida
                connection.setDoInput(true);
                //este muestra la escepcion de que si registro
                connection.setDoOutput(true);


                OutputStream outputStream= connection.getOutputStream();
                BufferedWriter write= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                write.write(params);
                write.flush();
                write.close();

                connection.connect();

               int responseCode= connection.getResponseCode();
               if(responseCode== HttpURLConnection.HTTP_OK){
                   Log.i("AddUser", "usuario agregado con exito");
                   return true;


               }
                else {
                   return false;
               }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);



            if(aBoolean){
                Toast.makeText(Registrar.this, "Agregado con exito", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
             Toast.makeText(Registrar.this,
                     "usuario no agregado, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
