package ch.sircremefresh.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {
	public static String getFormattedDuration(String DurationString) {
		String formattedDuration = "";
		int days = Integer.parseInt(DurationString.substring(0, 2));
		if (days > 0)
			formattedDuration += days + " days ";
		int hours = Integer.parseInt(DurationString.substring(3, 5));
		if (hours > 0)
			formattedDuration += hours + " h ";
		int minutes = Integer.parseInt(DurationString.substring(6, 8));
		if (minutes > 0)
			formattedDuration += minutes + " min";
		return formattedDuration;
	}

	public static String getFormattedTime(String localDateString) {
		LocalDateTime localDateTime = parserLocalDateTimeFromString(localDateString);
		return getFormattedTime(localDateTime);
	}

	private static String getFormattedTime(LocalDateTime localDateTime) {
		return localDateTime.getHour() + ":" + (localDateTime.getMinute() < 10 ? "0" : "") + localDateTime.getMinute();
	}

	public static LocalDateTime parserLocalDateTimeFromString(String string) {
		return LocalDateTime.parse(
				string.replace("T", " "),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ")
		);
	}
}
