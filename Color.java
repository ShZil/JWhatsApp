public class Color {
    public static String PURPLE = "\033[95m";
    public static String CYAN = "\033[96m";
    public static String DARKCYAN = "\033[36m";
    public static String BLUE = "\033[94m";
    public static String GREEN = "\033[92m";
    public static String YELLOW = "\033[93m";
    public static String RED = "\033[91m";
    public static String BOLD = "\033[1m";
    public static String UNDERLINE = "\033[4m";
    public static String END = "\033[0m";
    
    public static void uncolor() {
        PURPLE = "";
        CYAN = "";
        DARKCYAN = "";
        BLUE = "";
        GREEN = "";
        YELLOW = "";
        RED = "";
        BOLD = "";
        UNDERLINE = "";
        END = "";
    }
}
