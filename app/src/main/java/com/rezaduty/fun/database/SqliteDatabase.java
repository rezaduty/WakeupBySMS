package com.rezaduty.fun.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rezaduty.fun.Person;

import java.util.ArrayList;
import java.util.List;


public class SqliteDatabase extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	1;
    private	static final String	DATABASE_NAME = "smsTasks";
    private	static final String TABLE_PERSON = "person";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_PERSONNAME = "name";
    private static final String COLUMN_TITLE = "title";

    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_PRODUCTS_TABLE = "CREATE	TABLE " + TABLE_PERSON + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PERSONNAME + " TEXT," + COLUMN_TITLE + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);
    }

    public List<Person> listPerson(){
        String sql = "select * from " + TABLE_PERSON;
        SQLiteDatabase db = this.getReadableDatabase();
        List<Person> storePeople = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String title = cursor.getString(2);
                storePeople.add(new Person(id, name, title));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storePeople;
    }

    public void addPerson(Person person){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSONNAME, person.getName());
        values.put(COLUMN_TITLE, person.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PERSON, null, values);
    }

    public void updatePerson(Person person){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PERSONNAME, person.getName());
        values.put(COLUMN_TITLE, person.getTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PERSON, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(person.getId())});
    }


    public boolean findPerson(String name,String title){


        String[] columns = new String[]{COLUMN_PERSONNAME, COLUMN_TITLE};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_PERSON, columns, COLUMN_PERSONNAME+"=? AND "+COLUMN_TITLE + " =?", new String[] { name,title  }, null, null, null);

        if	(cursor.moveToFirst()) {
            Log.d("SMS","Nashod");
            cursor.close();
            return true;



        }else{
            Log.d("SMS","OK");
            cursor.close();
            return false;

        }


    }

    public void deleteProduct(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PERSON, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
