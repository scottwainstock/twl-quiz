package com.twlquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class Preferences extends TWLQuizUtil {
	ToggleButton sound;
	ToggleButton tileView;
	SharedPreferences.Editor preferences;				

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		sound = (ToggleButton) findViewById(R.id.sound);
		tileView = (ToggleButton) findViewById(R.id.tile);
		preferences = getSharedPreferences(PREFERENCES_FILE, 0).edit();				

		initializePreferences();
	}

	private void initializePreferences() {
		sound.setChecked(getSharedPreferences(PREFERENCES_FILE, 0).getBoolean("sound", false) ? true : false);
		sound.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				preferences.putBoolean("sound", sound.isChecked() ? true : false).commit();
			}
		});

		tileView.setChecked(getSharedPreferences(PREFERENCES_FILE, 0).getBoolean("tileView", false) ? true : false);
		tileView.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				preferences.putBoolean("tileView", tileView.isChecked() ? true : false).commit();
			}
		});
	}
}