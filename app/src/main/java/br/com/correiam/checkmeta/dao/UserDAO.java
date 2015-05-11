package br.com.correiam.checkmeta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.correiam.checkmeta.dominio.User;

/**
 * Created by Misael Correia on 25/04/15.
 * misaelsco@gmail.com
 */
public class UserDAO {

    private SQLiteHelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_USER = "User";

    public UserDAO(){

    }
    public UserDAO(Context context){
        helper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }
    //Transforma uma conta em um mapa (ContentValues) e
    //usa o recurso da classe db (metodo insert)
    public Long insert(User user){
        open();
        Long insertedId = Long.valueOf(-1);

        ContentValues values = new ContentValues();
        values.put("name",user.getName());
        values.put("password",user.getPassword());
        values.put("email", user.getEmail());

        try{
            insertedId = db.insert(TABLE_USER,null,values);
        }catch (SQLiteException ex){
            close();
        }
        close();
        return insertedId;
    }

    public boolean update(User u)
    {
        return false;
    }

    public User  isValidCredentials(String email, String password)
    {
        User user = null;
        open();
        try {
            Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE email = '" + email + "' AND password = '" + password + "'", null);

            if(c.getCount() > 0) {
                user = new User();
                c.moveToFirst();
                user.setId(c.getLong(c.getColumnIndex("id")));
                user.setName(c.getString(c.getColumnIndex("name")));
                user.setEmail(c.getString(c.getColumnIndex("email")));
                user.setPassword(c.getString(c.getColumnIndex("password")));
                c.close();
            }
            else {
                c.close();
            }
        }
        catch (SQLiteException ex){
            Log.d("LogError", ex.toString());
            close();
        }
        close();
        return user;
    }

    public boolean isDuplicated(String email) {
        open();
        try {
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USER + " WHERE email = '" + email + "'", null);
            c.moveToFirst();
            if (c.getInt(0) == 0) {
                return false;
            }
        } catch (SQLiteException ex) {
            Log.d("LogError", "Email '" + email + "' é duplicado [" + ex.toString() + "]");
        }
        return true;
    }
}
