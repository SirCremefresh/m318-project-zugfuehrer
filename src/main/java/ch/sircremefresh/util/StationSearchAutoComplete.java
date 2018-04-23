package ch.sircremefresh.util;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.StationDto;

import java.util.LinkedList;
import java.util.List;

public class StationSearchAutoComplete {
	public static void setupAutoComplete(AutoCompleteController stationSearchAutoComplete, TransportService transportService) {
		stationSearchAutoComplete.getTextProperty().addListener((observable, oldValue, newValue) -> {
			List<StationDto> stations = transportService.getStations(newValue);
			List<String> hints = new LinkedList<>();
			for (int i = 0; i < stations.size(); i++) {
				if (i > 5)
					break;
				hints.add(stations.get(i).getName());
			}
			stationSearchAutoComplete.setHints(hints);
		});
	}
}
