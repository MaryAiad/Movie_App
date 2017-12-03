package com.example.mkany.movieapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mery on 11/26/2017.
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_Name, null, TaskContract.DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " integer primary key AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_MOVIE_TITLE + " text not null,"+
                TaskContract.TaskEntry.COL_MOVIE_Image + " text not null,"+
                TaskContract.TaskEntry.COL_MOVIE_RATING + " double, "+
                TaskContract.TaskEntry.COL_MOVIE_Release +" text not null, "+
                TaskContract.TaskEntry.COL_MOVIE_Overview + " text);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
