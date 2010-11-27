package com.twlquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TWLQuiz extends TWLQuizUtil {
	private LinearLayout wordContainer;
	private TableLayout historyTable;
	private SQLiteDatabase database;
	private String currentList;
	private String currentWord;
	private int streakCounter = 0;
	private boolean isGood = false;
	private boolean tileView = false;
	private boolean playSound = false;
	private HashMap<String, String> wordList = new HashMap<String, String>();
	private HashMap<String, Integer> failList = new HashMap<String, Integer>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		wordContainer = (LinearLayout)findViewById(R.id.wordContainer);
		historyTable = (TableLayout)findViewById(R.id.history);		
		database = new DatabaseHelper(this).getWritableDatabase();

		loadPreferences();
		loadWordList("twl_threes");
	}

	private void loadPreferences() {
		playSound = getSharedPreferences(PREFERENCES_FILE, 0).getBoolean("sound", false) ? true : false;
		tileView = getSharedPreferences(PREFERENCES_FILE, 0).getBoolean("tileView", false) ? true : false;
	}

	private void loadWordList(String list) {
		streakCounter = 0;
		failList.clear();
		wordList.clear();
		historyTable.removeAllViews();
		currentList = list;

		try {
			InputStream inputStream = this.getResources().openRawResource(getResources().getIdentifier("com.twlquiz:raw/" + list, null, null));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] parsedLine = line.split("\\s+", 2);
				wordList.put(parsedLine[0], parsedLine[1]);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		getWord();
	}

	private void addToFailList() {
		failList.remove(currentWord);
		failList.put(currentWord, INITIAL_FAIL_LIST_COUNTER);		
	}

	private void decrementFailList() {
		if (failList.containsKey(currentWord)) {
			if (failList.get(currentWord) == 0) {
				failList.remove(currentWord);
			} else {
				failList.put(currentWord, failList.get(currentWord) - 1);
			}
		}
	}

	private void incrementStreak() {
		streakCounter++;

		if (streakCounter % DISPLAY_STREAK_MILESTONE == 0) {
			playSound("streak");
			Toast.makeText(getBaseContext(), "STREAK: " + Integer.toString(streakCounter), Toast.LENGTH_SHORT).show();
		}
	}

	private void playSound(String fileName) {
		if (playSound) {
			MediaPlayer.create(getBaseContext(), getResources().getIdentifier("com.twlquiz:raw/" + fileName, null, null)).start();		
		}
	}

	private void youGotItRight() {
		playSound("good");
		incrementStreak();
		decrementFailList();
	}

	private void youGotItWrong() {
		playSound("bad");

		Cursor cursor = database.rawQuery("select high from streaks where type=?", new String[] { currentList });
		cursor.moveToFirst();
		int currentHigh = cursor.getInt(cursor.getColumnIndex("high"));

		if ((currentHigh == 0) || (currentHigh < streakCounter)) {
			database.execSQL("update streaks set high = ? where type = ?", new String[] { Integer.toString(streakCounter), currentList });
		}

		cursor.close();

		streakCounter = 0;
		addToFailList();
	}

	public void displayWord(View view) {
		switch (view.getId()) {
		case R.id.pressedGood :
			if (!isGood) {
				youGotItWrong();
			} else {
				youGotItRight();
			}

			logHistory(true);

			break;
		case R.id.pressedBad :
			if (isGood) {
				youGotItWrong();
			} else {
				youGotItRight();
			}

			logHistory(false);

			break;
		default :
			break;
		}

		getWord(); 
	}

	private void logHistory(Boolean yourGuess) {
		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)); 
		tableRow.setContentDescription(currentWord);

		tableRow.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View view) {
				Object definition = wordList.get(view.getContentDescription());
				if (definition == null) {
					definition = "THIS IS NOT A WORD";
				} 

				Toast toastDefinition = Toast.makeText(getBaseContext(), (CharSequence) definition, Toast.LENGTH_LONG);
				toastDefinition.setGravity(Gravity.TOP, 0, 0);
				toastDefinition.show();

				return false;
			}
		});

		LinearLayout guessedWord = formatHistoryText(currentWord, isGood ? GOOD_LETTER_TYPE : BAD_LETTER_TYPE);
		guessedWord.setPadding(0, 0, 10, 0);

		tableRow.addView(guessedWord);
		tableRow.addView(formatHistoryText(yourGuess ? "GOOD" : "BAD", yourGuess ? GOOD_LETTER_TYPE : BAD_LETTER_TYPE));

		historyTable.addView(tableRow, 0, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private LinearLayout formatHistoryText(String text, int letterType) {
		LinearLayout historyText = new LinearLayout(getBaseContext());
		historyText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		return populateWordContainer(text, historyText, letterType);
	}

	private String getWord() {
		String newWord = currentWord;
		int random = new Random().nextInt(10);

		while(newWord == currentWord) {
			if ((random <= 1) && !failList.isEmpty()) {
				newWord = failWord();
			} else if (random <= 5) {
				newWord = realWord();
			} else {
				newWord = badWord();
			}
		}

		currentWord = newWord;

		return currentWord;
	}

	private String failWord() {
		String failWord = pickRandomWord(failList);

		populateWordContainer(failWord, wordContainer, REGULAR_LETTER_TYPE);

		isGood = wordList.containsKey(failWord) ? true : false;

		return failWord;
	}

	private String realWord() {
		String realWord = pickRandomWord(wordList);

		populateWordContainer(realWord, wordContainer, REGULAR_LETTER_TYPE);
		isGood = true;

		return realWord;
	}

	private String badWord() {
		String badWord = generateBadWord();

		while(wordList.containsKey(badWord) == true) {
			badWord = generateBadWord();
		}

		populateWordContainer(badWord, wordContainer, REGULAR_LETTER_TYPE);
		isGood = false;

		return badWord;
	}

	@SuppressWarnings("unchecked")
	private String pickRandomWord(HashMap list) {
		Object[] words =  list.keySet().toArray();

		return (String) words[new Random().nextInt(list.size())];
	}

	private String generateBadWord() {
		char[] badLetters = pickRandomWord(wordList).toCharArray();
		int randomLetterIndex = new Random().nextInt(badLetters.length);

		badLetters[randomLetterIndex] = generateBadLetter(badLetters[randomLetterIndex]);

		return new String(badLetters);
	}

	private char generateBadLetter(char letterToSwap) {
		if (VOWELS.contains(letterToSwap)) {
			return (new Random().nextInt(10) == 0) ? 'Y' : randomLetter(VOWELS);
		} else {
			char random = randomLetter(CONSONANTS);

			while (random == 'Z' || random == 'Q') {
				if (new Random().nextInt(10) <= 1) {
					return random;
				} else {
					random = randomLetter(CONSONANTS);
				}
			}

			return random;
		}
	}

	private char randomLetter(List<Character> list) {
		return list.get((int)(Math.random() * list.size()));
	}

	private LinearLayout populateWordContainer(String word, LinearLayout container, int letterType) {
		container.removeAllViews();

		if (tileView || (letterType == REGULAR_LETTER_TYPE)) {
			word = word.toLowerCase();
		}

		char[] letters = word.toCharArray();
		for (int i = 0; i < letters.length; i++) {
			String letterFileName = "com.twlquiz:drawable/";
			TextView letterText = new TextView(this);
			ImageView letterImage = new ImageView(this);
			letterImage.setAdjustViewBounds(true);

			switch (letterType) {
			case BAD_LETTER_TYPE:
				if (tileView) {
					letterImage.setMaxHeight(HISTORICAL_LETTER_SIZE);
					letterImage.setMaxWidth(HISTORICAL_LETTER_SIZE);
					letterFileName = letterFileName.concat("bad_letter_" + letters[i]);
				} else {
					letterText.setBackgroundColor(Color.WHITE);
					letterText.setTextColor(Color.BLACK);
				}

				break;
			case GOOD_LETTER_TYPE:
				if (tileView) {
					letterImage.setMaxHeight(HISTORICAL_LETTER_SIZE);
					letterImage.setMaxWidth(HISTORICAL_LETTER_SIZE);
					letterFileName = letterFileName.concat("good_letter_" + letters[i]);
				} else {
					letterText.setBackgroundColor(Color.BLACK);
					letterText.setTextColor(Color.WHITE);
				}
				
				break;
			case REGULAR_LETTER_TYPE:
				letterImage.setMaxHeight(QUIZ_LETTER_SIZE);
				letterImage.setMaxWidth(QUIZ_LETTER_SIZE);
				letterFileName = letterFileName.concat("letter_" + letters[i]);
				
				break;
			default:
				break;
			}

			if (tileView || (letterType == REGULAR_LETTER_TYPE)) {
				letterImage.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(letterFileName, null, null)));
				container.addView(letterImage);
			} else {
				letterText.setTextSize(HISTORICAL_LETTER_SIZE);
				letterText.setText(Character.toString(letters[i]));
				container.addView(letterText);
			}
		}

		return container;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(MENU_TWOS, MENU_TWOS, MENU_TWOS, "2s");
		menu.add(MENU_THREES, MENU_THREES, MENU_THREES, "3s");
		menu.add(MENU_FOURS, MENU_FOURS, MENU_FOURS, "4s");
		menu.add(MENU_STATS, MENU_STATS, MENU_STATS, "Stats");
		menu.add(MENU_PREFS, MENU_PREFS, MENU_PREFS, "Setup");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_TWOS:
			loadWordList("twl_twos");
			return true;
		case MENU_THREES:
			loadWordList("twl_threes");
			return true;
		case MENU_FOURS:
			loadWordList("twl_fours");
			return true;
		case MENU_STATS:
			Intent statsIntent = new Intent();
			statsIntent.setClassName("com.twlquiz", "com.twlquiz.Stats");
			startActivityForResult(statsIntent, 22);
			return true;
		case MENU_PREFS:
			Intent prefIntent = new Intent();
			prefIntent.setClassName("com.twlquiz", "com.twlquiz.Preferences");
			startActivityForResult(prefIntent, 22);
			return true;
		default:
			return false;
		}
	}
}