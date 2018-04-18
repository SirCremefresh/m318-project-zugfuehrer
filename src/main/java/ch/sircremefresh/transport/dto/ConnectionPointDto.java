package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ConnectionPointDto {
	public StationDto station;
	public String arrival;
	public String arrivalTimestamp;
	public String departure;
	public String departureTimestamp;
	public int delay;
	public String platform;
	public String realtimeAvailability;
}
