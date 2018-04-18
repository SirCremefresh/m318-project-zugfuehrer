package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Getter
@Setter
public class ConnectionPointDto {
	private StationDto station;
	private String arrival;
	private String arrivalTimestamp;
	private String departure;
	private String departureTimestamp;
	private int delay;
	private String platform;
	private String realtimeAvailability;

	public String getFormattedDepartureTime() {
		LocalDateTime localDateTime = parserLocalDateTimeFromString(departure);
		return getFormattedTime(localDateTime);
	}

	public String getFormattedArrivalTime() {
		LocalDateTime localDateTime = parserLocalDateTimeFromString(arrival);
		return getFormattedTime(localDateTime);
	}

	private LocalDateTime parserLocalDateTimeFromString(String string) {
		return LocalDateTime.parse(
				string.replace("T", " "),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ")
		);
	}

	private String getFormattedTime(LocalDateTime localDateTime) {
		return localDateTime.getHour() + ":" + (localDateTime.getMinute() < 10? "0": "") + localDateTime.getMinute() ;
	}
}
