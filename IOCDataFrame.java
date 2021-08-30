import java.util.ArrayList;

public class IOCDataFrame extends DataFrame {
    public IOCDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public IOCDataFrame(String[] data, String column) {
        this(new String[][]{data}, new String[]{column});
    }

    public static IOCDataFrame build(NormalDataFrame ndf, boolean run) {
        if (!run) return null;
        ArrayList<Double> iocs = new ArrayList<>();
        for (int i : ndf.indexing) {
            String text = ndf.row(i)[3];
            iocs.add(Util.ioc(text));
        }
        return new IOCDataFrame(new String[][]{Util.toStringArray(iocs), ndf.select("message").toArray()},
                new String[]{"ioc", "message"});
    }
}
