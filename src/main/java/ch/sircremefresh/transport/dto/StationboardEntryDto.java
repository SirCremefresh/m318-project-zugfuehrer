package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StationboardEntryDto {
	private StopDto stop;
	private String to;
}
