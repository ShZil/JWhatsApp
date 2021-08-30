import java.util.ArrayList;

public class MessageType {
    private static String writer = "";
    private String type;
    private String author;
    private String arg;
    private String middle;
    private boolean addArg;

    public MessageType(String type, String author, String arg) {
        this.type = type;
        if (author.equals("")) this.author = writer;
        else this.author = author;
        this.arg = arg;
    }

    public MessageType(String type, String middle, boolean addArg) {
        this.type = type;
        this.middle = middle;
        this.addArg = addArg;
    }

    public MessageType(String type, String author) {
        this(type, author, "");
    }

    public MessageType(String type) {
        this(type, "", "");
    }

    public static void setWriter(String author) {
        writer = author;
    }

    public void add(ArrayList<String> types, ArrayList<String> authors, ArrayList<String> args) {
        types.add(type);
        authors.add(author);
        args.add(arg);
    }

    public void add(ArrayList<String> types, ArrayList<String> authors, ArrayList<String> args, String text) {
        int premid = text.indexOf(middle);
        if (premid == -1) throw new StringIndexOutOfBoundsException(middle + " not found in " + text);
        int postmid = premid + middle.length();

        this.author = text.substring(0, premid);
        this.arg = addArg ? text.substring(postmid) : "";
        add(types, authors, args);
    }
}
