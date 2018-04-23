package ch.sircremefresh.pages.timetable;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.pages.departureboard.DepartureBoardController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.ConnectionDto;
import ch.sircremefresh.transport.dto.StationDto;
import ch.sircremefresh.util.StationSearchAutoComplete;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.val;

import java.util.LinkedList;
import java.util.List;

public class TimetableController {
	private TransportService transportService = new TransportService();

	private ObservableList<ConnectionDto> connections = FXCollections.observableArrayList();

	@FXML
	private AutoCompleteController fromAutoComplete;

	@FXML
	private AutoCompleteController toAutoComplete;

	@FXML
	private TableView<ConnectionDto> connectionTableView;

	@FXML
	private TableColumn<ConnectionDto, String> connectionTableFromColumn;

	@FXML
	private TableColumn<ConnectionDto, String> connectionTableToColumn;

	@FXML
	private TableColumn<ConnectionDto, String> connectionTableDepartureTimeColumn;

	@FXML
	private TableColumn<ConnectionDto, String> connectionTableArrivalTimeColumn;

	@FXML
	private TableColumn<ConnectionDto, String> connectionTableDurationColumn;

	@FXML
	public void initialize() {
		StationSearchAutoComplete.setupAutoComplete(fromAutoComplete, transportService);
		StationSearchAutoComplete.setupAutoComplete(toAutoComplete, transportService);
		initializeConnectionTable();
	}

	private void initializeConnectionTable() {
		connectionTableView.setItems(connections);
		connectionTableFromColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getStation().getName()));
		connectionTableToColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getStation().getName()));
		connectionTableDepartureTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getFormattedDepartureTime()));
		connectionTableArrivalTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getFormattedArrivalTime()));
		connectionTableDurationColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> {
			String duration = cd.getValue().getDuration();
			String formattedDuration = "";
			int days = Integer.parseInt(duration.substring(0, 2));
			if (days > 0)
				formattedDuration += days + " days ";
			int hours = Integer.parseInt(duration.substring(3, 5));
			if (hours > 0)
				formattedDuration += hours + " h ";
			int minutes = Integer.parseInt(duration.substring(6, 8));
			if (minutes > 0)
				formattedDuration += minutes + " min";
			return formattedDuration;
		}));
	}

	@FXML
	public void searchConnections() {
		if (fromAutoComplete.getText().isEmpty()) {
			showInfoBox("from station is empty", "the from station input can't be left empty", Alert.AlertType.WARNING);
			return;
		}
		if (toAutoComplete.getText().isEmpty()) {
			showInfoBox("to station is empty", "the to station input can't be left empty", Alert.AlertType.WARNING);
			return;
		}
		val fromHints = fromAutoComplete.getHints();
		val toHints = toAutoComplete.getHints();
		if (fromHints.size() == 0) {
			showInfoBox("could not find station", "the station: " + fromAutoComplete.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		if (toHints.size() == 0) {
			showInfoBox("could not find station", "the station: " + toAutoComplete.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		fromAutoComplete.setText(fromHints.get(0));
		toAutoComplete.setText(toHints.get(0));
		val result = transportService.getConnections(fromAutoComplete.getText(), toAutoComplete.getText());
		connections.clear();
		connections.addAll(result);
	}

	private void showInfoBox(String title, String msg, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}
