# Blocks DF ------------------------------------------------------------------------------------------------------------
import pandas as pd
from timeit import default_timer as timer

import NormalDataFrame
import TimeDataFrame
import util


def build(df, run):
    if not run:
        util.append_time(0)
        return None
    temp = timer()
    starts = []
    lengths = []
    chars = []
    authors = []
    block_index = 0
    index = 0

    while index < len(df) - 1:
        date, time, author, text = NormalDataFrame.row(df, index)
        a = author
        h = time.split(':')[0]
        starts.append(index)
        lengths.append(0)
        chars.append(0)
        authors.append(a)
        while author == a and h == time.split(':')[0] and index < len(df) - 1:
            index += 1
            lengths[block_index] += 1
            chars[block_index] += len(text)
            date, time, author, text = NormalDataFrame.row(df, index)
        block_index += 1

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(starts, lengths, chars, authors)), columns=['starting-index', 'length', 'chars', 'author'])


def row(bdf, i):
    return (bdf.at[i, "starting-index"],
            bdf.at[i, "length"],
            bdf.at[i, "chars"],
            bdf.at[i, "author"])


def avg_length(bdf):
    return sum(bdf['chars']) / len(bdf)


def per_month(this, time_df):
    indexes = []
    lengths = []
    charss = []
    counts = []
    index = -1
    for i in list(this.index):
        start, length, chars, author = row(this, i)
        minute, hour, day, month, year, weekday, monthly_index, day_id = TimeDataFrame.row(time_df, i)
        previous_minute, previous_hour, previous_day, previous_month, previous_year, previous_weekday, \
        previous_monthly_index, previous_day_id = TimeDataFrame.row(time_df, i - 1)
        if previous_monthly_index < monthly_index or i == list(this.index)[0]:
            indexes.append(monthly_index)
            lengths.append(0)
            charss.append(0)
            counts.append(0)
            index += 1
        lengths[index] += length
        charss[index] += chars
        counts[index] += 1
    lengths = [i / j for i, j in zip(lengths, counts)]
    charss = [i / j for i, j in zip(charss, counts)]
    return pd.DataFrame(list(zip(indexes, charss, lengths)),
                        columns=['month', 'avg-char-count', 'avg-length'])


def avgs(bpmdf):
    return sum(bpmdf['avg-char-count']) / len(bpmdf)
