import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        File[] paths = Util.getChats();
        String[] names = Util.getNames(paths);
        Settings settings = Util.parseSettings();
        if (!settings.colored) {
            Color.uncolor();
            Start.uncolor();
        }
        // ArrayList<Comparison> relations = new ArrayList<Comparison>();

        Start.settings(settings.renderFinal, settings.optimize);
        Start.prints(names, settings.renderNames, settings.generateNames);
        

        for (int i = 0; i < paths.length; i++) {
            File path = paths[i];
            String name = names[i];

            NormalDataFrame ndf = null;
            AlphabetsDataFrame adf = null;
            BlocksDataFrame bdf = null;
            CharacterDataFrame cdf = null;
            IOCDataFrame idf = null;
            MessageTypeDataFrame mtdf = null;
            TimeDataFrame tdf = null;
            WordsDataFrame wdf = null;
            if (Util.contains(settings.generateNames, name) || Arrays.equals(settings.generateNames, Util.ALL)) {
                Util.print("Generating chat with " + Color.UNDERLINE + name + Color.END + ". " +
                        Color.RED + Util.progress(name, names, "[- ]") + Color.END + Color.GREEN);
                Util.print("[         ]", "\r");
                String[] messages = Files.toMessages(Util.read(path));
                Util.print("[█        ]", "\r");
                ndf = NormalDataFrame.build(messages);
                Files.saveDF(ndf, name);
                Util.print("[██       ]", "\r");
                adf = AlphabetsDataFrame.build(ndf, true);
                Util.print("[███      ]", "\r");
                bdf = BlocksDataFrame.build(ndf, true);
                Util.print("[████     ]", "\r");
                cdf = CharacterDataFrame.build(ndf, !settings.optimize);
                Util.print("[█████    ]", "\r");
                idf = IOCDataFrame.build(ndf, true);
                Util.print("[██████   ]", "\r");
                mtdf = MessageTypeDataFrame.build(ndf, true);
                Util.print("[███████  ]", "\r");
                tdf = TimeDataFrame.build(ndf, true);
                Util.print("[████████ ]", "\r");
                wdf = WordsDataFrame.build(ndf, mtdf, !settings.optimize);
                Util.print("[█████████]" + Color.END);
            } else {
                Util.print(Color.END + "Loading chat with " + Color.UNDERLINE + name + Color.END + ". " +
                        Color.RED + Util.progress(name, names, "[- ]") + Color.END + Color.GREEN);
                ndf = Files.getDF(name);
//                DataFrame[] dfs = Files.getDFs(name);
//                adf = dfs[0];
//                tdf = dfs[1];
//                cdf = dfs[2];
//                mtdf = dfs[3];
//                wdf = dfs[4];
//                idf = dfs[5];
//                bdf = dfs[6];
//                if (ndf == null || tdf == null) {
//                    continue;
//                }
            }

            // relations[filled] = Util.findAVGs(ndf, idf, bdf, tdf);
            // System.out.println(Arrays.deepToString(relations));
            // filled++;

            Files.saveDFs(adf, tdf, cdf, mtdf, wdf, idf, bdf, name);
        }
    }
}
