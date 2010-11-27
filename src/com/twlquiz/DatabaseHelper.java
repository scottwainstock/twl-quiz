package com.twlquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper; 

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "twl_quiz";
	private static final String[] LIST_TYPES = {"twl_twos", "twl_threes", "twl_fours"};

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}

	public void onCreate(SQLiteDatabase database) {		
		database.execSQL("create table streaks (id integer not null, type string, high integer, primary key(id), unique(type))");

		ContentValues contentValues = new ContentValues();
		for(int i = 0; i < LIST_TYPES.length; i++) {
			contentValues.put("type", LIST_TYPES[i]);
			database.insert("streaks", null, contentValues);
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}