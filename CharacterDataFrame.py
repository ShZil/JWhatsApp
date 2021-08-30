# Character DF ---------------------------------------------------------------------------------------------------------
import pandas as pd
from timeit import default_timer as timer

import Alphabets
import NormalDataFrame
import util


def build(df, run):
    if not run:
        util.append_time(0)
        return None
    temp = timer()
    all = True
    if all:
        chars = []
        rarities = []

        for i in list(df.index):
            date, time, author, text = NormalDataFrame.row(df, i)
            if util.is_nan(text):
                continue
            for c in text:
                if c not in chars:
                    chars.append(c)
                    rarities.append(0)
            for letter in text:
                rarities[chars.index(letter)] += text.count(letter)
        util.append_time(timer() - temp)
        return pd.DataFrame(list(zip(chars, rarities)), columns=['letter', 'rarity']).sort_values(by="rarity", ascending=False)
    else:
        chars = Alphabets.characters()
        rarities = []
        for _ in chars:
            rarities.append(0)
        for i in range(len(df)):
            date, time, author, text = NormalDataFrame.row(df, i)
            if util.is_nan(text):
                continue
            for letter in chars:
                rarities[chars.index(letter)] += text.count(letter)

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(chars, rarities)), columns=['letter', 'rarity'])


def row(cdf, i):
    return (cdf.at[i, "letter"],
            cdf.at[i, "rarity"])
