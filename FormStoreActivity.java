package com.example.laujame.newproyect;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import modelos.tienda;

public class FormStoreActivity extends AppCompatActivity implements View.OnClickListener {



 EditText  id,
         nombre,
         direccion,
         descripcion,
         latitud, longitud;

 Button guardar,
         cancelar;
  tienda storeIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_store);
        id=findViewById(R.id.idForm);
        nombre=findViewById(R.id.nombreForm);
        direccion=findViewById(R.id.direccionForm);
        descripcion=findViewById(R.id.descripcionForm);
        latitud=findViewById(R.id.latitudForm);
        longitud=findViewById(R.id.longitudForm);
        guardar=findViewById(R.id.botonGuardarForm);
        cancelar=findViewById(R.id.buttonCancelarFrom);



        guardar.setOnClickListener(this);
        cancelar.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonGuardarForm:
                break;


            case R.id.buttonCancelarFrom:
                break;
        }
    }

    class UpdateStore extends AsyncTask<tienda, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(tienda... tiendas) {
            String params ="";

            params= "nombre"+tiendas[0].getNombre()+"&"+
                    "Latitud="+tiendas[0].getLatitud()+"&"+
                    "longitud"+tiendas[0].getLongitud()+"&"+
                    "direccion"+tiendas[0].getDireccion()+"&"+
                    "descripcion"+tiendas[0].getDescripcion();

            try{
                URL url= new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/modificarTienda.php");
                HttpURLConnection conection =(HttpURLConnection)url.openConnection();
                //cambiar metodo a POST y preparar para enviar y recibir datos
                conection.setRequestMethod("POST");
                conection.setDoOutput(true);
                conection.setDoInput(true);

               //se crea el outputscram y el buffered writer paa escribir los datos

                OutputStream outputStream= conection.getOutputStream();
                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                //se escriben los datos en params
                writer.write(params);
                //limpiamos las celdas
                writer.flush();
                //cerramos la conexion
                writer.close();

                //se realiza la conexion
                conection .connect();
                //se obtiene el repose code de la conexion
                int responseCode= conection.getResponseCode();
                if (responseCode==HttpURLConnection.HTTP_OK){
                    return true;

                }else {
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
            if (aBoolean){
                Toast.makeText(FormStoreActivity.this, "Tienda actualizada con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FormStoreActivity.this, "NO e pudo actualizar, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
