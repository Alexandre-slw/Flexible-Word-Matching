package com.salwyrr.detection;

import java.util.List;

public class WordEntry {

    private final String originalWithSpaces;
    private final String original;
    private final String step1;
    private final String step2;
    private final WordEntry associatedEntry;
    private final List<WordEntry> previousEntries;

    public WordEntry(String originalWithSpaces, String original, String step1, String step2, WordEntry associatedEntry, List<WordEntry> previousEntries) {
        this.originalWithSpaces = originalWithSpaces;
        this.original = original;
        this.step1 = step1;
        this.step2 = step2;
        this.associatedEntry = associatedEntry;
        this.previousEntries = previousEntries;
    }

    /**
     * Returns the matched text but without any changes to it
     * @return matched text without any changes, spaces still there and no normalization at all
     */
    public String getOriginalWithSpaces() {
        return this.originalWithSpaces;
    }

    /**
     * Returns the matched text but without any normalization and spaces are not present to represent the way the detection works
     * @return matched text without spaces and normalization
     */
    public String getOriginal() {
        return this.original;
    }

    /**
     * Returns the normalized matched text but still with multiple letters to allow better recognition and anti-false-positive
     * @return normalized text with multiple letters still there
     */
    public String getNormalizedWithMultiLetters() {
        return this.step1;
    }

    /**
     * Returns the fully normalized matched text used in detection
     * @return fully normalized text
     */
    public String getFullyNormalized() {
        return this.step2;
    }

    /**
     * Returns an associated WordEntry if any (ex. detection list entries for validators)
     * @return Associated word entry
     */
    public WordEntry getAssociatedEntry() {
        return this.associatedEntry;
    }

    /**
     * Returns previous WordEntries if any (ex. previous matched entries for validators)
     * @return Previous word entries
     */
    public List<WordEntry> getPreviousEntries() {
        return this.previousEntries;
    }
}
