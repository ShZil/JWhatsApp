import java.util.ArrayList;

public class WordsDataFrame extends DataFrame {
    public WordsDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public static WordsDataFrame build(NormalDataFrame nndf, MessageTypeDataFrame mtdf, boolean run) {
        if (!run) {
            return null;
        }
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Integer> rarities = new ArrayList<>();
        NormalDataFrame ndf = new NormalDataFrame(nndf);
        ndf.filter(mtdf.select("index").toArray());

        for (int i : ndf.indexing) {
            String text = ndf.row(i)[3];
            for (String word : text.split(" ")) {
                if (word.equals("")) // This will occur if 'text' has internal adjacent double whitespaces.
                    continue;
                if (!words.contains(word)) {
                    words.add(word);
                    rarities.add(0);
                }
                int index = words.indexOf(word);
                rarities.set(index, rarities.get(index) + Util.count(text, word));
            }
        }
        WordsDataFrame toReturn = new WordsDataFrame(new String[][] {Util.toStringArray(words), Util.toStringArray(rarities)},
                new String[]{"word", "rarity"});
        toReturn.sortValues("rarity");
        toReturn.reverse();
        return toReturn;
    }
}
