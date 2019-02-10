package com.nodepad.pc.nodepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//****NotesDB.java is the sqlite database model.****//

public class NotesDB extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "notespad";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String PATH = "path";
    public static final String TIME = "time";
    public static final String COLOR = "color";
    public static final String TABLE = "TABLE";

    public NotesDB(Context context){
        super(context,"notes",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //****Create table,text content, id, path, time, color.****//

        db.execSQL("Create "+TABLE+ " "+TABLE_NAME + "("
            +ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CONTENT +" TEXT NOT NULL, "
            + PATH+ " TEXT , "
                + TIME +" TEXT NOT NULL, " + COLOR +" TEXT ) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
