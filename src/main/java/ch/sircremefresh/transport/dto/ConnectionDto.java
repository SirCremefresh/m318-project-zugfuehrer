package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ConnectionDto {
	private ConnectionPointDto from;
	private ConnectionPointDto to;
	private String duration;
}
