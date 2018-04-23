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
}
