package com.twlquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class Preferences extends TWLQuizUtil {
	Button resetStats;
	ToggleButton sound;
	ToggleButton tileView;
	SQLiteDatabase database;
	SharedPreferences.Editor preferences;
	static Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		context = this;

		sound = (ToggleButton) findViewById(R.id.sound);
		tileView = (ToggleButton) findViewById(R.id.tile);
		resetStats = (Button) findViewById(R.id.reset_stats);
		preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();	
		database = new DatabaseHelper(this).getWritableDatabase();

		initializePreferences();
	}

	private void initializePreferences() {
		setupToggleButton(sound, "sound");
		setupToggleButton(tileView, "tileView");

		resetStats.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				AlertDialog.Builder confirm = new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert);
				confirm.setTitle("Clear Statistic Data?").setMessage("Are you sure you want to reset your stats?");
				confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						DatabaseHelper.clearStats(database);
					}

				});
				confirm.setNegativeButton("No", null);
				confirm.show();

				return;
			}
		});
	}

	private void setupToggleButton(final ToggleButton button, final String prefName) {
		button.setChecked(getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getBoolean(prefName, true) ? true : false);
		
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				preferences.putBoolean(prefName, button.isChecked() ? true : false).commit();
			}
		});
	}
}