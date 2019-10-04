package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    public final static String DATABASE_NAME="MyToDo.db";
    public final static String TABLE_NAME="mytask_table";
    public final static String COL_1="ID";
    public final static String COL_2="NAME";
    public final static String COL_3="DATE";
    public final static String COL_4="TIME";
    public final static String COL_5="DESCRIPTION";
    public final static String COL_6="STATUS";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME+
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT," +
                " DATE TEXT," +
                " TIME TEXT," +
                " DESCRIPTION TEXT," +
                " STATUS TEXT DEFAULT 'N')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,String date,String time,String description){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,date);
        contentValues.put(COL_4,time);
        contentValues.put(COL_5,description);

        long result=db.insert(TABLE_NAME,null,contentValues);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateData(int id,String name,String date,String time,String description){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,date);
        contentValues.put(COL_4,time);
        contentValues.put(COL_5,description);

        db.update(TABLE_NAME,contentValues,"ID=?" , new String[]{String.valueOf(id)});
        return true;
    }

    public Integer deleteData(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});
    }
    public List<Task> getAlltask() {

        List<Task> plist = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setId(cursor.getInt(0));
                t.setTname(cursor.getString(1));
                t.setDate(cursor.getString(2));
                t.setTime(cursor.getString(3));
                t.setDescription(cursor.getString(4));
                t.setStatus(cursor.getString(5));
                plist.add(t);
            } while (cursor.moveToNext());
        }
        return plist;

    }

}
