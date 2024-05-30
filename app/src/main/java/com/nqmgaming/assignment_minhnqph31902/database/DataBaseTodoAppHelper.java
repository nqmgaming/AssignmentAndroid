package com.nqmgaming.assignment_minhnqph31902.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseTodoAppHelper extends SQLiteOpenHelper {

    //create name and version
    public static final String DATABASE_NAME = "todoapp.db";
    public static final int DATABASE_VERSION = 1;

    //create table user and todo
    public static final String TABLE_USER = "user_table";
    public static final String TABLE_TODO = "todo_table";

    //create constructor
    public DataBaseTodoAppHelper(Context context) {

        //call super
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create user table if it doesn't exist
        String USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "username TEXT NOT NULL, email TEXT NOT NULL, password TEXT NOT NULL, firstname TEXT NOT NULL, lastname TEXT NOT NULL)";
        db.execSQL(USER_TABLE);

        // Create todo table if it doesn't exist
        String TODO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TODO + " (id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, content TEXT NOT NULL  , status INTEGER NOT NULL, " +
                "start_date TEXT NOT NULL, end_date TEXT NOT NULL , user_id INTEGER NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES user_table (id))";
        db.execSQL(TODO_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
