package com.twlquiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Stats extends TWLQuizUtil {

	private SQLiteDatabase database;
	TextView highScoreTWLTwos, highScoreTWLThrees, highScoreTWLFours, highScoreTWLAll;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);

		highScoreTWLTwos   = (TextView)findViewById(R.id.high_score_twl_twos);
		highScoreTWLThrees = (TextView)findViewById(R.id.high_score_twl_threes);
		highScoreTWLFours  = (TextView)findViewById(R.id.high_score_twl_fours);
		highScoreTWLAll    = (TextView)findViewById(R.id.high_score_twl_all);

		showStats();
	}

	private void showStats() {
		database = new DatabaseHelper(this).getWritableDatabase();
		Cursor cursor = database.rawQuery("select high from streaks where type in (?, ?, ?, ?)", LIST_TYPES);
		
		cursor.moveToPosition(0);
		highScoreTWLAll.setText(cursor.getString(cursor.getColumnIndex("high")));
		
		cursor.moveToPosition(1);
		highScoreTWLFours.setText(cursor.getString(cursor.getColumnIndex("high")));

		cursor.moveToPosition(2);
		highScoreTWLThrees.setText(cursor.getString(cursor.getColumnIndex("high")));
		
		cursor.moveToPosition(3);
		highScoreTWLTwos.setText(cursor.getString(cursor.getColumnIndex("high")));

		cursor.close();
		database.close();
	}
}