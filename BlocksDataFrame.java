import java.util.ArrayList;

public class BlocksDataFrame extends DataFrame {
    public BlocksDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public static BlocksDataFrame build(NormalDataFrame ndf, boolean run) {
        if (!run) return null;
        ArrayList<Integer> starts = new ArrayList<>();
        ArrayList<Integer> lengths = new ArrayList<>();
        ArrayList<Integer> chars = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        int block_index = 0;
        int index = 0;

        while (index < ndf.length - 1) {
            String[] row = ndf.row(index);
            String a = row[2], text = row[3];
            String author = a;
            starts.add(index);
            lengths.add(0);
            chars.add(0);
            authors.add(a);
            while (author.equals(a) && index < ndf.length - 1) {
                index++;
                lengths.set(block_index, lengths.get(block_index) + 1);
                chars.set(block_index, chars.get(block_index) + text.length());
                row = ndf.row(index);
                author = row[2]; text = row[3];
            }
            block_index++;
        }

        return new BlocksDataFrame(
                new String[][]{
                        Util.toStringArray(starts),
                        Util.toStringArray(lengths),
                        Util.toStringArray(chars),
                        Util.toStringArray(authors)
                }, new String[]{"starting-index", "length", "chars", "author"});
    }
}
