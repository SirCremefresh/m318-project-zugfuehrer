package ch.sircremefresh.pages.timetable;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.controls.numberinput.NumberInputController;
import ch.sircremefresh.location.LocationService;
import ch.sircremefresh.transport.TransportApiException;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.ConnectionDto;
import ch.sircremefresh.transport.dto.StationDto;
import ch.sircremefresh.util.InfoBox;
import ch.sircremefresh.util.StationSearchAutoComplete;
import ch.sircremefresh.util.TimeFormatter;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.val;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimetableController {
	private TransportService transportService = new TransportService();
	private LocationService locationService = new LocationService();

	private ObservableList<ConnectionDto> connections = FXCollections.observableArrayList();

	@FXML
	private AutoCompleteController fromAutoComplete;

	@FXML
	private AutoCompleteController toAutoComplete;

	@FXML
	private DatePicker connectionDateDatePicker;

	@FXML
	private NumberInputController connectionDateHourTextField;

	@FXML
	private NumberInputController connectionDateMinuteTextField;

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
		try {
			val location = locationService.getLocation();
			System.out.println(location.getCity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		StationSearchAutoComplete.setupAutoComplete(fromAutoComplete, transportService);
		StationSearchAutoComplete.setupAutoComplete(toAutoComplete, transportService);
		setupDateTimePicker();
		initializeConnectionTable();
		fromAutoComplete.getTextField().requestFocus();
	}

	@FXML
	private void setupDateTimePicker() {
		connectionDateDatePicker.setValue(LocalDate.now());

		connectionDateHourTextField.setMaxSize(2);
		connectionDateMinuteTextField.setMaxSize(2);

		val dateTime = LocalDateTime.now();
		connectionDateHourTextField.setText(String.valueOf(dateTime.getHour()));
		connectionDateMinuteTextField.setText(String.valueOf(dateTime.getMinute()));
	}

	private void initializeConnectionTable() {
		connectionTableView.focusedProperty().addListener((arg0, oldVal, newVal) -> {
			((BehaviorSkinBase) connectionTableView.getSkin()).getBehavior().traverseNext();
		});


		connectionTableView.setItems(connections);

		connectionTableFromColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableToColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableDepartureTimeColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableArrivalTimeColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));
		connectionTableDurationColumn.prefWidthProperty().bind(connectionTableView.widthProperty().divide(5));

		connectionTableFromColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getStation().getName()));
		connectionTableToColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getStation().getName()));
		connectionTableDepartureTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> TimeFormatter.getFormattedTime(cd.getValue().getFrom().getDeparture())));
		connectionTableArrivalTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> TimeFormatter.getFormattedTime(cd.getValue().getTo().getArrival())));
		connectionTableDurationColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> {
			String duration = cd.getValue().getDuration();
			return TimeFormatter.getFormattedDuration(duration);
		}));
	}

	@FXML
	public void showLocation() {
		if (toAutoComplete.getText().isEmpty()) {
			InfoBox.show("Text field is empty", "you need to type in the station name", Alert.AlertType.INFORMATION);
		}
		List<StationDto> stations;
		try {
			stations = transportService.getStations(toAutoComplete.getText());
		} catch (TransportApiException e) {
			InfoBox.show("error while getting station", e.getMessage(), Alert.AlertType.ERROR);
			return;
		} catch (Exception e) {
			InfoBox.show("something went wrong", "while getting station", Alert.AlertType.ERROR);
			return;
		}

		if (stations.size() == 0) {
			InfoBox.show("no station found", "no station matched to '" + toAutoComplete.getText() + "'", Alert.AlertType.ERROR);
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
			InfoBox.show("from station is empty", "the from station input can't be left empty", Alert.AlertType.WARNING);
			return;
		}
		if (toAutoComplete.getText().isEmpty()) {
			InfoBox.show("to station is empty", "the to station input can't be left empty", Alert.AlertType.WARNING);
			return;
		}
		val fromHints = fromAutoComplete.getHints();
		val toHints = toAutoComplete.getHints();
		if (fromHints.size() == 0) {
			InfoBox.show("could not find station", "the station: " + fromAutoComplete.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		if (toHints.size() == 0) {
			InfoBox.show("could not find station", "the station: " + toAutoComplete.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}

		val date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(connectionDateDatePicker.getValue());
		val time = connectionDateHourTextField.getText() + ":" + connectionDateMinuteTextField.getText();
		fromAutoComplete.setText(fromHints.get(0));
		toAutoComplete.setText(toHints.get(0));

		List<ConnectionDto> newConnections;
		try {
			newConnections = transportService.getConnections(fromAutoComplete.getText(), toAutoComplete.getText(), date, time);
		} catch (TransportApiException e) {
			InfoBox.show("error while getting connections", e.getMessage(), Alert.AlertType.ERROR);
			return;
		} catch (Exception e) {
			InfoBox.show("something went wrong", "while getting connections", Alert.AlertType.ERROR);
			return;
		}

		connections.clear();
		connections.addAll(newConnections);
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
					.append(TimeFormatter.getFormattedTime(connection.getFrom().getDeparture()))
					.append("%0A");
		}

		uri.append("%0A%0A%0A%0A%0A%0AGenerated%20by%20the%20best%20Commute%20Application%20Ever");

		try {
			desktop.mail(new URI(uri.toString()));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
