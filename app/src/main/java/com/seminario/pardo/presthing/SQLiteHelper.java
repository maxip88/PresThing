package com.seminario.pardo.presthing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Date;

/**
 * Created by Maxi on 30/08/2017.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //TABLA DE HERRAMIENTAS

    public void insertData(String nombre_herramienta, String descripcion_herramienta, byte [] image, int estado){

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO HERRAMIENTAS VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nombre_herramienta);
        statement.bindString(2, descripcion_herramienta);
        statement.bindBlob(3, image);
        statement.bindDouble(4, (double) estado); //Agregado
        statement.executeInsert();

    }

    public void upgradeData(String nombre_herramienta, String descripcion_herramienta, byte [] image, int id_herramienta){

        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE HERRAMIENTAS SET nombre_herramienta = ?, descripcion_herramienta = ?," +
                "imagen_herramienta = ? WHERE id_herramienta = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1, nombre_herramienta);
        statement.bindString(2, descripcion_herramienta);
        statement.bindBlob(3, image);
        statement.bindDouble(4, (double)id_herramienta);

        statement.execute();
        database.close();
    }

    public void deleteData (int idHerram){

        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM HERRAMIENTAS WHERE id_herramienta = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.clearBindings();
        statement.bindDouble(1, (double) idHerram);
        statement.execute();
        database.close();
    }

    public Cursor getData (String sql){

        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //TABLA DE PRESTAMOS

    public void insertPrestamo(int id_herramienta, int id_contacto, Date fecha_hasta){

        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO HERRAMIENTASACONTACTOS VALUES (NULL, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, (double)id_herramienta);
        statement.bindDouble(2, (double)id_contacto);
        statement.bindLong(3, fecha_hasta.getTime());
        statement.executeInsert();

        }

}
