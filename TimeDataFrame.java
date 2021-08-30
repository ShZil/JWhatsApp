import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeDataFrame extends DataFrame {
    public TimeDataFrame(String[][] data, String[] columns) {
        super(data, columns);
    }

    public static TimeDataFrame build(NormalDataFrame ndf, boolean run) {
        if (!run) {
            return null;
        }
        int len = ndf.length;
        String[] minutes = new String[len];
        String[] hours = new String[len];
        String[] days = new String[len];
        String[] months = new String[len];
        String[] years = new String[len];
        String[] weekdays = new String[len];
        String[] monthsSince = new String[len];
        String[] dailyIDs = new String[len];
        for (int i : ndf.indexing) {
            String[] row = ndf.row(i);
            String date = row[0];
            String time = row[1];
            String hour, minute, day, month, year, weekday, monthID, dayID;
            if (time.length() > 5 || time.split(":").length < 2 || date.split("/").length < 3) {
                hour = "0";
                minute = "0";
                day = "0";
                month = "0";
                year = "0";
                weekday = "0";
                monthID = "0";
                dayID = "0";
            } else {
                hour = time.split(":")[0];
                minute = time.split(":")[1];
                month = date.split("/")[0];
                day = date.split("/")[1];
                year = date.split("/")[2];
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), 0, 0);
                weekday = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
                monthID = String.valueOf(Integer.parseInt(year) * 12 + Integer.parseInt(month));
                dayID = String.valueOf(Integer.parseInt(monthID) * 31 + Integer.parseInt(day));
            }
            hours[i] = hour;
            minutes[i] = minute;
            days[i] = day;
            months[i] = month;
            years[i] = year;
            weekdays[i] = weekday;
            monthsSince[i] = monthID;
            dailyIDs[i] = dayID;
        }
        return new TimeDataFrame(new String[][]{minutes, hours, days, months, years, weekdays, monthsSince, dailyIDs},
                new String[]{"minute", "hour", "day", "month", "year", "weekday", "monthly-index", "day-id"});
    }
}
