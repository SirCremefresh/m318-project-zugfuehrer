package ch.sircremefresh.transport.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ConnectionDto {
	public ConnectionPointDto from;
	public ConnectionPointDto to;
	public String duration;
}
