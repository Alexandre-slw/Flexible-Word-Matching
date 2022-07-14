package com.salwyrr.detection;

import java.util.function.Function;

public class TextNormalizer {

    /**
     * Replace all numbers in the text by a similar latin letter
     * @return corresponding function
     */
    public static Function<String, String> numberToSimilarLetter() {
        return text -> text
                .replace('0', 'O')
                .replace('1', 'I')
                .replace('2', 'Z')
                .replace('3', 'E')
                .replace('4', 'A')
                .replace('5', 'S')

                .replace('7', 'L')
                .replace('8', 'B')
                .replace('9', 'g');
    }

    /**
     * Make i and l the same to prevent the "I uppercase is looking like an l lowercase"
     * @return corresponding function
     */
    public static Function<String, String> iAndLAreSame() {
        return text -> text.replace('l', 'i');
    }

}
