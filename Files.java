import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Files {
    public static String[] toMessages(String[] lines) {
        ArrayList<String> messages = new ArrayList<>();
        int i = -1;
        for (String line : lines) {
            if (Util.isDate(line)) {
                i += 1;
                // first line in message
                messages.add(line);
            } else {
                // non-initial line in message
                messages.set(i, messages.get(i) + "\n" + line);
            }
        }
        return messages.toArray(new String[0]);
    }

    public static void saveDF(NormalDataFrame df, String name) {
        folder("saved/" + name);
        df.index("index");
        df.toCSV("saved/" + name + "/chat.csv");
    }

    public static NormalDataFrame getDF(String name) {
        try {
            return DataFrame.readCSV("saved/" + name + "/chat.csv");
        }
        catch (FileNotFoundException e) {
            Util.print("`saved/" + name + "/chat.csv` not found. Try to generate the Database again.");
            return null;
        }
    }

    private static void folder(String name) {
        File dir = new File(name);
        if (!dir.exists()) dir.mkdirs();
    }

    public static void saveDFs(AlphabetsDataFrame adf, TimeDataFrame tdf, CharacterDataFrame cdf, MessageTypeDataFrame mtdf, WordsDataFrame wdf, IOCDataFrame idf, BlocksDataFrame bdf, String name) {
        folder("saved/" + name);
        if (adf != null) adf.index("index");
        if (tdf != null) tdf.index("index");
        if (cdf != null) cdf.index("char");
        if (mtdf != null) mtdf.index("index");
        if (wdf != null) wdf.index("index");
        if (idf != null) idf.index("index");
        if (bdf != null) bdf.index("index");

        if (adf != null) adf.toCSV("saved/" + name + "/chat_alphabets.csv");
        if (tdf != null) tdf.toCSV("saved/" + name + "/chat_time.csv");
        if (cdf != null) cdf.toCSV("saved/" + name + "/chat_characters.csv");
        if (mtdf != null) mtdf.toCSV("saved/" + name + "/chat_message_types.csv");
        if (wdf != null) wdf.toCSV("saved/" + name + "/chat_words.csv");
        if (idf != null) idf.toCSV("saved/" + name + "/chat_ioc.csv");
        if (bdf != null) bdf.toCSV("saved/" + name + "/chat_blocks.csv");
    }
}
