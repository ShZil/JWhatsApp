import matplotlib.patches as mpatches

# Alphabets DF ---------------------------------------------------------------------------------------------------------
import util

letters = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
           "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"]
punctuation = ["+", ",", ".", "-", "\"", "&", "!", "?", ":", ";", "#", "~", "=", "\\"
               "/", "$", "@", "^", "(", ")", "_", "<", ">", "[", "]", "\'", "`", "*", "|"]
hebrew = ["א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ", "ל", "מ", "נ",
          "ס", "ע", "פ", "צ", "ק", "ר", "ש", "ת", "ך", "ן", "ף", "ץ", "ם"]
math = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]


def count_letters(text):
    return sum(c.lower() in letters for c in text)


def count_spaces(text):
    return sum(c == " " for c in text)


def count_punctuation(text):
    return sum(c in punctuation for c in text)


def count_hebrew(text):
    return sum(c in hebrew for c in text)


def count_math(text):
    return sum(c in math for c in text)


def characters():
    return [" "] + letters + punctuation + hebrew + math


def get_alphabet_colors():
    return {'english-letters': '#f03535', 'hebrew-letters': '#ff69ed', 'punctuation': '#059e00', 'math': '#2dacd6',
            'emojis': '#ffc800', 'whitespace': '#808080'}


def get_character_colors(chars):
    colors = []
    for char in chars:
        if char in letters:
            colors.append('#f03535')
        elif char in punctuation:
            colors.append('#059e00')
        elif char in hebrew:
            colors.append('#ff69ed')
        elif char in math:
            colors.append('#2dacd6')
        elif char == util.character_name(' '):
            colors.append('#808080')
        else:
            colors.append('#ffde34')
    return colors


def get_legend():
    patches = []
    for key, value in get_alphabet_colors().items():
        patches.append(mpatches.Patch(color=value, label=key))
    return patches
