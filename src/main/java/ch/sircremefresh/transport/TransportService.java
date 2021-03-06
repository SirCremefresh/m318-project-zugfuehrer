package ch.sircremefresh.transport;

import ch.sircremefresh.transport.dto.ConnectionDto;
import ch.sircremefresh.transport.dto.StationDto;
import ch.sircremefresh.transport.dto.StationboardDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import lombok.val;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

public class TransportService {

	public List<StationDto> getStations(final String searchQuery) {
		val gson = getGson();

		val response = createGetRequest("http://transport.opendata.ch/v1/locations?query=" + urlEncode(searchQuery));


		JSONObject responseObject = null;
		try {
			responseObject = new JSONObject(response.getEntity(String.class));
		} catch (JSONException e) {
			throw new TransportApiException("An error occurred when parsing server response", e);
		}

		return ParseResponseToStationDtoList(gson, responseObject);
	}

	public List<StationDto> getStationsNear(final String x, final String y) {
		val gson = getGson();

		val response = createGetRequest("http://transport.opendata.ch/v1/locations?x=" + x + "&y=" + y);

		JSONObject responseObject = null;
		try {
			responseObject = new JSONObject(response.getEntity(String.class));
		} catch (JSONException e) {
			throw new TransportApiException("An error occurred when parsing server response", e);
		}

		return ParseResponseToStationDtoList(gson, responseObject);
	}

	private List<StationDto> ParseResponseToStationDtoList(Gson gson, JSONObject responseObject) {
		try {
			Type StationDtoListType = new TypeToken<List<StationDto>>() {
			}.getType();
			val stationsJsonArray = responseObject.getJSONArray("stations");
			return gson.fromJson(stationsJsonArray.toString(), StationDtoListType);
		} catch (JSONException e) {
			throw new TransportApiException("An error occurred when parsing server response", e);
		}
	}

	public List<ConnectionDto> getConnections(final String from, final String to, final String date, final String time) {
		val gson = getGson();

		val response = createGetRequest("http://transport.opendata.ch/v1/connections?from=" + urlEncode(from) + "&to=" + urlEncode(to) + "&date=" + date + "&time=" + time);

		JSONObject responseObject = null;
		try {
			responseObject = new JSONObject(response.getEntity(String.class));
		} catch (JSONException e) {
			throw new TransportApiException("An error occurred when parsing server response", e);
		}

		try {
			Type connectionDtoListType = new TypeToken<List<ConnectionDto>>() {
			}.getType();
			val connectionsJsonArray = responseObject.getJSONArray("connections");
			return gson.fromJson(connectionsJsonArray.toString(), connectionDtoListType);
		} catch (JSONException e) {
			throw new TransportApiException("An error occurred when parsing server response", e);
		}
	}

	public StationboardDto getStationboard(String station) {
		val gson = getGson();

		val response = createGetRequest("http://transport.opendata.ch/v1/stationboard?station=" + urlEncode(station));

		val responseString = response.getEntity(String.class);

		return gson.fromJson(responseString, StationboardDto.class);
	}

	private ClientResponse createGetRequest(final String url) {
		val client = Client.create();
		val webResource = client.resource(url);
		ClientResponse response;

		try {
			response = webResource
					.accept("application/json")
					.get(ClientResponse.class);
		} catch (ClientHandlerException e) {
			throw new TransportApiException("Connection with Server could not be established", e);
		}

		if (response.getStatus() != 200)
			throw new TransportApiException("An error occurred on the server side");

		return response;
	}

	private Gson getGson() {
		val builder = new GsonBuilder();
		return builder.create();
	}

	public String urlEncode(final String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new TransportApiException("An error occurred while urlEncoding");
		}
	}
}
