import pandas as pd

import AlphabetsDataFrame
import BlocksDataFrame
import CharacterDataFrame
import IOCDataFrame
import MessageTypeDataFrame
import TimeDataFrame
import NormalDataFrame
import WordsDataFrame

import start
import util
import warnings
import color
import render
import files


# To do: Reply time for messages, reply patterns (phrase matching).
# Consider word distribution per author (across chats), not one chat.
# Then clean the code,
# And to github you shall upload
# Length in months of chat
# Most common letters by alphabet (i.e. most common emojis)
# Whom do I mostly talk to? - Comparison of talking hours (00-24), weekdays (1-7), months (0-inf)


# Main -----------------------------------------------------------------------------------------------------------------

def main():
    warnings.filterwarnings("ignore", category=RuntimeWarning)
    paths, names = util.get_chats()
    render_names, generate_names, render_final, optimize, colored = util.parse_settings()
    if not colored:
        color.uncolor()
        start.uncolor()
    relations = []

    start.settings(render_final, optimize)
    start.prints(names, render_names, generate_names)

    for path, name in zip(paths, names):
        if name in generate_names or generate_names == ["*"]:
            print("Generating chat with " + color.UNDERLINE + name + color.END + ". " +
                  color.RED + util.progress(name, names, "[- ]") + color.END + color.GREEN)
            util.append_time(name)
            print("[         ]", end="\r")
            messages = files.to_messages(path)
            print("[█        ]", end="\r")
            ndf = NormalDataFrame.build(messages)
            files.save_df(ndf, name)
            print("[██       ]", end="\r")
            adf = AlphabetsDataFrame.build(ndf, True)
            print("[███      ]", end="\r")
            bdf = BlocksDataFrame.build(ndf, True)
            print("[████     ]", end="\r")
            cdf = CharacterDataFrame.build(ndf, not optimize)
            print("[█████    ]", end="\r")
            idf = IOCDataFrame.build(ndf, True)
            print("[██████   ]", end="\r")
            mtdf = MessageTypeDataFrame.build(ndf, True)
            print("[███████  ]", end="\r")
            tdf = TimeDataFrame.build(ndf, True)
            print("[████████ ]", end="\r")
            wdf = WordsDataFrame.build(ndf, mtdf, not optimize)
            print("[█████████]" + color.END)
        else:
            print(color.END + "Loading chat with " + color.UNDERLINE + name + color.END + ". " +
                  color.RED + util.progress(name, names, "[- ]") + color.END + color.GREEN)
            ndf = files.get_df(name)
            adf, tdf, cdf, mtdf, wdf, idf, bdf = files.get_dfs(name)
            if ndf is None or tdf is None:
                continue

        relations.append(util.find_avgs(ndf, idf, bdf, tdf))

        files.save_dfs(adf, tdf, cdf, mtdf, wdf, idf, bdf, name)
        if name in render_names or render_names == ["*"]:
            n = names if render_names == ["*"] else render_names
            render.renders(name, n, ndf, adf, tdf, cdf, mtdf, wdf, idf, bdf)
    if render_final:
        render.render_relations(names, relations)
    if generate_names:
        times = pd.DataFrame(util.get_times(), columns=['Name', 'Messages', 'NDF', 'ADF', 'BDF',
                                                        'CDF', 'IDF', 'MTDF', 'TDF', 'WDF'])
        render.display_times(times)
        print(times)


if __name__ == '__main__':
    main()
