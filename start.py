import util
import color

cset = color.PURPLE
cnames = color.BLUE
crender = color.CYAN
cgenerate = color.DARKCYAN


def prints(names, render_names, generate_names):
    print(cnames + color.UNDERLINE + f"Chats" + color.END + cnames + ": " + (', '.join(names)) + color.END)
    print(crender + color.UNDERLINE + f"Will Render" + color.END + crender + ": " +
          (color.BOLD + "All" if render_names == ["*"] else
           color.BOLD + "None" if render_names == [] else ', '.join(render_names)) + color.END)
    print(cgenerate + color.UNDERLINE + f"Will Generate" + color.END + cgenerate + ": " +
          (color.BOLD + "All" if generate_names == ["*"] else
           color.BOLD + "None" if generate_names == [] else ', '.join(generate_names)) + color.END)

    util.warn_user(render_names, names)


def settings(render_final, optimize):
    print(cset + color.BOLD + f"Starting with settings:" + color.END)
    print("    " + cset + color.UNDERLINE + f"render mutual graph(s)" + color.END +
          cset + f": " + str(render_final))
    print("    " + cset + color.UNDERLINE + f"optimize" + color.END +
          cset + f" (don't generate time-consuming data): " + str(optimize))


def uncolor():
    global cset
    global cnames
    global crender
    global cgenerate
    cset = ''
    cnames = ''
    crender = ''
    cgenerate = ''
