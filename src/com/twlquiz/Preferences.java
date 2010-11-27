package com.twlquiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class Preferences extends TWLQuizUtil {

	ToggleButton sound;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		sound = (ToggleButton) findViewById(R.id.sound);
		
		sound.setChecked(getSharedPreferences(PREFERENCES_FILE, 0).getBoolean("sound", false) ? true : false);
		sound.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				SharedPreferences.Editor preferences = getSharedPreferences(PREFERENCES_FILE, 0).edit();				
				preferences.putBoolean("sound", sound.isChecked() ? true : false).commit();
			}
		});
	}
}