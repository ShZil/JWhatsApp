# Index of Coincidence DF ----------------------------------------------------------------------------------------------

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
    iocs = []
    for i in list(df.index):
        date, time, author, text = NormalDataFrame.row(df, i)
        if util.is_nan(text):
            iocs.append(0)
        else:
            iocs.append(util.ioc(text))

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(iocs)), columns=['ioc'])


def row(idf, i):
    return idf.at[i, "ioc"]


def per_month(this, time_df):
    merged = pd.merge(this, time_df, left_index=True, right_index=True)
    merged = merged[['ioc', 'monthly-index']]
    merged = merged.groupby("monthly-index").mean()
    merged['month'] = merged.index
    return merged


def avg(idf):
    return sum(idf['ioc']) / len(idf)
