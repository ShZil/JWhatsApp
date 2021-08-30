# Message Type DF ------------------------------------------------------------------------------------------------------
# Message Types:
# text,
# media,
# deleted,
# whatsapp-info,
# group-created,
# participant-join,
# participant-leave,
# description-change,
# title-change,
# icon-change,
# admin,
# blocked,
# unblocked,
# contact
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
    types = []
    authors = []
    messages = []
    args = []
    indexes = []
    for i in list(df.index):
        is_text = False
        has_args = False
        date, time, author, text = NormalDataFrame.row(df, i)
        if util.is_nan(text):
            types.append('nan')
            authors.append(author)
            args.append("")
            continue
        if text == "Messages and calls are end-to-end encrypted. No one outside of this chat, not even WhatsApp, can read or listen to them. Tap to learn more.":
            types.append('whatsapp-info')
            authors.append(author)
        elif "Your security code with" in text or text == "This chat is with a business account. Tap to learn more.":
            types.append('whatsapp-info')
            authors.append(author)
        elif "created group" in text:
            types.append('group-created')
            authors.append(text[:text.find(" created group")])
        elif "joined using this group\'s invite link" in text:
            types.append('participant-join')
            authors.append(text[:text.find(" joined using this group\'s invite link")])
        elif "was added" in text:
            types.append('participant-join')
            authors.append(author)
            args.append(text[:text.find(" was added")].replace("and", ","))
            has_args = True
        elif "added" in text:
            types.append('participant-join')
            authors.append(text[:text.find(" added")])
            args.append(text[text.find("added ") + 6:].replace("and", ","))
            has_args = True
        elif "changed the subject" in text:
            types.append('title-change')
            authors.append(text[:text.find(" changed the subject")])
            args.append(text[text.find("changed the subject from") + len("changed the subject from"):text.find(
                " to ")] + ',' + text[text.find(" to ") + 4:])
            has_args = True
        elif "changed the group description" in text:
            types.append('description-change')
            authors.append(text[:text.find(" changed the group description")])
        elif "changed this group\'s settings" in text:
            types.append('settings-change')
            authors.append(text[:text.find(" changed this group\'s settings")])
        elif "changed this group\'s icon" in text:
            types.append('icon-change')
            authors.append(text[:text.find(" changed this group\'s icon")])
        elif text == "You\'re now an admin":
            types.append('admin')
            authors.append('You')
        elif "left" in text and author == "WhatsApp":
            types.append('participant-leave')
            authors.append(text[:text.find(" left")])
        elif text == "You blocked this contact. Tap to unblock.":
            types.append('block')
            authors.append('Self')
        elif text == "You unblocked this contact.":
            types.append('unblock')
            authors.append('Self')
        elif text == "This message was deleted":
            types.append("deleted")
            authors.append(author)
        elif text == "<Media omitted>":
            types.append("media")
            authors.append(author)
        elif author == "WhatsApp":
            types.append('whatsapp-info')
            authors.append(author)
        else:
            is_text = True
        if not is_text:
            indexes.append(i)
            messages.append(text)
        if not has_args:
            args.append("")

    util.append_time(timer() - temp)
    return pd.DataFrame(list(zip(indexes, types, authors, args, messages)),
                        columns=['ind', 'type', 'author', 'arg', '__message'])


def row(mtdf, i):
    return (mtdf.at[i, "ind"],
            mtdf.at[i, "type"],
            mtdf.at[i, "author"],
            mtdf.at[i, "arg"])
