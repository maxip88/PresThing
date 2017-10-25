package com.seminario.pardo.presthing;

/**
 * Created by Maxi on 12/10/2017.
 */
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Alarma extends AppCompatActivity {
    private EditText t3,t5,t6,t7;
    private AdminSQLiteOpenHelper admin;
    private SQLiteDatabase bd;
    private ContentValues registro;
    private EditText tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate, btnElegirCont;
    // fecha
    private int year;
    private int month;
    private int day;
    static final int REQUEST_SELECT_CONTACT = 1;
    static final int DATE_DIALOG_ID = 999;
    // hora
    private int minute;
    private int hour;

    private TimePicker timePicker;
    private TextView textViewTime;
    private Button button;
    private static final int TIME_DIALOG_ID = 998;
    Calendar calendario = Calendar.getInstance();
    int hora, min,dia,mes,ano;
    String fecha_sistema,hora_sistema;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            //Contacto seleccionado
            //Nuevo Agregado
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String nombreContacto = cursor.getString(numberIndex);
                Toast.makeText(Alarma.this, nombreContacto, Toast.LENGTH_SHORT).show();
                t5.setText(nombreContacto);
            }
            //Fin Agregado
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        admin = new AdminSQLiteOpenHelper(this, Vars.bd, null, Vars.version);
        bd = admin.getWritableDatabase();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH)+1;
        ano = calendario.get(Calendar.YEAR);
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        min = calendario.get(Calendar.MINUTE);
        fecha_sistema=mes+"-"+dia+"-"+ano+" ";
   //   fecha_sistema=dia+"-"+mes+"-"+ano+" ";
        hora_sistema = hora+":"+min;
        setCurrentDateOnView();
        addListenerOnButton();
        // hora
        setCurrentTimeOnView();
        t3 = (EditText) findViewById(R.id.text_nomHerram);
        t5= (EditText) findViewById(R.id.text_contacto);
        t6= (EditText) findViewById(R.id.text_fecha);
        t7= (EditText) findViewById(R.id.text_hora);
        //Recupero datos de ListaHerramienta
        Intent intent = getIntent();
        Bundle datos = intent.getExtras();

        if(datos != null){
            String nombreHerram = datos.getString("nombre_herramienta");
            t3.setText(nombreHerram);
        }
        //Inicio el servicio
        servicio();
        btnElegirCont = (Button) findViewById(R.id.button_elegirCont);
        btnElegirCont.setOnClickListener(new View.OnClickListener() {
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
    public void servicio() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); //arranca la aplicacion
        int intervalMillis = 1 * 3 * 1000; //3 segundos
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
    }

    public void llenar111(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, Vars.bd, null, Vars.version);
        SQLiteDatabase bd = admin.getReadableDatabase();
        bd = admin.getWritableDatabase();
        registro = new ContentValues();
        registro.put("encabezado", t3.getText().toString());
        registro.put("mensaje", t5.getText().toString());//nombre del campo
        registro.put("fecha", t6.getText().toString());
        registro.put("hora", t7.getText().toString());
        bd.insert("alarma", null, registro);//nombre de la tabla
        bd.close();
        //Llamo al metodo de crear evento
        addEvent();
        //
        t3.setText("");
        t5.setText("");
        t6.setText("");
        t7.setText("");
        Toast.makeText(this, "Alarma registrada", Toast.LENGTH_LONG).show();
    }
    //Agregado para crear evento
    public void addEvent() {
        try {
            Calendar c = Calendar.getInstance();

            c.set(Calendar.YEAR, year);//año
            c.set(Calendar.MONTH, month);//mes
            c.set(Calendar.DAY_OF_MONTH, day);//dia

            c.set(Calendar.HOUR_OF_DAY, hour); //hora
            c.set(Calendar.MINUTE, minute);//min

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setType("vnd.android.cursor.dir/event")
                    .setData(CalendarContract.Events.CONTENT_URI)

                    .putExtra(CalendarContract.Events.TITLE, "Prestamo")
                    .putExtra(CalendarContract.Events.DESCRIPTION, t3.getText().toString() + " prestado a " + t5.getText().toString())
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, c.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, c.getTimeInMillis() + 60 + 60 * 1000);

                    startActivity(intent);

        }catch (Exception e){
            Log.i(e.getMessage(),"Referencia");
        }
    }
    //FIN Agregado de evento
    public void setCurrentTimeOnView() {

        textViewTime = (EditText) findViewById(R.id.text_hora);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

    }

    public void addListenerOnButton() {

        btnChangeDate = (Button) findViewById(R.id.button_fecha);
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

        button = (Button) findViewById(R.id.button_hora);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }
        });
    }


    public void setCurrentDateOnView() {

        tvDisplayDate = (EditText) findViewById(R.id.text_fecha);
        dpResult = (DatePicker) findViewById(R.id.datePicker);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Ingreso el dia de hoy en el text
        tvDisplayDate.setText(new StringBuilder()
                // El mes está basado en 0, solo sumo 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // Ingreso el dia de hot en datepicker
        dpResult.init(year, month, day, null);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // Seteo en el datepicker la fecha actual
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
            case TIME_DIALOG_ID:

                return new TimePickerDialog(this, timePickerListener, hour, minute,false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =  new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

            hour = selectedHour;

            minute = selectedMinute;
            textViewTime.setText(new StringBuilder().append(padding_str(hour)).append(":").append(padding_str(minute)));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);

        }

    };

    private static String padding_str(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // La fecha seleccionada la seteo
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // Tambien en el datepicker
            dpResult.init(year, month, day, null);

        }
    };

}
