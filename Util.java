import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Util {
    public static final String[] ALL = new String[]{"*"};
    private static final String chatPrefix = "WhatsApp Chat with ";
    private static final String chatSuffix = ".txt";
    private static final String sourceFolder = "./raw";
    private static final Set<Character> allowedForDate = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', ':', ',', ' '));

    public static void print(String str) {
        print(str, "");
    }

    public static void print(String str, String end) {
        System.out.println(str + end);
    }

    public static void warnUser(String[] render_names, String[] names) {

    }

    public static File[] getChats() {
        File[] filesList = new File(sourceFolder).listFiles();
        int len = 0;
        assert filesList != null;
        for (File f : filesList) {
            if (f.isFile() && f.getName().endsWith(chatSuffix))
                len++;
        }
        File[] toReturn = new File[len];
        int i = 0;
        for (File f : filesList) {
            if (f.isFile() && f.getName().endsWith(chatSuffix)) {
                toReturn[i] = f;
                i++;
            }
        }
        return toReturn;
    }

    public static String[] getNames(File[] paths) {
        // paths[index].getName() must be of format "WhatsApp Chat with _____.txt",
        // where the underscores can be replaced with any string.
        String[] names = new String[paths.length];
        for (int index = 0; index < names.length; index++) {
            File path = paths[index];
            int endIndex = path.getName().length() - chatSuffix.length();
            try {
                names[index] = path.getName().substring(chatPrefix.length(), endIndex);
            } catch (IndexOutOfBoundsException e) {
                names[index] = "INVALID PATH NAME";
            }
        }
        return names;
    }

    public static Settings parseSettings() {
        // Incorrect, please fix. Translate from python file.
        return new Settings(getNames(getChats()), new String[]{"*"}, true, false, true);
    }

    public static boolean contains(String[] generateNames, String name) {
        for (String value : generateNames) {
            if (value.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static String multiply(char ch, int len) {
        return new String(new char[len]).replace('\u0000', ch);
    }

    public static String progress(String value, String[] array, String shape) {
        // Format:
        // 01111122223
        // Example:
        // [-----    ]
        int index = Util.indexOf(array, value);
        return Util.multiply(' ', Util.GetLongestStringLength(array) - value.length()) +
                shape.charAt(0) +
                Util.multiply(shape.charAt(1), index + 1) +
                Util.multiply(shape.charAt(2), array.length - index - 1) +
                shape.charAt(3);
    }

    private static int GetLongestStringLength(String[] array) {
        int max = -1;
        for (String value : array) {
            if (value.length() > max) {
                max = value.length();
            }
        }
        return max;
    }

    public static int indexOf(Object[] array, String value) {
        for (int index = 0; index < array.length; index++) {
            if (array[index].equals(value)) {
                return index;
            }
        }
        return -1;
    }

    public static <T> T[] subarray(T[] array, int start) {
        Object[] sub = new Object[array.length - start];
        if (array.length - start >= 0) System.arraycopy(array, start, sub, 0, array.length - start);
        else throw new IllegalArgumentException("Array is shorter than start index");
        return (T[])sub;
    }

    public static String[] read(File f) {
        try {
            Scanner scan = new Scanner(f);
            List<String> lines = new ArrayList<>();
            while (scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            return lines.toArray(new String[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public static String readFull(File f) {
        try {
            Scanner scan = new Scanner(f);
            StringBuilder lines = new StringBuilder();
            while (scan.hasNextLine()) {
                lines.append(scan.nextLine());
            }
            return lines.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isDate(String str) {
        if (str.length() < 10) return false;
        str = str.substring(0, 10);
        for (char ch : str.toCharArray()) {
            if (!allowedForDate.contains(ch)) {
                return false;
            }
        }
        return count(str, '/') == 2 && count(str, ',') == 1;
    }

    public static int count(String str, char ch) {
        return (int) str.chars().filter(c -> c == ch).count();
    }

    public static int count(String str, String sub) {
        return str.split(Pattern.quote(sub), -1).length - 1;
    }

    public static String removeUnwantedChars(String str) {
        String trimmed = removeUnwantedChar(removeUnwantedChar(removeUnwantedChar(str, '\"'), '\n'), ' ');
        return trimmed.replaceAll("\u200e", "").replaceAll("\u202b", "").replaceAll("\u202c", "");
    }

    private static String removeUnwantedChar(String str, char ch) {
        return str.replaceAll(ch + "$", "").replaceAll("^" + ch, "");
    }

    public static int find2nd(String str, String substring) {
        return str.indexOf(substring, str.indexOf(substring) + 1);
    }

    public static String limit(String str, int limit) {
        if (str.length() <= limit) return str;
        return str.substring(0, limit - 3) + "...";
    }

    public static <T> String[] toStringArray(ArrayList<T> arrayList, Function<T, String> convert) {
        return arrayList.stream()
                .map(convert)
                .toArray(String[]::new);
    }

    public static <T> String[] toStringArray(ArrayList<T> arrayList) {
        return toStringArray(arrayList, String::valueOf);
    }

    public static double ioc(String str) {
        if (str.length() < 2) {
            return 0;
        }
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        for (char ch : str.toCharArray()) {
            if (!characters.contains(ch)) {
                characters.add(ch);
                counts.add(Util.count(str, ch));
            }
        }
        int ioc = counts.stream().map(o -> o * (o-1)).reduce(0, Integer::sum);
        // double ioc = sum([x * (x-1) for x in dict.values()])
        return ioc / (Math.pow(str.length(), 2) - str.length());
    }

    public static String getMapKeyContained(Map<String, MessageType> map, String text) {
        if (map.containsKey(text)) return text;
        for (String next : map.keySet()) {
            if (text.contains(next)) return next;
        }
        return null;
    }

    public static boolean hasMapKey(Map<String, MessageType> map, String text) {
        if (map.containsKey(text)) return true;
        for (String s : map.keySet()) {
            if (text.contains(s)) return true;
        }
        return false;
    }

    public static String[] concat(String obj, String[] arr) {
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(arr));
        temp.add(0, obj);
        return temp.toArray(new String[0]);
    }

    public static String[][] concat(String[] obj, String[][] arr) {
        ArrayList<String[]> temp = new ArrayList<>(Arrays.asList(arr));
        temp.add(0, obj);
        return temp.toArray(new String[0][0]);
    }

    public static <T> Integer[] toIntArray(ArrayList<T> source, Function<T, Integer> convert) {
        return source.stream()
                .map(convert)
                .toArray(Integer[]::new);
    }

    public static Integer[] toIntArray(ArrayList<String> source) {
        return toIntArray(source, Integer::parseInt);
    }
}
