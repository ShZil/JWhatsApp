import java.util.Set;

public class Alphabets {
    private static final Set<Character> english = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
    private static final Set<Character> hebrew = Set.of('א', 'ב', 'ג', 'ד', 'ה', 'ו', 'ז', 'ח', 'ט', 'י', 'כ', 'ל', 'מ',
            'נ', 'ס', 'ע', 'פ', 'צ', 'ק', 'ר', 'ש', 'ת', 'ך', 'ן', 'ף', 'ץ', 'ם');
    private static final Set<Character> punctuation = Set.of('+', ',', '.', '-', '\"', '&', '!', '?', ':', ';', '#', '~',
            '=', '\\', '/', '$', '@', '^', '(', ')', '_', '<', '>', '[', ']', '\'', '`', '*', '|');
    private static final Set<Character> math = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    private static final Character space = ' ';

    public static int countLetters(String text) {
        return countFromAlphabet(text, english);
    }

    public static int countHebrew(String text) {
        return countFromAlphabet(text, hebrew);
    }

    public static int countPunctuation(String text) {
        return countFromAlphabet(text, punctuation);
    }

    public static int countSpaces(String text) {
        return countFromAlphabet(text, space);
    }

    public static int countMath(String text) {
        return countFromAlphabet(text, math);
    }

    private static int countFromAlphabet(String text, Set<Character> alphabet) {
        int count = 0;
        for (char ch : text.toCharArray()) {
            if (alphabet.contains(ch)) {
                count++;
            }
        }
        return count;
    }

    private static int countFromAlphabet(String text, Character alphabet) {
        int count = 0;
        for (char ch : text.toCharArray()) {
            if (alphabet == ch) {
                count++;
            }
        }
        return count;
    }
}
