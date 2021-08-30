# Words DF -------------------------------------------------------------------------------------------------------------
import pandas as pd
from timeit import default_timer as timer

import NormalDataFrame
import util


def build(df, mtdf, run):
    if not run:
        util.append_time(0)
        return None
    temp = timer()
    df = df.drop(mtdf['ind'])
    words = []
    rarities = []

    for i in list(df.index):
        date, time, author, text = NormalDataFrame.row(df, i)
        if util.is_nan(text):
            continue
        for word in text.split():
            if word not in words:
                words.append(word)
                rarities.append(0)
        for word in text.split():
            rarities[words.index(word)] += text.count(word)

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(words, rarities)), columns=['word', 'rarity']).sort_values(by="rarity",
                                                                                            ascending=False)


def row(wdf, i):
    return (wdf.at[i, "word"],
            wdf.at[i, "rarity"])
