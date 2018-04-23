package ch.sircremefresh.pages.departureboard;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.util.StationSearchAutoComplete;
import javafx.fxml.FXML;

public class DepartureBoardController {
	private TransportService transportService = new TransportService();


	@FXML
	private AutoCompleteController stationSearchAutoComplete;

	@FXML
	public void initialize() {
		StationSearchAutoComplete.setupAutoComplete(stationSearchAutoComplete, transportService);
	}
}
