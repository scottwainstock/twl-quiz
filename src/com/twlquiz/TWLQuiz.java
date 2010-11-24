package com.twlquiz;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TWLQuiz extends Activity {
	private final int BAD_LETTER_TYPE        = 0;
	private final int GOOD_LETTER_TYPE       = 1;
	private final int REGULAR_LETTER_TYPE    = 2;

	private final int QUIZ_LETTER_SIZE       = 80;
	private final int HISTORICAL_LETTER_SIZE = 40;

	private final int MENU_TWOS   = 0;
	private final int MENU_THREES = 1;
	private final int MENU_FOURS  = 2;
	
	private final int DISPLAY_STREAK_MILESTONE  = 5;
	private final int INITIAL_FAIL_LIST_COUNTER = 2;

	private final List<Character> CONSONANTS = Arrays.asList('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Z');
	private final List<Character> VOWELS     = Arrays.asList('A','E','I','O','U','Y');

	private LinearLayout wordContainer;
	private TableLayout historyTable;
	private String currentWord;
	private int streakCounter = 0;
	private boolean isGood = false;
	private HashMap<String, String> wordList = new HashMap<String, String>();
	private HashMap<String, Integer> failList = new HashMap<String, Integer>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		wordContainer = (LinearLayout)findViewById(R.id.wordContainer);
		historyTable = (TableLayout)findViewById(R.id.history);

		loadWordList("twl_threes");
	}

	private void loadWordList(String list) {
		streakCounter = 0;
		failList.clear();
		wordList.clear();
		historyTable.removeAllViews();

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
	
	public void addToFailList() {
		failList.remove(currentWord);
		failList.put(currentWord, INITIAL_FAIL_LIST_COUNTER);		
	}
	
	public void decrementFailList() {
		if (failList.containsKey(currentWord)) {
			if (failList.get(currentWord) == 0) {
				failList.remove(currentWord);
			} else {
				failList.put(currentWord, failList.get(currentWord) - 1);
			}
		}
	}
	
	public void incrementStreak() {
		streakCounter++;
		
		if (streakCounter % DISPLAY_STREAK_MILESTONE == 0) {
			Toast.makeText(getBaseContext(), "STREAK: " + Integer.toString(streakCounter), Toast.LENGTH_SHORT).show();
		}
	}

	public void displayWord(View view) {
		switch (view.getId()) {
		case R.id.pressedGood :
			if (!isGood) {
				addToFailList();
			} else {
				incrementStreak();
				decrementFailList();
			}
			
			logHistory(true);

			break;
		case R.id.pressedBad :
			if (isGood) {
				addToFailList();
			} else {
				incrementStreak();
				decrementFailList();
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
		currentWord = (new Random().nextInt(2) == 1) ? realWord() : badWord(); 

		return currentWord;
	}

	private String realWord() {
		String realWord = generateRealWord();

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

	private String generateRealWord() {
		Object[] words =  wordList.keySet().toArray();

		return (String) words[new Random().nextInt(wordList.size())];
	}

	private String generateBadWord() {
		char[] badLetters = generateRealWord().toCharArray();
		int randomLetterIndex = new Random().nextInt(badLetters.length);

		badLetters[randomLetterIndex] = generateBadLetter(badLetters[randomLetterIndex]);

		return new String(badLetters);
	}

	private char generateBadLetter(char letterToSwap) {
		if (VOWELS.contains(letterToSwap)) {
			return randomLetter(VOWELS);
		} else {
			return randomLetter(CONSONANTS);
		}
	}

	private char randomLetter(List<Character> list) {
		return list.get((int)(Math.random() * list.size()));
	}

	private LinearLayout populateWordContainer(String word, LinearLayout container, int letterType) {
		word = word.toLowerCase();
		container.removeAllViews();

		char[] letters = word.toCharArray();

		for (int i = 0; i < letters.length; i++) {
			String letterFileName = "com.twlquiz:drawable/";
			ImageView letterImage = new ImageView(this);
			letterImage.setAdjustViewBounds(true);

			switch (letterType) {
			case BAD_LETTER_TYPE:
				letterImage.setMaxHeight(HISTORICAL_LETTER_SIZE);
				letterImage.setMaxWidth(HISTORICAL_LETTER_SIZE);
				letterFileName = letterFileName.concat("bad_letter_" + letters[i]);
				break;
			case GOOD_LETTER_TYPE:
				letterImage.setMaxHeight(HISTORICAL_LETTER_SIZE);
				letterImage.setMaxWidth(HISTORICAL_LETTER_SIZE);
				letterFileName = letterFileName.concat("good_letter_" + letters[i]);
				break;
			case REGULAR_LETTER_TYPE:
				letterImage.setMaxHeight(QUIZ_LETTER_SIZE);
				letterImage.setMaxWidth(QUIZ_LETTER_SIZE);
				letterFileName = letterFileName.concat("letter_" + letters[i]);
				break;
			default:
				break;
			}

			letterImage.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(letterFileName, null, null)));

			container.addView(letterImage);
		}

		return container;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(MENU_TWOS, MENU_TWOS, MENU_TWOS, "2s");
		menu.add(MENU_THREES, MENU_THREES, MENU_THREES, "3s");
		menu.add(MENU_FOURS, MENU_FOURS, MENU_FOURS, "4s");

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
		default:
			return false;
		}
	}
}