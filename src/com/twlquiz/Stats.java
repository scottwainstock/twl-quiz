package com.twlquiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Stats extends TWLQuizUtil {

	private SQLiteDatabase database;
	TextView highScoreTWLTwos;
	TextView highScoreTWLThrees;
	TextView highScoreTWLFours;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);

		highScoreTWLTwos = (TextView)findViewById(R.id.high_score_twl_twos);
		highScoreTWLThrees = (TextView)findViewById(R.id.high_score_twl_threes);
		highScoreTWLFours = (TextView)findViewById(R.id.high_score_twl_fours);

		showStats();
	}

	private void showStats() {
		database = new DatabaseHelper(this).getWritableDatabase();
		Cursor cursor = database.rawQuery("select high from streaks where type in (?, ?, ?)", new String[] { "twl_twos", "twl_threes", "twl_fours" });
		
		cursor.moveToPosition(0);
		highScoreTWLFours.setText(cursor.getString(cursor.getColumnIndex("high")));

		cursor.moveToPosition(1);
		highScoreTWLThrees.setText(cursor.getString(cursor.getColumnIndex("high")));
		
		cursor.moveToPosition(2);
		highScoreTWLTwos.setText(cursor.getString(cursor.getColumnIndex("high")));

		cursor.close();
		database.close();
	}
}