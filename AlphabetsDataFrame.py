# Alphabets DF ---------------------------------------------------------------------------------------------------------
import pandas as pd
from timeit import default_timer as timer

import Alphabets
import NormalDataFrame
import TimeDataFrame
import util


def build(df, run):
    if not run:
        util.append_time(0)
        return None
    temp = timer()
    lengths = []
    englishes = []
    hebrews = []
    emojis = []
    punctuation = []
    spaces = []
    maths = []
    for i in list(df.index):
        date, time, author, text = NormalDataFrame.row(df, i)
        if util.is_nan(text):
            length = 0
            english = 0
            hebrew = 0
            emoji = 0
            pun = 0
            math = 0
            space = 0
        else:
            length = len(text)
            english = Alphabets.count_letters(text)
            hebrew = Alphabets.count_hebrew(text)
            pun = Alphabets.count_punctuation(text)
            space = Alphabets.count_spaces(text)
            math = Alphabets.count_math(text)
            emoji = length - english - hebrew - pun - math - space
        englishes.append(english)
        hebrews.append(hebrew)
        lengths.append(length)
        punctuation.append(pun)
        maths.append(math)
        spaces.append(space)
        emojis.append(emoji)

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(lengths, englishes, hebrews, punctuation, maths, emojis, spaces)),
                        columns=['length', 'english-letters', 'hebrew-letters', 'punctuation', 'math', 'emojis',
                                 'whitespace'])


def row(adf, i):
    return adf.at[i, "length"],\
           adf.at[i, "english-letters"],\
           adf.at[i, "hebrew-letters"],\
           adf.at[i, "punctuation"],\
           adf.at[i, "math"],\
           adf.at[i, "emojis"],\
           adf.at[i, "whitespace"]


def per_month(this, time_df):
    merged = pd.merge(this, time_df, left_index=True, right_index=True)
    merged = merged[['length', 'english-letters', 'hebrew-letters', 'punctuation', 'math', 'emojis', 'whitespace', 'monthly-index']]
    merged = merged.groupby("monthly-index").mean()
    merged['month'] = merged.index
    return merged
