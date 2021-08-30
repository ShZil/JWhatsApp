# Rendering ------------------------------------------------------------------------------------------------------------
import numpy as np
import pandas as pd
from matplotlib import pyplot as plt
import matplotlib.patches as mpatches

import Alphabets
import AlphabetsDataFrame
import IOCDataFrame
import NormalDataFrame
import TimeDataFrame

import util
import color


def renders(name, render_names, ndf, adf, tdf, cdf, mtdf, wdf, idf, bdf):
    print("Rendering chat with " + color.UNDERLINE + name + color.END + ". " +
          color.GREEN + util.progress(name, render_names, "[- ]"))
    apmdf = AlphabetsDataFrame.per_month(adf, tdf)
    ipmdf = IOCDataFrame.per_month(idf, tdf)

    fig, axs = plt.subplots(2)
    render_weekdays(axs[0], tdf, name)
    render_hours(axs[1], ndf, tdf, name)
    fig, ax = plt.subplots()
    render_alphabets(ax, apmdf, name)
    fig, axs = plt.subplots(2)
    render_characters_bars(axs[0], cdf, name)
    render_words(axs[1], wdf, name)
    fig, axs = plt.subplots(2)
    render_ioc(axs[0], ipmdf, name)
    render_lengths(axs[1], ndf, tdf, name)


def render_characters_bars(ax, cdf, name):
    if cdf is None:
        return
    c = cdf.sort_values(by="rarity", ascending=False)
    c = c[c['rarity'] > 200]
    letters = [util.character_name(char) for char in c['letter']]
    colors = Alphabets.get_character_colors(letters)
    c = pd.DataFrame(list(zip(letters, c['rarity'])), columns=['letter', 'rarity'])
    c.plot(legend=False, x="letter", y="rarity", kind='bar', ax=ax, color=colors)
    ax.set_xlabel('character')
    ax.set_ylabel('appearances')
    ax.legend(handles=Alphabets.get_legend())
    ax.set_title(f"{name} - Usage of each character:")


def render_weekdays(ax, tdf, name):
    weekdays = np.bincount(tdf["weekday"])
    ax.pie(weekdays, labels=[util.stringify_weekday(i) for i in range(len(weekdays))], colors=util.pride())
    ax.set_title(f"{name} - Activity per weekday:")


def render_hours(ax, ndf, tdf, name):
    authors = list(set(ndf['author']))
    if 'WhatsApp' in authors:
        authors.remove('WhatsApp')
    for author in authors:
        n = list(ndf[ndf['author'] == author].index)
        t = TimeDataFrame.filter_indexes(n, tdf)
        hours = np.bincount(t["hour"])
        ax.plot(range(len(hours)), hours)
    ax.set_xticks(range(24), minor=False)
    ax.set_xticklabels(["Midnight"] +
                       ['0' + str(x) + ':00' for x in range(1, 10)] +
                       ["11:00", "Midday"] +
                       [str(x) + ':00' for x in range(13, 25)])
    ax.set_xlabel('hour')
    ax.set_ylabel('messages')
    ax.set_title(f"{name} - Messages per hour:")
    ax.legend(util.fix_rtl(authors))
    plt.show()


def render_alphabets(ax, apmdf, name):
    apmdf.pop("length")
    month_indexes = apmdf.pop("month")
    apmdf = apmdf.div(apmdf.sum(axis=1), axis=0)  # NORMALIZE!
    apmdf.plot.area(color=Alphabets.get_alphabet_colors().values(), ax=ax)
    ax.set_xticklabels([util.stringify_month(x) for x in month_indexes])
    ax.set_yticks(util.percentages(0, 100, 20)[0])
    ax.set_yticklabels(util.percentages(0, 100, 20)[1])
    ax.set_title(f"{name} - Alphabets per month:")
    plt.show()


