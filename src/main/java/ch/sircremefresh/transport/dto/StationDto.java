package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StationDto {
	private String id;
	private String name;
	private CoordinateDto coordinate;
}
