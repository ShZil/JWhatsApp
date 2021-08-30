public class Start {
    static String colorSettings;
    static String colorNames;
    static String colorRender;
    static String cgenerate;

    static {
        colorNames = Color.BLUE;
        colorSettings = Color.PURPLE;
        colorRender = Color.CYAN;
        cgenerate = Color.DARKCYAN;
    }

    public static void prints(String[] names, String[] render_names, String[] generate_names) {
        Util.print(
                colorNames +
                        Color.UNDERLINE +
                        "Chats" +
                        Color.END +
                        colorNames +
                        ": " +
                        String.join(", ", names) +
                        Color.END
        );
        Util.print(
                colorRender +
                        Color.UNDERLINE +
                        "Will Render" +
                        Color.END +
                        colorRender +
                        ": " +
                        Color.BOLD +
                        (render_names == Util.ALL ?
                                "All" :
                                (Color.BOLD +
                                        (render_names.length == 0 ?
                                                "None" :
                                                String.join(", ", render_names)
                                        )
                                ) + Color.END
                        )
        );
        Util.print(cgenerate +
                Color.UNDERLINE +
                "Will Generate" +
                Color.END +
                cgenerate +
                ": " +
                (Color.BOLD +
                        (generate_names == Util.ALL ?
                                "All" :
                                Color.BOLD +
                                        (generate_names.length == 0 ?
                                                "None" :
                                                String.join(", ", render_names)
                                        )
                        ) + Color.END
                )
        );

        Util.warnUser(render_names, names);
    }

    public static void settings(boolean renderFinal, boolean optimize) {
        Util.print(colorSettings + Color.BOLD + "Starting with settings:" + Color.END);
        Util.print("    " + colorSettings + Color.UNDERLINE + "render mutual graph(s)" + Color.END +
                colorSettings + ": " + renderFinal);
        Util.print("    " + colorSettings + Color.UNDERLINE + "optimize" + Color.END +
                colorSettings + " (don't generate time-consuming data): " + optimize);
    }

    public static void uncolor() {
        colorNames = "";
        colorSettings = "";
        colorRender = "";
        cgenerate = "";
    }
}
