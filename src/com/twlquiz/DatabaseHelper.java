package com.twlquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper; 

public class DatabaseHelper extends SQLiteOpenHelper {
	public DatabaseHelper(Context context) {
		super(context, TWLQuizUtil.DATABASE_NAME, null, 2);
	}
	
	public static void clearStats(SQLiteDatabase database) {
		database.execSQL("update streaks set high = 0");
	}

	public void onCreate(SQLiteDatabase database) {		
		database.execSQL("create table streaks (id integer not null, type string, high integer, primary key(id), unique(type))");

		ContentValues contentValues = new ContentValues();
		for(int i = 0; i < TWLQuizUtil.LIST_TYPES.length; i++) {
			contentValues.put("type", TWLQuizUtil.LIST_TYPES[i]);
			database.insert("streaks", null, contentValues);
		}
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}