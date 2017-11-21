package com.seminario.pardo.presthing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maxi on 31/08/2017.
 */
public class AdaptadorListaHerramienta extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Herramienta> listaHerramienta;

    public AdaptadorListaHerramienta(Context context, int layout, ArrayList<Herramienta> listaHerramienta) {
        this.context = context;
        this.layout = layout;
        this.listaHerramienta = listaHerramienta;
    }

    @Override
    public int getCount() {
        return listaHerramienta.size();
    }

    @Override
    public Object getItem(int position) {
        return listaHerramienta.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView img_herram;
        TextView nom_herram, desc_herram, est_herram;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.nom_herram = (TextView) row.findViewById(R.id.nom_herram);
            holder.desc_herram = (TextView) row.findViewById(R.id.desc_herram);
            holder.img_herram = (ImageView) row.findViewById(R.id.img_herram);

            holder.est_herram = (TextView) row.findViewById(R.id.desc_estado);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Herramienta herramienta = listaHerramienta.get(position);

        holder.nom_herram.setText(herramienta.getNombre_herramienta());
        holder.desc_herram.setText(herramienta.getDesc_herramienta());

        byte [] imgHerram = herramienta.getImg_herramienta();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgHerram, 0, imgHerram.length);

        holder.img_herram.setImageBitmap(bitmap);

        int estado = (herramienta.getEstado());

        if (estado == 0) {
            holder.est_herram.setText("Disponible");
            row.setBackgroundColor(Color.GREEN);
        }
        else {
            holder.est_herram.setText("Prestado");
            row.setBackgroundColor(Color.RED);
        }

        return row;
    }
}
