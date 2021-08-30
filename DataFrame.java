import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataFrame {
    String index = "index";
    int len; // Column Count
    int length; // Row Count
    int[] indexing;
    String[] columns;
    String[][] data;
    /*
    * [
    *   column1:[...],
    *   column2:[...],
    *   column3:[...],
    *   column4:[...]
    * ]
    * len = 4, length = ...
    * */

    public DataFrame(String[][] data, String[] columns) {
        len = data.length;
        length = data[0].length;
        indexing = new int[length];
        fill(indexing);
        if (len == columns.length) {
            this.columns = columns;
        } else {
            throw new IllegalArgumentException("`Columns` are not the same length as `Data`.");
        }
        this.data = data;
    }

    public DataFrame(DataFrame df) {
        this(df.data, df.columns);
    }

    private void fill(int[] indexing) {
        for (int i = 0; i < indexing.length; i++) {
            indexing[i] = i;
        }
    }

    public static NormalDataFrame readCSV(String path) throws FileNotFoundException {
        // Doesn't account for string literals which contain "/'/,/\n.
        File f = new File(path);
        if (f.exists()) {
            final String SEP = ",";
            String[] read = Util.read(f);
            String[] cols = read[0].split(SEP);
            String index = cols[0];
            String[] indexing = new String[read.length - 1];
            String[][] data = new String[cols.length - 1][read.length - 1];
            int[] castedIndexing = new int[indexing.length];
            for (int i = 0; i < indexing.length; i++) {
                String sindex = indexing[i];
                castedIndexing[i] = Integer.parseInt(sindex);
            }
            for (int i = 1; i < read.length; i++) {
                String line = read[i];
                String[] cells = line.split(SEP);
                indexing[i - 1] = cells[0];
                for (int j = 1; j < cells.length; j++) {
                    data[j - 1][i - 1] = cells[j];
                }
            }
            String[] columns = Util.subarray(cols, 1);
            NormalDataFrame ndf = new NormalDataFrame(data, columns);
            ndf.index = index;
            ndf.indexing = castedIndexing;
            return ndf;
        } else {
            throw new FileNotFoundException();
        }
    }

    public void index(String index) {
        this.index = index;
    }

    public void toCSV(String path) {
        final String SEP = ",";
        final String LINE = "\n";
        final String PROTECT = "\"";
        StringBuilder sb = new StringBuilder();

        sb.append(index);
        for (String column : columns) {
            sb.append(SEP);
            sb.append(column);
        }
        sb.append(LINE);
        for (int i = 0; i < data[0].length; i++) {
            int index = indexing[i];
            sb.append(index);
            for (int j = 0; j < len; j++) {
                String cell = data[j][i];
                if (cell.equals("\"")) cell = "\"\"\"\"";
                if (cell.contains(SEP) || cell.contains(LINE)) {
                    sb.append(SEP);
                    sb.append(PROTECT);
                    sb.append(cell);
                    sb.append(PROTECT);
                } else {
                    sb.append(SEP);
                    sb.append(cell);
                }
            }
            sb.append(LINE);
        }
        try {
            File f = new File(path);
            f.createNewFile();
            FileWriter write = new FileWriter(f);
            write.write(sb.toString());
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        final String SEP = ",";
        final String LINE = "\n";
        StringBuilder sb = new StringBuilder();

        sb.append(index);
        for (String column : columns) {
            sb.append(SEP);
            sb.append(column);
        }
        sb.append(LINE);
        for (int i = 0; i < Math.min(data[0].length, 101); i++) {
            int index = indexing[i];
            sb.append(index);
            for (int j = 0; j < len; j++) {
                String cell = data[j][i];
                sb.append(SEP);
                sb.append(Util.limit(cell, 15));
            }
            sb.append(LINE);
        }
        return sb.toString();
    }

    public Dimension size() {
        return new Dimension(len, length);
    }

    public String[] row(int i) {
        if (i > length || i < 0) throw new IndexOutOfBoundsException("Requested non-existing row.");
        String[] row = new String[len];
        for (int j = 0; j < len; j++) {
            row[j] = data[j][i];
        }
        return row;
    }

    protected void sortValues(String column) {
        quickSort(data, Arrays.asList(columns).indexOf(column), 0, data[0].length - 1);
    }

    protected void reverse() {
        for (int i = 0; i < len; i++) {
            List<String> column = Arrays.asList(data[i]);
            Collections.reverse(column);
            data[i] = column.toArray(new String[0]);
        }
    }

    private static void swap(String[][] arr, int j, int k) {
        for (int i = 0; i < arr.length; i++) {
            String temp = arr[i][j];
            arr[i][j] = arr[i][k];
            arr[i][k] = temp;
        }
    }

    private static int partition(String[][] arr, int column, int low, int high) {
        int pivot = Integer.parseInt(arr[column][high]);
        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {
            if (Integer.parseInt(arr[column][j]) < pivot)
            {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    private static void quickSort(String[][] arr, int column, int low, int high) {
        if (low < high)
        {
            int pi = partition(arr, column, low, high);

            quickSort(arr, column, low, pi - 1);
            quickSort(arr, column,pi + 1, high);
        }
    }

    public <T extends DataFrame> T select(String[] columns) {
        String[][] copied = new String[columns.length][length];
        Set<String> cols = new HashSet<>(Arrays.asList(this.columns));
        for (int i = 0; i < columns.length; i++) {
            if (cols.contains(columns[i])) {
                copied[i] = this.data[Arrays.asList(this.columns).indexOf(columns[i])];
            }
        }
        return (T) new DataFrame(copied, columns);
    }

    public <T extends DataFrame> T select(String column) {
        return select(new String[]{column});
    }

    protected String[] toArray() {
        if (len == 1) {
            return data[0];
        }
        return null;
    }

    protected DataFrame filter(String column, Function<String, Boolean> condition) {
        ArrayList<Integer> savedIndexes = new ArrayList<>();
        int col = Arrays.asList(columns).indexOf(column);
        for (int i : this.indexing) {
            if (condition.apply(data[col][i])) {
                savedIndexes.add(i);
            }
        }
        String[][] newData = new String[len][];
        for (int j = 0; j < len; j++) {
            ArrayList<String> prevData = new ArrayList<>(Arrays.asList(data[j]));
            newData[j] = IntStream.range(0, prevData.size())
                    .filter(savedIndexes::contains)
                    .mapToObj(prevData::get)
                    .collect(Collectors.toList())
                    .toArray(String[]::new);
        }
        String[] indexes = Util.toStringArray(savedIndexes);
        String[] cols = Util.concat("index", this.columns);
        String[][] newDat = Util.concat(indexes, newData);
        return new DataFrame(newDat, cols);
    }

    protected void filter(String[] indices) {
        Set<String> set = new HashSet<>(Arrays.asList(indices));
        for (int i = indexing.length - 1; i >= 0; i--) {
            if (set.contains(String.valueOf(i))) {
                drop(i);
            }
        }
        length -= indices.length;
        indexing = Arrays.copyOf(indexing, length);
    }

    private void drop(int index) {
        for (int col = 0; col < len; col++) {
            String[] column = data[col];
            ArrayList<String> columnList = new ArrayList<>(Arrays.asList(column));
            columnList.remove(index);
            data[col] = columnList.toArray(new String[0]);
        }
    }

    private void drop(int[] indexes) {
        for (int index : indexes) {
            drop(index);
        }
        length -= indexes.length;
        indexing = Arrays.copyOf(indexing, length);
    }
}
