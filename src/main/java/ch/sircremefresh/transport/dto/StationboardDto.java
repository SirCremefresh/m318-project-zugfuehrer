package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class StationboardDto {
	private StationDto station;
	private List<StationboardEntryDto> stationboard;
}
