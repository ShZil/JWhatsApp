# Regular DF -----------------------------------------------------------------------------------------------------------
import pandas as pd
from timeit import default_timer as timer

import util


def build(messages):
    temp = timer()
    texts = []
    dates = []
    times = []
    authors = []
    for index in range(len(messages)):
        # FORMAT: "mm/dd/yy, HH:MM - [Author]: [message]"
        message = messages[index]
        date = message[:message.find(',')]
        time = message[message.find(', ') + 2:message.find(' - ')]
        author = util.remove_unwanted_chars(message[message.find(' - ') + 3:util.find_2nd(message, ':')])
        text = util.remove_unwanted_chars(message[util.find_2nd(message, ':') + 1:])
        if util.find_2nd(message, ':') == -1:
            author = "WhatsApp"
            text = util.remove_unwanted_chars(message[message.find(' - ') + 3:])
        elif "created group" in author or "changed the" in author:
            text = author
            author = "WhatsApp"
        texts.append(text)
        times.append(time)
        dates.append(date)
        authors.append(author)
        print("NDF -", index, end='\r')

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(dates, times, authors, texts)),
                        columns=['date', 'time', 'author', 'message'])


def row(df, i):
    return (df.at[i, "date"],
            df.at[i, "time"],
            df.at[i, "author"],
            df.at[i, "message"])


def get_authors(df):
    authors = []
    for i in range(len(df)):
        date, time, author, message = row(df, i)
        if author not in authors:
            authors.append(author)
    return authors


def avg(nd):
    try:
        return sum([len(string) for string in nd['message']]) / len(nd)
    except ZeroDivisionError:
        return 0


def count(nd):
    return len(nd)
