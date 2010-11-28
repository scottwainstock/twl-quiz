package com.twlquiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class Stats extends TWLQuizUtil {
	private SQLiteDatabase database;
	WebView chart;
	TextView highScoreTWLTwos, highScoreTWLThrees, highScoreTWLFours, highScoreTWLAll;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);

		highScoreTWLTwos   = (TextView)findViewById(R.id.high_score_twl_twos);
		highScoreTWLThrees = (TextView)findViewById(R.id.high_score_twl_threes);
		highScoreTWLFours  = (TextView)findViewById(R.id.high_score_twl_fours);
		highScoreTWLAll    = (TextView)findViewById(R.id.high_score_twl_all);
		chart = (WebView)findViewById(R.id.chart);

		showStats();
		loadChart();
	}

	private void loadChart() {
		TWLQuizChart twlQuizChart = new TWLQuizChart(chart);

		chart.getSettings().setJavaScriptEnabled(true);
		chart.addJavascriptInterface(twlQuizChart, "twlquizchart");
		chart.loadUrl("file:///android_asset/flot/html/chart.html");
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