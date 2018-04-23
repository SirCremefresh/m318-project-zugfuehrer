package ch.sircremefresh.location;

import ch.sircremefresh.location.dto.LocationDto;
import ch.sircremefresh.util.IpGetter;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class LocationService {
	private DatabaseReader dbReader;

	public LocationService() {
		File database = new File(getClass().getResource("GeoLite2-City.mmdb").getFile());
		try {
			dbReader = new DatabaseReader.Builder(database).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public LocationDto getLocation()
			throws Exception {
		String ip = IpGetter.getIp();
		InetAddress ipAddress = InetAddress.getByName(ip);
		CityResponse response = dbReader.city(ipAddress);

		String cityName = response.getCity().getName();
		String latitude = response.getLocation().getLatitude().toString();
		String longitude = response.getLocation().getLongitude().toString();
		return new LocationDto(ip, cityName, latitude, longitude);
	}
}
