package com.twlquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class TWLQuiz extends Activity {
	private final int PHONY_LETTER_TYPE   = 0;
	private final int GOOD_LETTER_TYPE    = 1;
	private final int REGULAR_LETTER_TYPE = 2;

	private final int MENU_TWOS   = 0;
	private final int MENU_THREES = 1;
	private final int MENU_FOURS  = 2;

	private final char[] ALPHABET = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private final char[] VOWELS   = {'A','E','I','O','U','Y'};

	private LinearLayout wordContainer;
	private TableLayout historyTable;
	private String currentWord;
	private boolean isGood = false;
	private HashMap wordList = new HashMap();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		wordContainer = (LinearLayout)findViewById(R.id.wordContainer);
		historyTable = (TableLayout)findViewById(R.id.history);

		loadWordList("twl_threes");
	}

	private void loadWordList(String list) {
		wordList.clear();
		
		try {
			InputStream inputStream = this.getResources().openRawResource(getResources().getIdentifier("com.twlquiz:raw/" + list, null, null));
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] parsedLine = line.split("\\s+"); 		    	
				wordList.put(parsedLine[0], parsedLine[1]);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		getWord();
	}

	public void displayWord(View view) {
		boolean gotItRight = false;

		switch (view.getId()) {
		case R.id.pressedGood :
			if (isGood) {
				gotItRight = true;
			}

			break;
		case R.id.pressedPhony :
			if (!isGood) {
				gotItRight = true;
			}

			break;
		default :
			break;
		}

		logHistory(gotItRight);
		getWord(); 
	}

	private void logHistory(Boolean gotItRight) {
		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));   

		LinearLayout guessedWord = formatHistoryText(currentWord, gotItRight ? GOOD_LETTER_TYPE : PHONY_LETTER_TYPE);
		guessedWord.setPadding(0, 0, 10, 0);

		tableRow.addView(guessedWord);
		tableRow.addView(formatHistoryText(isGood ? "GOOD" : "PHONY", isGood ? GOOD_LETTER_TYPE : PHONY_LETTER_TYPE));

		historyTable.addView(tableRow, 0, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private LinearLayout formatHistoryText(String text, int letterType) {
		LinearLayout historyText = new LinearLayout(getBaseContext());
		historyText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		return populateWordContainer(text, historyText, letterType);
	}

	private String getWord() {
		currentWord = (new Random().nextInt(2) == 1) ? realWord() : phonyWord(); 

		return currentWord;
	}

	private String realWord() {
		String realWord = generateRealWord();
		
		populateWordContainer(realWord, wordContainer, REGULAR_LETTER_TYPE);
		isGood = true;

		return realWord;
	}

	private String phonyWord() {
		String phonyWord = generatePhonyWord();

		while(wordList.containsKey(phonyWord) == true) {
			phonyWord = generatePhonyWord();
		}

		populateWordContainer(phonyWord, wordContainer, REGULAR_LETTER_TYPE);
		isGood = false;

		return phonyWord;
	}

	private String generateRealWord() {
		Object[] words =  wordList.keySet().toArray();

		return (String) words[new Random().nextInt(wordList.size())];
	}

	private String generatePhonyWord() {
		char[] phonyLetters = generateRealWord().toCharArray();

		int randomLetterIndex = new Random().nextInt(phonyLetters.length);

		phonyLetters[randomLetterIndex] = generatePhonyLetter(phonyLetters[randomLetterIndex]);

		return new String(phonyLetters);
	}

	private char generatePhonyLetter(char letterToSwap) {
		if (Arrays.asList(VOWELS).contains(letterToSwap)) {
			return randomLetter(VOWELS);
		} else {
			return randomLetter(ALPHABET);
		}
	}

	private char randomLetter(char[] source) {
		int seed = (int)(Math.random()*26);
		return source[seed];
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
			case PHONY_LETTER_TYPE:
				letterImage.setMaxHeight(40);
				letterImage.setMaxWidth(40);
				letterFileName = letterFileName.concat("phony_letter_" + letters[i]);
				break;
			case GOOD_LETTER_TYPE:
				letterImage.setMaxHeight(40);
				letterImage.setMaxWidth(40);
				letterFileName = letterFileName.concat("good_letter_" + letters[i]);
				break;
			case REGULAR_LETTER_TYPE:
				letterImage.setMaxHeight(80);
				letterImage.setMaxWidth(80);
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
			Log.i("TWOS:", "1");
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