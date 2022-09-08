package com.salwyrr.detection;

import java.text.Normalizer;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlexibleWordMatching {
	private final LinkedHashMap<WordEntry, Predicate<WordEntry>[]> wordsToDetect = new LinkedHashMap<>();
	private final Function<String, String>[] normalizers;

	@SafeVarargs
	public FlexibleWordMatching(Function<String, String>... normalizers) {
		this.normalizers = normalizers;
	}

	/**
	 * Add word to detect,
	 * multiple words can be added but spaces will be removed to match recognition process, note that spaces
	 * detections can still be done in the validators
	 * Wildcards ('*') are allowed to detect any character instead
	 * @param word word to add to detection list
	 * @param validators validators to apply custom check on presumed matches
	 */
	@SafeVarargs
	public final void addWord(String word, Predicate<WordEntry>... validators) {
		String orignal = word.replace(" ", "");
		String step1 = this.normalize(orignal);
		String step2 = this.completeNormalization(step1);
		this.wordsToDetect.put(new WordEntry(word, orignal, step1, step2, null, new ArrayList<>()), validators);
	}

	/**
	 * Add words to detect without validators
	 * @param words words to add to detection list
	 */
	public void addWords(String... words) {
		for (String word : words) {
			this.addWord(word);
		}
	}

	/**
	 * @return The list of words to detect
	 */
	public LinkedHashMap<WordEntry, Predicate<WordEntry>[]> getWordsToDetect() {
		return this.wordsToDetect;
	}

	/*
	 * First step normalization to make comparaison easier
	 */
	private String normalize(String text) {
		// Convert to lower case
		// ex. WéèélCoMe!! -> wéèélcome!!
		text = text.toLowerCase();

		// Convert to non-accentuated text
		// ex. wéèélcome!! -> weeelcome!!
		text = Normalizer.normalize(text, Normalizer.Form.NFD);
		text = text.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

		// Apply custom normalization
		for (Function<String, String> normalizer : this.normalizers) {
			text = normalizer.apply(text).toLowerCase(Locale.ROOT);
		}

		// Keep only letters, spaces and '*' (including alphabets other than english)
		// * are kept to detect words even when basic censoring (w*rd) is used
		// ex. weeelcome!! -> weeelcome
		text = text.replaceAll("[^\\pL *]", "");

		return text;
	}

	/*
	 * Second step normalization to allow anti-false-positive verification in first step
	 */
	private String completeNormalization(String text) {
		// Replace all repeating characters by only one
		// ex. weeelcome -> welcome
		char[] chars = text.toCharArray();
		for (char c : chars) {
			if (c == '*') continue; // ignore '*', we want to keep them all to recreate the original word
			String regex = "[" + Pattern.quote(String.valueOf(c)) + "]+";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(text);
			text = matcher.replaceAll(String.valueOf(c));
		}

		return text;
	}

	/**
	 * Search all matching results in the given text
	 * @param text text to search in
	 * @return All matching results
	 */
	public ArrayList<WordEntry> searchMatchesInText(String text) {
		String textFirstStep = this.normalize(text);
		String textSecondStep = this.completeNormalization(textFirstStep);

		String[] wordsOriginal = text.split(" ");
		String[] wordsFirstStep = textFirstStep.split(" ");
		String[] wordsSecondStep = textSecondStep.split(" ");
		
		ArrayList<WordEntry> detectedWords = new ArrayList<>();

		// Search in the source text using chunks of words
		// Will combine multiples words to find matches
		int part = 0; // number of words combined
		StringBuilder sub = new StringBuilder(); // Used to combine multiple words
		for (Map.Entry<WordEntry, Predicate<WordEntry>[]> entry : this.wordsToDetect.entrySet()) {
			for (int i = 0; i <= entry.getKey().getFullyNormalized().length(); i++) { // a word can be found in a single word, or split in
			                                                                        // a maximum of .length() words using spaces
				for (int wordIndex = 0; wordIndex < wordsSecondStep.length; wordIndex++) {
					sub.append(wordsSecondStep[wordIndex]);
					part++;

					if (part >= i || wordIndex + 1 >= wordsSecondStep.length) {
						match: {
							if (FlexibleWordMatching.equals(sub.toString(), entry.getKey().getFullyNormalized())) {
								StringBuilder subOriginal = new StringBuilder();
								StringBuilder subFirstStep = new StringBuilder();
								for (int j = wordIndex - part + 1; j <= wordIndex; j++) {
									if (subOriginal.length() > 0) subOriginal.append(" ");
									subOriginal.append(wordsOriginal[j]);
									subFirstStep.append(wordsFirstStep[j]);
								}

								WordEntry wordEntry = new WordEntry(
										subOriginal.toString(),
										subOriginal.toString().replace(" ", ""),
										subFirstStep.toString(),
										sub.toString(),
										entry.getKey(),
										detectedWords
								);

								// Test custom validators
								for (Predicate<WordEntry> validator : entry.getValue()) {
									if (!validator.test(wordEntry)) break match;
								}

								detectedWords.add(entry.getKey());

								sub = new StringBuilder();
								part = 0;
								i = entry.getKey().getFullyNormalized().length() + 1; // end previous for
								break;
							}
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

	protected static boolean equals(String toCheck, String toDetect) {
		if (toCheck.length() != toDetect.length()) return false;

		// Replace '*' by corresponding letters from word to detect
		toCheck = replaceWildcard(toCheck, toDetect);
		toDetect = replaceWildcard(toDetect, toCheck);

		return toCheck.equalsIgnoreCase(toDetect);
	}

	private static String replaceWildcard(String from, String to) {
		int i = 0;
		while (i < (i = from.indexOf("*", i))) {
			if (to.charAt(i) == '*') {
				i++;
				continue;
			}
			from = from.substring(0, i) + to.charAt(i) + from.substring(i + 1);
		}
		return from;
	}
}
