public class NormalDataFrame extends DataFrame {
    public NormalDataFrame(String[] a, String[] b, String[] c, String[] d, String columnA, String columnB, String columnC, String columnD) {
        super(new String[][]{a, b, c, d}, new String[]{columnA, columnB, columnC, columnD});
    }

    public NormalDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public NormalDataFrame(NormalDataFrame nndf) {
        super(nndf);
    }

    public static NormalDataFrame build(String[] messages) {
        String[] texts = new String[messages.length];
        String[] dates = new String[messages.length];
        String[] times = new String[messages.length];
        String[] authors = new String[messages.length];
        for (int index = 0; index < messages.length; index++) {
            // FORMAT: "mm/dd/yy, HH:MM - [Author]: [message]"
            String message = messages[index];
            int comma = message.indexOf(", ");
            String date = message.substring(0, comma);
            String time = message.substring(comma + 2, message.indexOf(" - "));
            int secondColon = Util.find2nd(message, ":");
            String author;
            String text;
            if (secondColon > -1) {
                author = Util.removeUnwantedChars(message.substring(message.indexOf(" - ") + 3, secondColon));
                text = Util.removeUnwantedChars(message.substring(secondColon + 1));
            } else {
                author = "WhatsApp";
                text = message.substring(message.indexOf(" - ") + 3);
            }
            if (author.contains("created group") || author.contains("changed the")) {
                text = author;
                author = "WhatsApp";
            }
            texts[index] = text;
            times[index] = time;
            dates[index] = date;
            authors[index] = author;
        }
        return new NormalDataFrame(dates, times, authors, texts, "date", "time", "author", "message");
    }
}
