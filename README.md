# Flexible Word Matching
 Find words in text where common detection bypass are used such as '*' or word splitting using spaces or even some word alteration.
 Custom filter and detection process can be applied to have the best results according to any situation.

# Basic example

```Java
import com.salwyrr.detection.*;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
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

}
```

### Output

```
Salwyrr
b*st
```
pogger is not detected because we added the condition that "at least 2 'g' are needed to be detected", otherwise it would be detected.

More complex example can be found in test/Test.java