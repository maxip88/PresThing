package com.seminario.pardo.presthing;

/**
 * Created by Maxi on 12/10/2017.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ALARMA(idal INTEGER PRIMARY KEY AUTOINCREMENT, encabezado TEXT, mensaje TEXT, fecha DATE, hora TIME)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("DROP TABLE IF EXISTS ALARMA" );
        db.execSQL("CREATE TABLE ALARMA(idal INTEGER PRIMARY KEY AUTOINCREMENT, encabezado TEXT, mensaje TEXT, fecha DATE, hora TIME)");

    }
}