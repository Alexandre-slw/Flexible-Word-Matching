package com.salwyrr.detection;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class WordValidators {

    /**
     * Check if the non-fully normalized text match the text, increase a lot the risk of false-negative
     * @return corresponding predicate
     */
    public static Predicate<WordEntry> matchNonFullyNormalized() {
        return (e) -> FlexibleWordMatching.equals(e.getNormalizedWithMultiLetters(), e.getAssociatedEntry().getNormalizedWithMultiLetters());
    }

    /**
     * Check if the non-fully normalized text contains the letter at least nbOccurrences times
     * @param letter letter to find
     * @param nbOccurrences number of occurrences
     * @return corresponding predicate
     */
    public static Predicate<WordEntry> containsAtLeast(char letter, int nbOccurrences) {
        return (e) -> {
            String text = e.getNormalizedWithMultiLetters();
            for (int i = 0; i < nbOccurrences; i++) {
                if (!text.contains(String.valueOf(letter))) return false;

                int index = text.indexOf(letter);
                if (index + 1 >= text.length()) return i + 1 == nbOccurrences;
                text = text.substring(index + 1);
            }
            return true;
        };
    }

    /**
     * Check if the non-fully normalized text contains the letter exactly nbOccurrences times
     * @param letter letter to find
     * @param nbOccurrences number of occurrences
     * @return corresponding predicate
     */
    public static Predicate<WordEntry> containsExactly(char letter, int nbOccurrences) {
        return (e) -> {
            String text = e.getNormalizedWithMultiLetters();
            for (int i = 0; i < nbOccurrences; i++) {
                if (!text.contains(String.valueOf(letter))) return false;

                int index = text.indexOf(letter);
                if (index + 1 >= text.length()) return i + 1 == nbOccurrences;
                text = text.substring(index + 1);
            }
            return text.indexOf(letter) == -1;
        };
    }

    /**
     * Check if the non-fully normalized text contains the letter at most nbOccurrences times
     * @param letter letter to find
     * @param nbOccurrences number of occurrences
     * @return corresponding predicate
     */
    public static Predicate<WordEntry> containsAtMost(char letter, int nbOccurrences) {
        return (e) -> {
            String text = e.getNormalizedWithMultiLetters();
            for (int i = 0; i < nbOccurrences; i++) {
                if (!text.contains(String.valueOf(letter))) return true;

                int index = text.indexOf(letter);
                if (index + 1 >= text.length()) return true;
                text = text.substring(index + 1);
            }
            return text.indexOf(letter) == -1;
        };
    }

    /**
     * Check if the previous matches contains one of the given words
     * @param previousMatches words to find
     * @return corresponding predicate
     */
    public static Predicate<WordEntry> wasPreviouslyDetected(List<String> previousMatches) {
        return (e) -> {
            // Check if there was a "you" word detected previously
            for (WordEntry previous : e.getPreviousEntries()) {
                if (previousMatches.contains(previous.getOriginal())) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * Check if the previous matches contains one of the given words
     * @param previousMatches words to find
     * @return corresponding predicate
     */
    public static Predicate<WordEntry> wasPreviouslyDetected(String... previousMatches) {
        return WordValidators.wasPreviouslyDetected(Arrays.asList(previousMatches));
    }
}
