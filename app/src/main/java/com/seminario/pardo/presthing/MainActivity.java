package com.seminario.pardo.presthing;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    EditText nombre_herramienta, desc_herramienta;
    ImageView img_herramienta;
    Bitmap bmp;
    Button elegir_img, listar, agregar_herramienta, activar_camara;
    final int REQUEST_CODE_GALLERY = 999;
    final int PHOTO_CODE = 200;
    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sqLiteHelper = new SQLiteHelper(this, "HerramientasDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS HERRAMIENTAS" +
                "(id_herramienta INTEGER PRIMARY KEY AUTOINCREMENT, nombre_herramienta VARCHAR," +
                " descripcion_herramienta VARCHAR, imagen_herramienta BLOB, estado INTEGER)");


        elegir_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        activar_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PHOTO_CODE);

            }
        });

        agregar_herramienta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    sqLiteHelper.insertData(
                            nombre_herramienta.getText().toString().trim(),
                            desc_herramienta.getText().toString().trim(),
                            imageViewToByte(img_herramienta),
                            0
                    );
                    Toast.makeText(getApplicationContext(), "Agregado OK", Toast.LENGTH_SHORT).show();
                    nombre_herramienta.setText("");
                    desc_herramienta.setText("");
                    img_herramienta.setImageResource(R.mipmap.ic_launcher);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListaHerramienta.class);
                startActivity(intent);
            }
        });
    }

    public static byte [] imageViewToByte(ImageView image) {

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){ //Linea agregada para camara
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_herramienta.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if(requestCode == PHOTO_CODE && resultCode == RESULT_OK){
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            img_herramienta.setImageBitmap(bmp);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        nombre_herramienta = (EditText) findViewById(R.id.text_nombre);
        desc_herramienta = (EditText) findViewById(R.id.text_desc);
        img_herramienta = (ImageView) findViewById(R.id.img_herramienta);
        elegir_img = (Button) findViewById(R.id.button_img);
        activar_camara = (Button) findViewById(R.id.button_camara);
        listar = (Button) findViewById(R.id.button_listar);
        agregar_herramienta = (Button) findViewById(R.id.button_agregar);

    }

}
