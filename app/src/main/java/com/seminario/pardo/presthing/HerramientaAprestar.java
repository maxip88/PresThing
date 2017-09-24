package com.seminario.pardo.presthing;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class HerramientaAprestar extends AppCompatActivity {

    static final int REQUEST_SELECT_CONTACT = 1;
    private Button elegir_contacto, prestar;
    private TextView nombre_contacto, nombre_herramienta;
    private CalendarView fechaHasta;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            // Do something with the selected contact at contactUri
           // Toast.makeText(HerramientaAprestar.this, "Hacer algo", Toast.LENGTH_SHORT).show();
            //Agregado
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
            // If the cursor returned is valid, get the name
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String nombreContacto = cursor.getString(numberIndex);
                Toast.makeText(HerramientaAprestar.this, nombreContacto, Toast.LENGTH_SHORT).show();
                nombre_contacto.setText(nombreContacto);
                // Do something with the phone number
                //Fin Agregado
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herramienta_aprestar);
        nombre_contacto = (TextView) findViewById(R.id.nombre_contacto);
        nombre_herramienta = (TextView) findViewById(R.id.nombre_herramienta);

        Intent intent = getIntent();
        Bundle datos = intent.getExtras();

        if(datos != null){
            String nombreHerram = datos.getString("nom_herram");
            nombre_herramienta.setText(nombreHerram);
        }

        elegir_contacto = (Button) findViewById(R.id.button_contacto);
        prestar = (Button) findViewById(R.id.button_prestar);
        fechaHasta = (CalendarView) findViewById(R.id.calendarView);

        elegir_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }

            }
        });
    }
}
