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

    /**
     * Use strictly latin letters from a to z only
     * @return corresponding function
     */
    public static Function<String, String> onlyLatin() {
        return text -> text.replaceAll("[^a-z *]", "");
    }

    /**
     * Replace cyrillic letters in the text by a similar latin letter
     * @return corresponding function
     */
    public static Function<String, String> cyrillicToLatin() {
        return text -> text
                .replace('а', 'a')
                .replace('ъ', 'b')
                .replace('ь', 'b')
                .replace('в', 'b')
                .replace('с', 'c')
                .replace('є', 'e')
                .replace('е', 'e')
                .replace('ә', 'e')
                .replace('ғ', 'f')
                .replace('н', 'h')
                .replace('һ', 'h')
                .replace('і', 'i')
                .replace('ј', 'j')
                .replace('к', 'k')
                .replace('м', 'm')
                .replace('и', 'n')
                .replace('о', 'o')
                .replace('р', 'p')
                .replace('я', 'r')
                .replace('г', 'r')
                .replace('ѕ', 's')
                .replace('о', 'o')
                .replace('ө', 'o')
                .replace('т', 't')
                .replace('ц', 'u')
                .replace('џ', 'u')
                .replace('ш', 'w')
                .replace('щ', 'w')
                .replace('х', 'x')
                .replace('ҳ', 'x')
                .replace('у', 'y')
                .replace('ү', 'y');
    }

}
