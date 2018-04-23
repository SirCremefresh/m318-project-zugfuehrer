package ch.sircremefresh.location.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationDto {
	private String ipAddress;
	private String city;
	private String latitude;
	private String longitude;
}
