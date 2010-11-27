package com.twlquiz;

import android.app.Activity;

import java.util.Arrays;
import java.util.List;

public class TWLQuizUtil extends Activity {
	protected static final String[] LIST_TYPES = { "twl_twos", "twl_threes", "twl_fours", "all" };
	
	protected static final int BAD_LETTER_TYPE        = 0;
	protected static final int GOOD_LETTER_TYPE       = 1;
	protected static final int REGULAR_LETTER_TYPE    = 2;

	protected static final int QUIZ_LETTER_SIZE       = 80;
	protected static final int HISTORICAL_LETTER_SIZE = 40;

	protected static final int MENU_TWOS   = 0;
	protected static final int MENU_THREES = 1;
	protected static final int MENU_FOURS  = 2;
	protected static final int MENU_STATS  = 3;
	protected static final int MENU_PREFS  = 4;

	protected static final int DISPLAY_STREAK_MILESTONE  = 10;
	protected static final int INITIAL_FAIL_LIST_COUNTER = 2;

	protected static final List<Character> CONSONANTS = Arrays.asList('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Z');
	protected static final List<Character> VOWELS     = Arrays.asList('A','E','I','O','U','Y');

	protected static final int RESET_PREFERENCES = 1;

	protected static final String DATABASE_NAME    = "twl_quiz";
	protected static final String PREFERENCES_FILE = "twl_quiz_prefs";
}