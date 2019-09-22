package com.example.laujame.newproyect;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.tienda;

public class AdapterStore extends BaseAdapter {

    protected Activity  activity;
    protected ArrayList<tienda> lst;

    public AdapterStore(Activity activity, ArrayList<tienda> lst) {
        this.activity = activity;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v= convertView;
        if(v==null){
            LayoutInflater layoutInflanter= (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v= layoutInflanter.inflate(R.layout.item_tienda,null);
        }
        tienda tienda= lst.get(position);
        TextView lblNombre, lblDescripcion;
        lblNombre=v.findViewById(R.id.lblNameItemTienda);
        lblDescripcion= v.findViewById(R.id.lblInformacion);

        lblNombre.setText(tienda.getNombre());
        lblDescripcion.setText(tienda.getDescripcion());

        return v;
    }
}
