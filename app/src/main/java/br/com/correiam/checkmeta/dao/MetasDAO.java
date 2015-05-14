package br.com.correiam.checkmeta.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.correiam.checkmeta.dominio.Meta;

/**
 * Created by Misael Correia on 25/04/15.
 * misaelsco@gmail.com
 */
public class MetasDAO {

    private SQLiteHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE = "Meta";

    public MetasDAO (Context context) {
        helper = new SQLiteHelper(context);
    }


    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public long insert(Meta meta){
        open();
        Long insertedRow = Long.valueOf(-1);

        ContentValues values = new ContentValues();
        values.put("name", meta.getName());
        values.put("description", meta.getDescription());
        values.put("dueDate", meta.getDueDate());
        values.put("state", meta.getState());

        try{
            insertedRow = db.insert(TABLE,null,values);
        }catch (SQLiteException ex){
            close();
        }
        close();
        return insertedRow;
    }


    public ArrayList<Meta> selectAll(){
        ArrayList<Meta> metas = new ArrayList<>();

        open();
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE , null);

            if(c.getCount() > 0) {
                c.moveToFirst();
                Meta retrievedMeta;
                do {
                    retrievedMeta = new Meta();
                    retrievedMeta.setId(c.getLong(c.getColumnIndex("id")));
                    retrievedMeta.setName(c.getString(c.getColumnIndex("name")));
                    retrievedMeta.setDescription(c.getString(c.getColumnIndex("description")));
                    retrievedMeta.setDueDate(c.getString(c.getColumnIndex("dueDate")));
                    retrievedMeta.setState(c.getString(c.getColumnIndex("state")));

                    metas.add(retrievedMeta);
                }
                while (c.moveToNext());
                c.close();
            }
            else {
                c.close();
            }
        }
        catch (SQLiteException ex){
            Log.d("LogError", "Erro ao buscar metas [" + ex.toString() + "]");
            close();
        }
        close();
        return metas;
    }

    public Meta select(String idMeta){
        Meta meta = new Meta();

        open();
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE + " WHERE id = " + idMeta, null);

            if(c.getCount() > 0) {
                c.moveToFirst();

                meta = new Meta();
                meta.setId(c.getLong(c.getColumnIndex("id")));
                meta.setName(c.getString(c.getColumnIndex("name")));
                meta.setDescription(c.getString(c.getColumnIndex("description")));
                meta.setDueDate(c.getString(c.getColumnIndex("dueDate")));
                meta.setState(c.getString(c.getColumnIndex("state")));
                meta.setActualDate(c.getString(c.getColumnIndex("actualDate")));

                c.close();
            }
            else {
                c.close();
            }
        }
        catch (SQLiteException ex){
            Log.d("LogError", "Erro ao buscar meta de id = " + idMeta + "[" + ex.toString() + "]");
            close();
        }
        close();
        return meta;
    }

    public boolean update(Meta meta){

        open();

        ContentValues values = new ContentValues();
        values.put("name", meta.getName());
        values.put("description", meta.getDescription());
        values.put("dueDate", meta.getDueDate());
        values.put("state", meta.getState());
        values.put("actualDate", meta.getActualDate());

        try {
            int update = db.update(TABLE, values, "id=" + meta.getId().toString(), null);
            Log.d("DEBUG", "Retorno do update: " + update);
            close();
            return true;
        }catch (Exception e){
            Log.d("ERROR", "Erro ao atualizar meta [" + e.toString() + "]");
            close();
        }
        close();
        return false;
    }

    public boolean delete(String idMeta) {
        open();
        try {
            int delete = db.delete(TABLE, "id=" + idMeta, null);
            Log.d("DEBUG", "Retorno do delete: " + delete);
            close();
            return true;
        }catch (Exception e){
            Log.d("ERROR", "Erro ao excluir meta [" + e.toString() + "]");
            close();
        }
        close();
        return false;


    }
}
