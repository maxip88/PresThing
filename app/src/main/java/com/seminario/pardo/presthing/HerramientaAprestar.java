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
    public static SQLiteHelper sqLiteHelper;//Agregado

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            // Contacto seleccionado
           // Toast.makeText(HerramientaAprestar.this, "Hacer algo", Toast.LENGTH_SHORT).show();
            //Agregado
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String nombreContacto = cursor.getString(numberIndex);
                Toast.makeText(HerramientaAprestar.this, nombreContacto, Toast.LENGTH_SHORT).show();
                nombre_contacto.setText(nombreContacto);
                // Hacer algo con el contacto
                //Fin Agregado
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herramienta_aprestar);

        //Agregado
        sqLiteHelper = new SQLiteHelper(this, "HerramientasAcontactosDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS HERRAMIENTASACONTACTOS" +
                "(id_prestamo INTEGER PRIMARY KEY AUTOINCREMENT, id_herramienta INTEGER, " +
                "id_contacto INTEGER, fecha_hasta DATE)");
        //Fin agregado

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