def render_words(ax, wdf, name):
    if wdf is None:
        return
    wdf = wdf.head(100)
    words = util.fix_rtl(wdf['word'])
    wdf = pd.DataFrame(list(zip(words, wdf['rarity'])), columns=['word', 'rarity'])
    colors = Alphabets.get_character_colors([x[0] for x in words])
    wdf.plot(legend=False, x="word", y="rarity", kind='bar', ax=ax, color=colors)
    ax.set_xlabel('words')
    ax.set_ylabel('appearances')
    ax.set_title(f"{name} - Commonly Used Words:")
    ax.legend(handles=Alphabets.get_legend())
    plt.show()


def render_ioc(ax, ipmdf, name):
    month_indexes = list(ipmdf.pop("month"))
    x = list(range(len(ipmdf)))
    ax.plot(x, ipmdf['ioc'])
    ax.plot(x, [IOCDataFrame.avg(ipmdf)] * len(x), linestyle='--')
    ax.set_xticks(range(len(month_indexes)))
    ax.set_xticklabels([util.stringify_month(x) for x in month_indexes])
    ax.set_yticks(util.percentages(0, 20, 4)[0])
    ax.set_yticklabels(util.percentages(0, 20, 4)[1])
    ax.set_title(f"{name} - Indexes Of Coincidence per month:")


def render_lengths(ax, ndf, tdf, name):
    months = TimeDataFrame.get_months(tdf)
    x = list(range(len(months)))
    authors = NormalDataFrame.get_authors(ndf)
    authors.remove("WhatsApp")
    values = {}
    sums = {i: 0 for i in months}
    for author in authors:
        n = ndf[ndf.author == author]
        values[author] = []
        for month in months:
            nd, td = util.select_month(n, tdf, month)
            c = NormalDataFrame.count(nd)
            values[author].append(c)
            sums[month] += c
        ax.plot(x, values[author])
    ax.plot(x, list(sums.values()), linestyle='--')
    ax.set_xticks(range(len(months)))
    ax.set_xticklabels([util.stringify_month(x) for x in months])
    ax.set_ylabel('Message Count')
    ax.set_title(f"{name} - Amount of messages per month:")
    ax.legend(util.fix_rtl(authors) + ["Total"])
    plt.show()


def render_relations(names, a):
    colors = ['#08bdaf', '#467ef4', '#da66e1', '#9ff07f', '#ea6a84', '#fb9300', '#ffce00', '#2940d3', '#511281',
              '#f21170', '#dad9d7', '#065535', '#b4d4f2', '#e76aa3', '#93bd8c', '#d2a58e', '#805b00', '#44281d',
              '#f9646c', '#f94552', '#fcea8b', '#afb5fc', '#fa626d', '#fdf0a6', '#008080', '#eebbf5', '#0ff1ce',
              '#9933ff', '#a020f0', '#bada55', '#0ff1ce', '#87ceeb', '#809c7c', '#809c7c', '#65708b', '#5c3d46',
              '#646570', '#cc99cc', '#dcdcdb', '#5e1224', '#3e0212', '#800020', '#e0dcdb', '#b7b4bb', '#222023',
              '#5c3d46', '#646570'][:len(names)]
    xs = []
    ys = []
    cs = []
    for i in range(len(a)):
        x_list = [x for x in a[i][0][:len(a[i][1])]]
        y_list = [y for y in a[i][1][:len(a[i][0])]]
        xs.extend(x_list)
        ys.extend(y_list)
        cs.extend([colors[i]] * len(x_list))
    plt.scatter(xs, ys, c=cs)
    patches = []
    for name, color in zip(names, colors):
        patches.append(mpatches.Patch(color=color, label=name))
    plt.legend(handles=patches)
    plt.xlabel('Index Of Coincidence')
    plt.ylabel('Block Lengths (characters)')
    plt.title("Relations between chats:")
    plt.show()


def display_times(df):
    columns = np.array(df.columns)
    columns = columns[columns != "Name"]
    for column in columns:
        plt.plot(range(len(df[column])), np.array(df[column]))
    plt.title("DF/Generate Time")
    plt.legend(np.array(columns))
    plt.show()
