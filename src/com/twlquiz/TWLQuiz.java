package com.twlquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TWLQuiz extends Activity {
	private final int PHONY_LETTER_TYPE   = 0;
	private final int GOOD_LETTER_TYPE    = 1;
	private final int REGULAR_LETTER_TYPE = 2;

	private final char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private final char[] vowels = {'A','E','I','O','U','Y'};

	private LinearLayout wordContainer;
	private TableLayout historyTable;
	private String currentWord;
	private boolean isGood = false;
	private String[] twlThrees = { 
			"AAH", "AAL", "AAS", "ABA", "ABO", "ABS", "ABY", "ACE", "ACT", "ADD", "ADO", "ADS", "ADZ", "AFF", "AFT", "AGA", "AGE", "AGO", "AGS", "AHA", "AHI", "AHS", "AID", "AIL", "AIM", "AIN", "AIR", "AIS", "AIT", "ALA", "ALB", "ALE", "ALL", "ALP", "ALS", "ALT", "AMA", "AMI", "AMP", "AMU", "ANA", "AND", "ANE", "ANI", "ANT", "ANY", "APE", "APO", "APP", "APT", "ARB", "ARC", "ARE", "ARF", "ARK", "ARM", "ARS", "ART", "ASH", "ASK", "ASP", "ASS", "ATE", "ATT", "AUK", "AVA", "AVE", "AVO", "AWA", "AWE", "AWL", "AWN", "AXE", "AYE", "AYS", "AZO", "BAA", "BAD", "BAG", "BAH", "BAL", "BAM", "BAN", "BAP", "BAR", "BAS", "BAT", "BAY", "BED", "BEE", "BEG", "BEL", "BEN", "BES", "BET", "BEY", "BIB", "BID", "BIG", "BIN", "BIO", "BIS", "BIT", "BIZ", "BOA", "BOB", "BOD", "BOG", "BOO", "BOP", "BOS", "BOT", "BOW", "BOX", "BOY", "BRA", "BRO", "BRR", "BUB", "BUD", "BUG", "BUM", "BUN", "BUR", "BUS", "BUT", "BUY", "BYE", "BYS", "CAB", "CAD", "CAM", "CAN", "CAP", "CAR", "CAT", "CAW", "CAY", "CEE", "CEL", "CEP", "CHI", "CIG", "CIS", "COB", "COD", "COG", "COL", "CON", "COO", "COP", "COR", "COS", "COT", "COW", "COX", "COY", "COZ", "CRU", "CRY", "CUB", "CUD", "CUE", "CUM", "CUP", "CUR", "CUT", "CWM", "DAB", "DAD", "DAG", "DAH", "DAK", "DAL", "DAM", "DAN", "DAP", "DAW", "DAY", "DEB", "DEE", "DEF", "DEL", "DEN", "DEV", "DEW", "DEX", "DEY", "DIB", "DID", "DIE", "DIF", "DIG", "DIM", "DIN", "DIP", "DIS", "DIT", "DOC", "DOE", "DOG", "DOL", "DOM", "DON", "DOR", "DOS", "DOT", "DOW", "DRY", "DUB", "DUD", "DUE", "DUG", "DUH", "DUI", "DUN", "DUO", "DUP", "DYE", "EAR", "EAT", "EAU", "EBB", "ECU", "EDH", "EDS", "EEK", "EEL", "EFF", "EFS", "EFT", "EGG", "EGO", "EKE", "ELD", "ELF", "ELK", "ELL", "ELM", "ELS", "EME", "EMS", "EMU", "END", "ENG", "ENS", "EON", "ERA", "ERE", "ERG", "ERN", "ERR", "ERS", "ESS", "ETA", "ETH", "EVE", "EWE", "EYE", "FAB", "FAD", "FAG", "FAN", "FAR", "FAS", "FAT", "FAX", "FAY", "FED", "FEE", "FEH", "FEM", "FEN", "FER", "FES", "FET", "FEU", "FEW", "FEY", "FEZ", "FIB", "FID", "FIE", "FIG", "FIL", "FIN", "FIR", "FIT", "FIX", "FIZ", "FLU", "FLY", "FOB", "FOE", "FOG", "FOH", "FON", "FOP", "FOR", "FOU", "FOX", "FOY", "FRO", "FRY", "FUB", "FUD", "FUG", "FUN", "FUR", "GAB", "GAD", "GAE", "GAG", "GAL", "GAM", "GAN", "GAP", "GAR", "GAS", "GAT", "GAY", "GED", "GEE", "GEL", "GEM", "GEN", "GET", "GEY", "GHI", "GIB", "GID", "GIE", "GIG", "GIN", "GIP", "GIT", "GNU", "GOA", "GOB", "GOD", "GOO", "GOR", "GOS", "GOT", "GOX", "GOY", "GUL", "GUM", "GUN", "GUT", "GUV", "GUY", "GYM", "GYP", "HAD", "HAE", "HAG", "HAH", "HAJ", "HAM", "HAO", "HAP", "HAS", "HAT", "HAW", "HAY", "HEH", "HEM", "HEN", "HEP", "HER", "HES", "HET", "HEW", "HEX", "HEY", "HIC", "HID", "HIE", "HIM", "HIN", "HIP", "HIS", "HIT", "HMM", "HOB", "HOD", "HOE", "HOG", "HON", "HOP", "HOT", "HOW", "HOY", "HUB", "HUE", "HUG", "HUH", "HUM", "HUN", "HUP", "HUT", "HYP", "ICE", "ICH", "ICK", "ICY", "IDS", "IFF", "IFS", "IGG", "ILK", "ILL", "IMP", "INK", "INN", "INS", "ION", "IRE", "IRK", "ISM", "ITS", "IVY", "JAB", "JAG", "JAM", "JAR", "JAW", "JAY", "JEE", "JET", "JEU", "JEW", "JIB", "JIG", "JIN", "JOB", "JOE", "JOG", "JOT", "JOW", "JOY", "JUG", "JUN", "JUS", "JUT", "KAB", "KAE", "KAF", "KAS", "KAT", "KAY", "KEA", "KEF", "KEG", "KEN", "KEP", "KEX", "KEY", "KHI", "KID", "KIF", "KIN", "KIP", "KIR", "KIS", "KIT", "KOA", "KOB", "KOI", "KOP", "KOR", "KOS", "KUE", "KYE", "LAB", "LAC", "LAD", "LAG", "LAM", "LAP", "LAR", "LAS", "LAT", "LAV", "LAW", "LAX", "LAY", "LEA", "LED", "LEE", "LEG", "LEI", "LEK", "LET", "LEU", "LEV", "LEX", "LEY", "LEZ", "LIB", "LID", "LIE", "LIN", "LIP", "LIS", "LIT", "LOB", "LOG", "LOO", "LOP", "LOT", "LOW>", "LOX", "LUG", "LUM", "LUV", "LUX", "LYE", "MAC", "MAD", "MAE", "MAG", "MAN", "MAP", "MAR", "MAS", "MAT", "MAW", "MAX", "MAY", "MED", "MEG", "MEL", "MEM", "MEN", "MET", "MEW", "MHO", "MIB", "MIC", "MID", "MIG", "MIL", "MIM", "MIR", "MIS", "MIX", "MOA", "MOB", "MOC", "MOD", "MOG", "MOL", "MOM", "MON", "MOO", "MOP", "MOR", "MOS", "MOT", "MOW", "MUD", "MUG", "MUM", "MUN", "MUS", "MUT", "MYC", "NAB", "NAE", "NAG", "NAH", "NAM", "NAN", "NAP", "NAW", "NAY", "NEB", "NEE", "NEG", "NET", "NEW", "NIB", "NIL", "NIM", "NIP", "NIT", "NIX", "NOB", "NOD", "NOG", "NOH", "NOM", "NOO", "NOR", "NOS", "NOT", "NOW", "NTH", "NUB", "NUN", "NUS", "NUT", "OAF", "OAK", "OAR", "OAT", "OBA", "OBE", "OBI", "OCA", "ODA", "ODD", "ODE", "ODS", "OES", "OFF", "OFT", "OHM", "OHO", "OHS", "OIL", "OKA", "OKE", "OLD", "OLE", "OMS", "ONE", "ONO", "ONS", "OOH", "OOT", "OPE", "OPS", "OPT", "ORA", "ORB", "ORC", "ORE", "ORS", "ORT", "OSE", "OUD", "OUR", "OUT", "OVA", "OWE", "OWL", "OWN", "OXO", "OXY", "PAC", "PAD", "PAH", "PAL", "PAM", "PAN", "PAP", "PAR", "PAS", "PAT", "PAW", "PAX", "PAY", "PEA", "PEC", "PED", "PEE", "PEG", "PEH", "PEN", "PEP", "PER", "PES", "PET", "PEW", "PHI", "PHT", "PIA", "PIC", "PIE", "PIG", "PIN", "PIP", "PIS", "PIT", "PIU", "PIX", "PLY", "POD", "POH", "POI", "POL", "POM", "POP", "POT", "POW", "POX", "PRO", "PRY", "PSI", "PST", "PUB", "PUD", "PUG", "PUL", "PUN", "PUP", "PUR", "PUS", "PUT", "PYA", "PYE", "PYX", "QAT", "QIS", "QUA", "RAD", "RAG", "RAH", "RAI", "RAJ", "RAM", "RAN", "RAP", "RAS", "RAT", "RAW", "RAX", "RAY", "REB", "REC", "RED", "REE", "REF", "REG", "REI", "REM", "REP", "RES", "RET", "REV", "REX", "RHO", "RIA", "RIB", "RID", "RIF", "RIG", "RIM", "RIN", "RIP", "ROB", "ROC", "ROD", "ROE", "ROM", "ROT", "ROW", "RUB", "RUE", "RUG", "RUM", "RUN", "RUT", "RYA", "RYE", "SAB", "SAC", "SAD", "SAE", "SAG", "SAL", "SAP", "SAT", "SAU", "SAW", "SAX", "SAY", "SEA", "SEC", "SEE", "SEG", "SEI", "SEL", "SEN", "SER", "SET", "SEW", "SEX", "SHA", "SHE", "SHH", "SHY", "SIB", "SIC", "SIM", "SIN", "SIP", "SIR", "SIS", "SIT", "SIX", "SKA", "SKI", "SKY", "SLY", "SOB", "SOD", "SOL", "SOM", "SON", "SOP", "SOS", "SOT", "SOU", "SOW", "SOX", "SOY", "SPA", "SPY", "SRI", "STY", "SUB", "SUE", "SUK", "SUM", "SUN", "SUP", "SUQ", "SYN", "TAB", "TAD", "TAE", "TAG", "TAJ", "TAM", "TAN", "TAO", "TAP", "TAR", "TAS", "TAT", "TAU", "TAV", "TAW", "TAX", "TEA", "TED", "TEE", "TEG", "TEL", "TEN", "TET", "TEW", "THE", "THO", "THY", "TIC", "TIE", "TIL", "TIN", "TIP", "TIS", "TIT", "TOD", "TOE", "TOG", "TOM", "TON", "TOO", "TOP", "TOR", "TOT", "TOW", "TOY", "TRY", "TSK", "TUB", "TUG", "TUI", "TUN", "TUP", "TUT", "TUX", "TWA", "TWO", "TYE", "UDO", "UGH", "UKE", "ULU", "UMM", "UMP", "UNS", "UPO", "UPS", "URB", "URD", "URN", "URP", "USE", "UTA", "UTE", "UTS", "VAC", "VAN", "VAR", "VAS", "VAT", "VAU", "VAV", "VAW", "VEE", "VEG", "VET", "VEX", "VIA", "VID", "VIE", "VIG", "VIM", "VIS", "VOE", "VOW", "VOX", "VUG", "VUM", "WAB", "WAD", "WAE", "WAG", "WAN", "WAP", "WAR", "WAS", "WAT", "WAW", "WAX", "WAY", "WEB", "WED", "WEE", "WEN", "WET", "WHA", "WHO", "WHY", "WIG", "WIN", "WIS", "WIT", "WIZ", "WOE", "WOG", "WOK", "WON", "WOO", "WOP", "WOS", "WOT", "WOW", "WRY", "WUD", "WYE", "WYN", "XIS", "YAG", "YAH", "YAK", "YAM", "YAP", "YAR", "YAW", "YAY", "YEA", "YEH", "YEN", "YEP", "YES", "YET", "YEW", "YID", "YIN", "YIP", "YOB", "YOD", "YOK", "YOM", "YON", "YOU", "YOW", "YUK", "YUM", "YUP", "ZAG", "ZAP", "ZAS", "ZAX", "ZED", "ZEE", "ZEK", "ZEP", "ZIG", "ZIN", "ZIP", "ZIT", "ZOA", "ZOO", "ZUZ", "ZZZ"		
	};
	private List<String> twlThreesList = Arrays.asList(twlThrees);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		wordContainer = (LinearLayout)findViewById(R.id.wordContainer);
		historyTable = (TableLayout)findViewById(R.id.history);

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

		while(twlThreesList.contains(phonyWord) == true) {
			phonyWord = generatePhonyWord();
		}

		populateWordContainer(phonyWord, wordContainer, REGULAR_LETTER_TYPE);
		isGood = false;

		return phonyWord;
	}

	private String generateRealWord() {
		return twlThrees[new Random().nextInt(twlThrees.length)];
	}

	private String generatePhonyWord() {
		char[] phonyLetters = generateRealWord().toCharArray();

		int randomLetterIndex = new Random().nextInt(phonyLetters.length);

		phonyLetters[randomLetterIndex] = generatePhonyLetter(phonyLetters[randomLetterIndex]);

		return new String(phonyLetters);
	}

	private char generatePhonyLetter(char letterToSwap) {
		if (Arrays.asList(vowels).contains(letterToSwap)) {
			return randomLetter(vowels);
		} else {
			return randomLetter(alphabet);
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
				letterImage.setMaxHeight(90);
				letterImage.setMaxWidth(90);
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
}
