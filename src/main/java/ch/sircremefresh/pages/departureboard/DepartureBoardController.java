package ch.sircremefresh.pages.departureboard;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.util.StationSearchAutoComplete;
import javafx.fxml.FXML;
import lombok.val;

public class DepartureBoardController {
	private TransportService transportService = new TransportService();


	@FXML
	private AutoCompleteController stationSearchAutoComplete;

	@FXML
	public void initialize() {
		StationSearchAutoComplete.setupAutoComplete(stationSearchAutoComplete, transportService);
	}

	@FXML
	public void showStationBoard() {
		val stationBoard = transportService.getStationboard(stationSearchAutoComplete.getText());
	}
}
