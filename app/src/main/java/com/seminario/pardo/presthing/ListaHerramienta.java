package com.seminario.pardo.presthing;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Maxi on 31/08/2017.
 */
public class ListaHerramienta extends AppCompatActivity {

    GridView gridView;
    ArrayList<Herramienta> lista;
    AdaptadorListaHerramienta adapter = null;
    //final int REQUEST_CODE_GALLERY = 888;
    final int PHOTO_CODE = 200;
    Bitmap bmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_herramienta);

        gridView = (GridView) findViewById(R.id.gridView);

        lista = new ArrayList<>();
        adapter = new AdaptadorListaHerramienta(this, R.layout.herramienta_items, lista);

        gridView.setAdapter(adapter);

        //Get all de la base de datos
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM HERRAMIENTAS");
        lista.clear();

        while(cursor.moveToNext()){
            int id_herramienta = cursor.getInt(0);
            String nombre_herramienta = cursor.getString(1);
            String descripcion_herramienta = cursor.getString(2);
            byte [] imagen_herramienta = cursor.getBlob(3);
            int estado = cursor.getInt(4); //Agregado

            lista.add(new Herramienta(imagen_herramienta, id_herramienta, nombre_herramienta, descripcion_herramienta, estado));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                CharSequence[] items = {"Actualizar", "Borrar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(ListaHerramienta.this);
                dialog.setTitle("Elija una opcion");

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        if(item==0){
                            //El actualizar
                            //Toast.makeText(getApplicationContext(), "Actualizar..", Toast.LENGTH_SHORT).show();
                            //Cursor c = MainActivity.sqLiteHelper.getData("SELECT id_herramienta FROM HERRAMIENTAS");
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id_herramienta, nombre_herramienta, descripcion_herramienta, estado FROM HERRAMIENTAS");
                            ArrayList<Integer> arrID = new ArrayList<>();
                            //
                            ArrayList<String> arrNom = new ArrayList<>();
                            ArrayList<String> arrDesc = new ArrayList<>();
                            ArrayList<Integer> arrEst = new ArrayList<>();
                            //

                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));

                                //
                                arrNom.add(c.getString(1));
                                arrDesc.add(c.getString(2));
                                arrEst.add(c.getInt(3));
                                //
                            }

                            showDialogUpdate(ListaHerramienta.this, arrID.get(position), arrNom.get(position), arrDesc.get(position), arrEst.get(position)); //OJO

                        } else {
                            //El borrar
                            Cursor c = MainActivity.sqLiteHelper.getData("SELECT id_herramienta FROM HERRAMIENTAS");
                            ArrayList<Integer> arrID = new ArrayList<>();

                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                Intent herramAprestar = new Intent(ListaHerramienta.this, Alarma.class); //Cambiado en vez de HerramientaAprestar

                Cursor c = MainActivity.sqLiteHelper.getData("SELECT nombre_herramienta FROM HERRAMIENTAS");
                ArrayList<String> arrNom = new ArrayList<>();

                while(c.moveToNext()){
                    arrNom.add(c.getString(0));
                }
                 String nombreHerram = (arrNom.get(position));

                Toast.makeText(ListaHerramienta.this, "El elemento seleccionado es: " + nombreHerram , Toast.LENGTH_LONG).show();

                herramAprestar.putExtra("nombre_herramienta", nombreHerram);//Probar

                startActivity(herramAprestar);
            }
        });

    }


    ImageView imageView_herramienta;
    private void showDialogUpdate(Activity activity, final int position, final String nomHerr, final String descHerr, final int estado_herram){ //Le saque el final

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_herramienta_activity);
        dialog.setTitle("Actualizar");

        imageView_herramienta = (ImageView) dialog.findViewById(R.id.imageView_herramienta);
        final EditText nombre_herramienta = (EditText) dialog.findViewById(R.id.nombre_herramienta);
        final EditText descripcion_herramienta = (EditText) dialog.findViewById(R.id.descripcion_herramienta);
        final CheckBox estado_herramienta = (CheckBox) dialog.findViewById(R.id.estado_herramienta); //Agregado
        Button actualizar_herramienta = (Button) dialog.findViewById(R.id.button_actualizar);

        nombre_herramienta.setText(nomHerr);
        descripcion_herramienta.setText(descHerr);
        if (estado_herram == 0){
            estado_herramienta.setChecked(false);

        } else {
            estado_herramienta.setChecked(true);
        }

        //Seteo width con el dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95); //0.95
        //Seteo height con el dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7); //0.7

        dialog.getWindow().setLayout(width,height);
        dialog.show();

       /* imageView_herramienta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Request para la foto de la libreria
                ActivityCompat.requestPermissions(
                        ListaHerramienta.this,
                       // new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        new String[]{Manifest.permission.CAMERA},
                        PHOTO_CODE);
                       // REQUEST_CODE_GALLERY);
            }
        });*/

        imageView_herramienta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PHOTO_CODE);

            }
        });

        actualizar_herramienta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int estado; //AgregadoUltimo

                if (estado_herramienta.isChecked()){
                    estado = 1;
                }

                 else {
                    estado = 0;
                     }
                //Fin agregado ultimo
                try {

                    MainActivity.sqLiteHelper.upgradeData(
                            nombre_herramienta.getText().toString().trim(),
                            descripcion_herramienta.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageView_herramienta),
                            position,
                            estado//Agregado
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Actualizado correctamente..", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Log.d("Error al actualizar: ",e.getMessage());
                }

                updateListaHerramienta();

            }
        });

    }

    private void showDialogDelete(final int idHerram){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListaHerramienta.this);
        dialogDelete.setTitle("Cuidado");
        dialogDelete.setMessage("¿Esta seguro que desea eliminarlo?");
        dialogDelete.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {

                    MainActivity.sqLiteHelper.deleteData(idHerram);
                    Toast.makeText(getApplicationContext(), "Se ha eliminado correctamente", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Log.e("Ha ocurrido un error: ", e.getMessage());

                }
                updateListaHerramienta();
            }
        });
        dialogDelete.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();

    }

    private void updateListaHerramienta(){
        //Get all de la base de datos
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM HERRAMIENTAS");
        lista.clear();

        while(cursor.moveToNext()){
            int id_herramienta = cursor.getInt(0);
            String nombre_herramienta = cursor.getString(1);
            String descripcion_herramienta = cursor.getString(2);
            byte [] imagen_herramienta = cursor.getBlob(3);
            int estado = cursor.getInt(4);

            lista.add(new Herramienta(imagen_herramienta, id_herramienta, nombre_herramienta, descripcion_herramienta, estado));
        }
        adapter.notifyDataSetChanged();
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "No tieme permisos para acceder al directorio", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView_herramienta.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if(requestCode == PHOTO_CODE && resultCode == RESULT_OK){

        try {
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            imageView_herramienta.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    super.onActivityResult(requestCode, resultCode, data);
    }

}
