# Flexible Word Matching
 Find words in text where common detection bypass are used such as '*' or word splitting using spaces or even some word alteration

# Basic example

```Java
import com.salwyrr.detection.FlexibleWordMatching;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        // Add words to detect
        FlexibleWordMatching.addWord("Salwyrr");
        FlexibleWordMatching.addWord("best");

        // Source text, here a user message
        String userMessage = "I love s al Wyyy r, that is the b*st thing ever";

        // Compute and display matches
        ArrayList<String> matches = FlexibleWordMatching.searchMatchesInText(userMessage);

        for (String match : matches) {
            System.out.println(match);
        }
    }

}
```

### Output

```
salwyr
best
```