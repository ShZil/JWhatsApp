# Math / Util ----------------------------------------------------------------------------------------------------------
import pandas as pd

import Alphabets
import BlocksDataFrame
import IOCDataFrame
import os

import NormalDataFrame
import TimeDataFrame

times = []
times_index = 0


def find_2nd(string, substring):
    return string.find(substring, string.find(substring) + 1)


def is_nan(num):
    return num != num


def two_digits(num):
    if num < 10:
        return '0' + str(num)
    else:
        return str(num)


def get_paths(names):
    paths = []
    for name in names:
        paths.append(f"WhatsApp Chat with {name}.txt")
    return paths


def get_names(paths):
    names = []
    for path in paths:
        names.append(path[len("raw/WhatsApp Chat with "):path.find(".txt")])
    return names


def remove_unwanted_chars(string):
    return string.strip('\"').strip('\n').strip(' ').replace("\u200e", "").replace("\u202b", "").replace("\u202c", "")


def character_name(char):
    if char == " ":
        return "Space"
    return char


def stringify_weekday(n):
    return (["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"])[n]


def stringify_month(month_id):
    return f"{two_digits(month_id%12 + 1)}/{two_digits(month_id//12)}"


def fix_rtl(strings):
    new = []
    for string in strings:
        if Alphabets.count_hebrew(string) > 0:
            new.append(string[::-1].replace(')', '(').replace('(', ')'))
        else:
            new.append(string)
    return new


def ioc(string):
    if len(string) < 2:
        return 0
    dict = {}
    for char in string:
        if char not in dict.keys():
            dict[char] = string.count(char)
    ioc = sum([x * (x-1) for x in dict.values()])
    return ioc / (pow(len(string), 2) - len(string))


def percentages(start, end, jump):
    return list(frange(start/100, end/100, jump/100)), [str(x) + '%' for x in range(start, end+1, jump)]


def frange(x, y, jump):
    while x <= y:
        yield x
        x += jump


def not_me(ndf):
    selfs = ["Me", "Self", "You", "Shaked (Dan) Zilberman"]
    for me in selfs:
        ndf = ndf[ndf.author != me]
    return list(ndf.index)


def find_avgs(ndf, idf, bdf, tdf):
    indexes = not_me(ndf)
    filtered_idf = idf[idf.index.isin(indexes)]
    filtered_bdf = bdf[bdf['starting-index'].isin(indexes)]
    ioc_avg = list(IOCDataFrame.per_month(filtered_idf, tdf)['ioc'])
    block_avg = list(BlocksDataFrame.per_month(filtered_bdf, tdf)['avg-char-count'])
    return ioc_avg, block_avg


def pride():
    return ['Red', 'Orange', 'Yellow', 'Green', 'Turquoise', 'Blue', 'Violet']


def date(str):
    for char in str:
        if char not in ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', ':', ',', ' ', '.']:
            return False
    return (str.count('/') == 2 or str.count('.') == 2) and str.count(',') == 1


def get_chats():
    paths = ["raw/" + x for x in list(filter(lambda x: x.endswith('.txt'), os.listdir(path="./raw")))]
    return paths, get_names(paths)


def warn_user(render_names, names):
    if len(set(render_names) - set(names)) == 0 or set(render_names) == {"*"}:
        return
    print("Not all render requests are present in the folder. Missing:")
    print(', '.join(set(render_names) - set(names)))


def append_time(t):
    global times_index
    global times
    if times_index == 0:
        times.append([])
    times[len(times) - 1].append(t)
    times_index += 1
    times_index %= 10


def get_times():
    return times


def progress(value, array, shape):
    index = array.index(value)
    length = len(array)
    return (" " * (max(map(lambda x: len(x), array)) - len(value))) + shape[0] + (shape[1] * (index + 1)) +\
           (shape[2] * (length - index - 1)) + shape[3]


# Filters / Group-By -------------------------------------------------------------------------------------------------

def filter_author(df, allowed_author):
    return df[df.author == allowed_author]


def select_month(df, time_df, allowed_month_id):
    t = time_df[time_df['monthly-index'] == allowed_month_id]
    n = df[df.index.isin(list(t.index))]
    return n, t


def parse_settings():
    f = open("settings.txt", 'r', encoding="utf8")
    lines = f.readlines()
    f.close()
    d = {
        "render_names": [],
        "generate_names": [],
        "render_final": False,
        "optimize": False,
        "colored": True
    }
    for line in lines:
        line = line.strip("\n")
        key, value = line.split(": ")
        if isinstance(d[key], bool):
            d[key] = True if value.lower() == "true" or value == "1" else False
        else:
            d[key] = list(map(lambda x: x.strip(' ').strip('"'), value.replace('[', '', 1).replace(']', '').split(",")))
        if not isinstance(d[key], bool):
            if d[key][0] == "":
                d[key] = []
    return tuple(d.values())
