package ch.sircremefresh.pages.timetable;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.ConnectionDto;
import ch.sircremefresh.util.StationSearchAutoComplete;
import ch.sircremefresh.util.TimeFormatter;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import lombok.val;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimetableController {
	private TransportService transportService = new TransportService();

	private ObservableList<ConnectionDto> connections = FXCollections.observableArrayList();

	@FXML
	private AutoCompleteController fromAutoComplete;

	@FXML
	private AutoCompleteController toAutoComplete;

	@FXML
	private DatePicker connectionDateDatePicker;

	@FXML
	private TextField connectionDateHourTextField;

	@FXML
	private TextField connectionDateMinuteTextField;

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
		setupDateTimePicker();
		initializeConnectionTable();
	}

	@FXML
	private void setupDateTimePicker() {
		connectionDateDatePicker.setValue(LocalDate.now());
		val dateTime = LocalDateTime.now();
		connectionDateHourTextField.setText(String.valueOf(dateTime.getHour()));
		connectionDateMinuteTextField.setText(String.valueOf(dateTime.getMinute()));
	}

	private void initializeConnectionTable() {
		connectionTableView.setItems(connections);

		connectionTableFromColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableToColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableDepartureTimeColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableArrivalTimeColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableDurationColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));

		connectionTableFromColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getStation().getName()));
		connectionTableToColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getStation().getName()));
		connectionTableDepartureTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getFormattedDepartureTime()));
		connectionTableArrivalTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getFormattedArrivalTime()));
		connectionTableDurationColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> {
			String duration = cd.getValue().getDuration();
			return TimeFormatter.getFormattedDuration(duration);
		}));
	}

	@FXML
	public void showLocation() {
		if (toAutoComplete.getText().isEmpty()) {
			showInfoBox("Text field is empty", "you need to type in the station name", Alert.AlertType.INFORMATION);
		}
		val stations = transportService.getStations(toAutoComplete.getText());
		if (stations.size() == 0) {
			showInfoBox("no station found", "no station matched to '" + toAutoComplete.getText() + "'", Alert.AlertType.ERROR);
		}
		val station = stations.get(0);
		try {
			Desktop.getDesktop().browse(new URL("https://www.google.com/maps/search/?api=1&query=" + station.getCoordinate().getX() + "," + station.getCoordinate().getY()).toURI());
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
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

		val date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(connectionDateDatePicker.getValue());
		val time = connectionDateHourTextField.getText() + ":" + connectionDateMinuteTextField.getText();
		fromAutoComplete.setText(fromHints.get(0));
		toAutoComplete.setText(toHints.get(0));
		val result = transportService.getConnections(fromAutoComplete.getText(), toAutoComplete.getText(), date, time);
		connections.clear();
		connections.addAll(result);
	}

	@FXML
	public void shareConnections() {
		searchConnections();
		val fromHints = fromAutoComplete.getHints();
		val toHints = toAutoComplete.getHints();
		Desktop desktop = Desktop.getDesktop();
		StringBuilder uri = new StringBuilder("mailto:?");
		uri
				.append("subject=Connections%20From:")
				.append(transportService.urlEncode(fromHints.get(0)))
				.append("%20To:")
				.append(transportService.urlEncode(toHints.get(0)));

		uri.append("&body=");
		for (val connection : connections) {
			uri
					.append("From:%20")
					.append(transportService.urlEncode(connection.getFrom().getStation().getName()))
					.append("%20To:%20")
					.append(transportService.urlEncode(connection.getTo().getStation().getName()))
					.append("%20Duration:%20")
					.append(
							TimeFormatter.getFormattedDuration(
									connection.getDuration()
							).replace(" ", "%20")
					)
					.append("%20Departure:%20")
					.append(connection.getFrom().getFormattedDepartureTime())
					.append("%0A");
		}

		uri.append("%0A%0A%0A%0A%0A%0AGenerated%20by%20the%20best%20Commute%20Application%20Ever");

		try {
			desktop.mail(new URI(uri.toString()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private void showInfoBox(String title, String msg, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}
