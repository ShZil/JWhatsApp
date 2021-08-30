import java.util.ArrayList;

public class CharacterDataFrame extends DataFrame {
    public CharacterDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public static CharacterDataFrame build(NormalDataFrame ndf, boolean run) {
        if (!run) {
            return null;
        }
        ArrayList<Character> chars = new ArrayList<>();
        ArrayList<Integer> rarities = new ArrayList<>();

        for (int i : ndf.indexing) {
            String[] row = ndf.row(i);
            String text = row[3];
            for (char c : text.toCharArray()) {
                if (!chars.contains(c)) {
                    chars.add(c);
                    rarities.add(0);
                }
                int index = chars.indexOf(c);
                rarities.set(index, rarities.get(index) + Util.count(text, c));
            }
        }
        CharacterDataFrame toReturn = new CharacterDataFrame(new String[][]{Util.toStringArray(chars), Util.toStringArray(rarities)}, new String[]{"letter", "rarity"});
        toReturn.sortValues("rarity");
        toReturn.reverse();
        return toReturn;
    }
}
