public class AlphabetsDataFrame extends DataFrame {

    public AlphabetsDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public static AlphabetsDataFrame build(NormalDataFrame ndf, boolean run) {
        if (!run) {
            return null;
        }
        String[] lengths = new String[ndf.length];
        String[] englishes = new String[ndf.length];
        String[] hebrews = new String[ndf.length];
        String[] emojis = new String[ndf.length];
        String[] punctuation = new String[ndf.length];
        String[] spaces = new String[ndf.length];
        String[] maths = new String[ndf.length];
        for (int i : ndf.indexing) {
            String[] row = ndf.row(i);
            String date = row[0], time = row[1], author = row[2], text = row[3];
            int length = 0, english = 0, hebrew = 0, emoji = 0, pun = 0, math = 0, space = 0;
            if (text != null) {
                length = text.length();
                english = Alphabets.countLetters(text);
                hebrew = Alphabets.countHebrew(text);
                pun = Alphabets.countPunctuation(text);
                space = Alphabets.countSpaces(text);
                math = Alphabets.countMath(text);
                emoji = length - english - hebrew - pun - math - space;
            }
            englishes[i] = String.valueOf(english);
            hebrews[i] = String.valueOf(hebrew);
            lengths[i] = String.valueOf(length);
            punctuation[i] = String.valueOf(pun);
            maths[i] = String.valueOf(math);
            spaces[i] = String.valueOf(space);
            emojis[i] = String.valueOf(emoji);
        }
        return new AlphabetsDataFrame(new String[][]{lengths, englishes, hebrews, punctuation, maths, emojis, spaces},
               new String[]{"length", "english-letters", "hebrew-letters", "punctuation", "math", "emojis", "whitespace"});
    }
}
