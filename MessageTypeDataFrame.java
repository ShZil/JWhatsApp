import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageTypeDataFrame extends DataFrame {
    private static final MessageType whatsappInfo = new MessageType("whatsapp-info", "WhatsApp");
    private static final Map<String, MessageType> simple = Stream.of(new Object[][] {
            { "Messages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them. Tap to learn more.", whatsappInfo },
            { "This chat is with a business account. Tap to learn more.", whatsappInfo },
            { "Your security code with", whatsappInfo },
            { "This message was deleted", new MessageType("deleted") },
            { "<Media omitted>", new MessageType("media") },
            { "You unblocked this contact.", new MessageType("unblock", "Self") },
            { "You blocked this contact. Tap to unblock.", new MessageType("block", "Self") }
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (MessageType) data[1]));
//    static {
//        simple.put("Messages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them. Tap to learn more.", whatsappInfo);
//        simple.put("This chat is with a business account. Tap to learn more.", whatsappInfo);
//        simple.put("Your security code with", whatsappInfo);
//        simple.put("This message was deleted", new MessageType("deleted"));
//        simple.put("<Media omitted>", new MessageType("media"));
//        simple.put("You unblocked this contact.", new MessageType("unblock", "Self"));
//        simple.put("You blocked this contact. Tap to unblock.", new MessageType("block", "Self"));
//    }

    private static final Map<String, MessageType> contained = Stream.of(new Object[][] {
            { " created group", new MessageType("group-created", " created group", true) },
            { " was added", new MessageType("participant-join", " was added", true) },
            { " added", new MessageType("participant-join", " added", true) },
            { " changed the subject", new MessageType("title-change", " changed the subject from", true) },
            { " joined using this group's invite link", new MessageType("participant-join", " joined using this group's invite link", false) },
            { " changed the group description", new MessageType("description-change", "changed the group description", false) },
            { " changed this group's settings", new MessageType("settings-change", " changed this group's settings", false) },
            { "changed this group's icon", new MessageType("icon-change", " changed this group's icon", false) },
            { "You're now an admin", new MessageType("admin", "You", false) }
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (MessageType) data[1]));
//    static {
//        contained.put(" created group", new MessageType("group-created", " created group", true));
//        contained.put(" was added", new MessageType("participant-join", " was added", true));
//        contained.put(" added", new MessageType("participant-join", " added", true));
//        contained.put(" changed the subject", new MessageType("title-change", " changed the subject from", true));
//
//        contained.put(" joined using this group's invite link", new MessageType("participant-join", " joined using this group's invite link", false));
//        contained.put(" changed the group description", new MessageType("description-change", "changed the group description", false));
//        contained.put(" changed this group's settings", new MessageType("settings-change", " changed this group's settings", false));
//        contained.put("changed this group's icon", new MessageType("icon-change", " changed this group's icon", false));
//        contained.put("You're now an admin", new MessageType("admin", "You", false));
//    }

    private static final Map<String, MessageType> whatsappSpecials = Stream.of(new Object[][] {
            { "left", new MessageType("participant-leave", " left", false) }
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (MessageType) data[1]));
//    static {
//        whatsappSpecials.put("left", new MessageType("participant-leave", " left", false));
//    }

    private static final MessageType whatsapp = new MessageType("whatsapp-info", "WhatsApp");

    public MessageTypeDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public MessageTypeDataFrame(DataFrame df) {
        super(df);
    }

    public static MessageTypeDataFrame build(NormalDataFrame ndf, boolean run) {
        if (!run) return null;
        ArrayList<String> types = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> args = new ArrayList<>();

        for (int i : ndf.indexing) {
            String[] row = ndf.row(i);
            String author = row[2], text = row[3];
            MessageType.setWriter(author);
            if (text == null || text.equals("")) {
                types.add("nan");
                authors.add(author);
                args.add("");
                continue;
            }

            if (simple.containsKey(text)) {
                simple.get(text).add(types, authors, args);
            } else if (Util.hasMapKey(contained, text)) {
                contained.get(Util.getMapKeyContained(contained, text)).add(types, authors, args, text);
            } else if (author.equals("WhatsApp")) {
                if (Util.hasMapKey(whatsappSpecials, text)) {
                    whatsappSpecials.get(Util.getMapKeyContained(whatsappSpecials, text)).add(types, authors, args, text);
                } else {
                    whatsapp.add(types, authors, args);
                }
            } else {
                types.add("text");
                authors.add(author);
                args.add("");
            }
        }
        return new MessageTypeDataFrame(new MessageTypeDataFrame(
                new String[][]{
                        types.toArray(new String[0]),
                        authors.toArray(new String[0]),
                        args.toArray(new String[0]),
                        ndf.select("message").toArray()
                }, new String[]{"type", "author", "arg", "__message"})
                .filter("type", MessageTypeDataFrame::nontext));
    }

    public static boolean nontext(String arg) {
        return !arg.equals("text");
    }
}
