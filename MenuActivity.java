package com.example.laujame.newproyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import modelos.tienda;

public class MenuActivity extends AppCompatActivity {

  ListView lst;
  AdapterStore adapterStore;
  ArrayList<tienda>arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu2);


        lst= findViewById(R.id.lstTiendas);
        //arrayList=new ArrayList<tienda>();
       // arrayList.add(new tienda("tiendaUno", "descripcion1") );
       // arrayList.add(new tienda("tiendaDos", "descripcion2"));

       // adapterStore= new AdapterStore(this, arrayList);
        //lst.setAdapter(adapterStore);
        registerForContextMenu(lst);

        new ConsultarTienda().execute();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
         MenuInflater menuInflater = getMenuInflater();
         menuInflater.inflate(R.menu.menu_context,menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();



        switch (item.getItemId()){

            case R.id.itemContextUpdate:
                Toast.makeText(getApplicationContext(), "Update:"+arrayList.get(info.position).getId(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getApplicationContext(),FormStoreActivity.class);

                tienda store= new tienda();
                store.setId(arrayList.get(info.position).getId());
                store.setNombre(arrayList.get(info.position).getNombre());
                store.setDescripcion(arrayList.get(info.position).getDescripcion());
                store.setDireccion(arrayList.get(info.position).getDireccion());
                store.setLatitud(arrayList.get(info.position).getLatitud());
                store.setLongitud(arrayList.get(info.position).getLongitud());

                intent.putExtra("myObj", (Parcelable) store);
                startActivity(intent);
                return true;


            case R.id.itemContextDelete:
                Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                tienda store1= new tienda();
                store1.setId(arrayList.get(info.position).getId());
                new DeleteStore().execute(store1);

                return  true;


            case R.id.itemContextNotify:
                //notificacion
                int inNotificationId=1;
                String chanelID="my_chanel_01";
                NotificationCompat.Builder noti= new NotificationCompat.Builder(getApplicationContext(), null);
                NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent  intent1= new Intent(getApplicationContext(),MenuActivity.class);
                PendingIntent  pendingIntent= PendingIntent.getActivity(MenuActivity.this, 0, intent1,0);


                // configurar notificaciones para versiones igual o superior a oreo

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    CharSequence name= "myname";
                    String description= "MyDescrip";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel  nChannel= new NotificationChannel(chanelID,name,importance);
                    nChannel.setDescription(description);
                    nChannel.enableLights(true);
                    nChannel.setLightColor(Color.BLUE);
                    nm.createNotificationChannel(nChannel);
                    noti= new NotificationCompat.Builder(getApplicationContext(), chanelID);

                }
                noti.setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("myTitle").setContentIntent(pendingIntent);
                noti.setChannelId(chanelID);
                nm.notify(inNotificationId, noti.build());
                default:
                    return super.onContextItemSelected(item);
        }
    }

    class ConsultarTienda extends AsyncTask<Void, Integer, JSONArray> {//integer es el progreso de la actividad en segundo plano

          @Override
          protected JSONArray doInBackground(Void... voids) {

              URLConnection conection= null;
              JSONArray jsonArray =null;
              try {
                  conection= new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/obtenerTiendas.php").openConnection();

                  InputStream inputStream=(InputStream)conection.getContent();
                  //numero de cabidades que tiene
                  byte[]buffer= new byte[1000];
                  int size = inputStream.read(buffer);//buffer que lee la cantidad de caracteres que contiene
                  jsonArray= new JSONArray(new String(buffer,0,size));



              } catch (IOException e) {
                  e.printStackTrace();
              } catch (JSONException e) {
                  e.printStackTrace();
              }


              return jsonArray;
          }

          @Override
          protected void onPostExecute(JSONArray jsonArray) {
              super.onPostExecute(jsonArray);
              tienda mytienda=null;
              arrayList= new ArrayList<tienda>();
              for(int i=0;i<jsonArray.length();i++){

                  try {
                      JSONObject jsonObject= jsonArray.getJSONObject(i);
                      mytienda= new tienda(jsonObject.getInt("idtienda"),
                                           jsonObject.getString("nombre"),
                                           jsonObject.getString("direccion"),
                                           jsonObject.getDouble("latitud"),
                                           jsonObject.getDouble("longitud"),
                                           jsonObject.getString("descripcion"));

                      arrayList.add(mytienda);
                  }catch (JSONException e){
                      e.printStackTrace();

                  }


              }
              adapterStore= new AdapterStore (MenuActivity.this, arrayList);
              lst.setAdapter(adapterStore);

          }
      }

    class DeleteStore extends AsyncTask<tienda, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(tienda... tiendas) {
            String params ="idtienda"+tiendas[0].getId();




            try{
                URL url= new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/eliminarTienda.php");
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
                Toast.makeText(MenuActivity.this, "Tienda eliminada con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(  MenuActivity.this, "No se pudo eliminar, intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
