package com.salwyrr.detection;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlexibleWordMatching {
	private static final ArrayList<String> wordsToDetect = new ArrayList<>();

	/**
	 * Add word to detect,
	 * multiple words can be added but spaces and '*' will be removed to match recognition process
	 * @param word word to add to detection list
	 */
	public static void addWord(String word) {
		FlexibleWordMatching.wordsToDetect.add(FlexibleWordMatching.normalize(word)
				.replace(" ", "")
				.replace("*", ""));
	}

	/*
	 * Normalize text to improve comparaison
	 */
	private static String normalize(String text) {
		// Convert to lower case
		// ex. WéèélCoMe!! -> wéèélcome!!
		text = text.toLowerCase();

		// Convert to non-accentuated text
		// ex. wéèélcome!! -> weeelcome!!
		text = Normalizer.normalize(text, Normalizer.Form.NFD);
		text = text.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

		// Keep only letters, spaces and '*' (including alphabets other than english)
		// * are kept to detect words even when basic censoring (w*rd) is used
		// ex. weeelcome!! -> weeelcome
		text = text.replaceAll("[^\\pL *]", "");

		// Replace all repeating characters by only one
		// ex. weeelcome -> welcome
		char[] chars = text.toCharArray();
		for (char c : chars) {
			if (c == '*') continue; // ignore '*', we want to keep them all to recreate the original word
			String regex = "[" + Pattern.quote(String.valueOf(c)) + "]+";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(text);
			text = matcher.replaceAll(c + "");
		}

		return text;
	}

	/**
	 * Search all matching results in the given text
	 * @param text text to search in
	 * @return All matching results
	 */
	public static ArrayList<String> searchMatchInText(String text) {
		text = FlexibleWordMatching.normalize(text);
		String[] words = text.split(" ");
		
		ArrayList<String> detectedWords = new ArrayList<>();

		// Search in the source text using chunks of words
		// Will combine multiples words to find matches
		int part = 0; // number of words combined
		StringBuilder sub = new StringBuilder(); // Used to combine multiple words
		for (String wordToDetect : FlexibleWordMatching.wordsToDetect) {
			for (int i = 0; i <= wordToDetect.length(); i++) { // a word can be found in a single word, or split in
													           // a maximum of .length() words using spaces
				for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
					sub.append(words[wordIndex]);
					part++;

					if (part >= i || wordIndex + 1 >= words.length) {
						if (FlexibleWordMatching.equals(sub.toString(), wordToDetect)) {
							detectedWords.add(wordToDetect);
							
							sub = new StringBuilder();
							part = 0;
							i = wordToDetect.length() + 1; // end previous for
							break;
						}

						wordIndex -= part - 1; // put the wordIndex back to its original position, +1, to move forward
						                       // the start of the chunks of words by 1
						sub = new StringBuilder();
						part = 0;
					}
				}
			}
		}
		return detectedWords;
	}

	private static boolean equals(String toCheck, String toDetect) {
		if (toCheck.length() != toDetect.length()) return false;

		// Replace '*' by corresponding letters from word to detect
		while (toCheck.contains("*")) {
			int i = toCheck.indexOf('*');
			toCheck = toCheck.substring(0, i) + toDetect.charAt(i) + toCheck.substring(i + 1);
		}
		return toCheck.equalsIgnoreCase(toDetect);
	}
}
