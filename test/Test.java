import com.salwyrr.detection.FlexibleWordMatching;
import com.salwyrr.detection.TextNormalizer;
import com.salwyrr.detection.WordEntry;
import com.salwyrr.detection.WordValidators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		simpleTest();
		interpretTest();
		customNormalization();
		cyrillicDetection();
	}

	/*
	 * Salwyrr and b*st are detected, pogger is not because the match does not pass the validator (at least 2 'g')
	 */
	public static void simpleTest() {
		FlexibleWordMatching wordMatching = new FlexibleWordMatching();

		// Add words to detect
		wordMatching.addWords("Salwyrr", "b*st");
		wordMatching.addWord("pogger", WordValidators.containsAtLeast('g', 2));

		// Source text, here a user message
		String userMessage = "I love s al Wyyy r, that is the \"be*t\" thing ever, #poger";

		// Compute and display matches
		ArrayList<WordEntry> matches = wordMatching.searchMatchesInText(userMessage);

		for (WordEntry match : matches) {
			System.out.println(match.getOriginalWithSpaces());
		}
	}

	/*
	 * Try to interpret the match by finding if the message is addressed to someone
	 */
	public static void interpretTest() {
		FlexibleWordMatching wordMatching = new FlexibleWordMatching();

		List<String> youWords = Arrays.asList("you", "you're", "you'r", "u", "u're", "u'r");

		// Add words to detect, order is important for it to work as intended
		wordMatching.addWords(youWords.toArray(new String[0]));
		wordMatching.addWord("stupid", WordValidators.wasPreviouslyDetected(youWords));

		// "you're" and "stupid" are detected
		System.out.println("\nTest 1 --");
		for (WordEntry match : wordMatching.searchMatchesInText("you're so stupid")) {
			System.out.println(match.getOriginal());
		}

		// Nothing is detected
		System.out.println("\nTest 2 --");
		for (WordEntry match : wordMatching.searchMatchesInText("I am so stupid")) {
			System.out.println(match.getOriginal());
		}
	}

	/*
	 * Use specific normalization to match specific needs
	 */
	public static void customNormalization() {
		FlexibleWordMatching wordMatching = new FlexibleWordMatching(TextNormalizer.numberToSimilarLetter(), TextNormalizer.iAndLAreSame());

		wordMatching.addWord("Promise");

		System.out.println("\nCustom normalization --");
		for (WordEntry match : wordMatching.searchMatchesInText("This works I pr0mlse")) {
			System.out.println(match.getOriginal());
		}
	}

	/*
	 * Detect words when cyrillic letters are used to bypass
	 */
	public static void cyrillicDetection() {
		FlexibleWordMatching wordMatching = new FlexibleWordMatching(TextNormalizer.cyrillicToLatin(), TextNormalizer.onlyLatin());

		// Add words to detect
		wordMatching.addWords("Salwyrr");

		// Source text, here a user message, the "а" is a cyrillic letter
		String userMessage = "sаlwyrr";

		// Compute and display matches
		ArrayList<WordEntry> matches = wordMatching.searchMatchesInText(userMessage);

		System.out.println("\nCyrillic --");
		for (WordEntry match : matches) {
			System.out.println(match.getOriginalWithSpaces());
		}
	}

}
