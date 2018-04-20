package ch.sircremefresh;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.ConnectionDto;
import ch.sircremefresh.transport.dto.StationDto;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import lombok.val;

import java.util.LinkedList;
import java.util.List;

public class Controller {
	private TransportService transportService = new TransportService();

	private ObservableList<StationDto> fromHints = FXCollections.observableArrayList();
	private ObservableList<StationDto> toHints = FXCollections.observableArrayList();

	private ObservableList<ConnectionDto> connections = FXCollections.observableArrayList();

	private Callback<ListView<StationDto>, ListCell<StationDto>> StationDtoCellFactory = (ListView<StationDto> p) -> new ListCell<StationDto>() {
		@Override
		protected void updateItem(StationDto t, boolean bln) {
			super.updateItem(t, bln);
			if (t != null) {
				setText(t.getName());
			}
		}
	};

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

		initializeAutoComplete(fromAutoComplete);
		initializeAutoComplete(toAutoComplete);
		initializeConnectionTable();
	}

	private void initializeAutoComplete(AutoCompleteController autoComplete) {
		autoComplete.getTextProperty().addListener((observable, oldValue, newValue) -> {
			List<StationDto> stations = transportService.getStations(newValue);
			List<String> hints = new LinkedList<>();
			for (int i = 0; i < stations.size(); i++) {
				if (i > 5)
					break;
				hints.add(stations.get(i).getName());
			}
			autoComplete.setHints(hints);
		});
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

	private StationDto getStationWithNameFromList(String stationName, List<StationDto> list) {
		for (val item : list) {
			if (item.getName().toLowerCase().equals(stationName))
				return item;
		}
		return null;
	}

	private void setFocusOnTextField(final TextField textField) {
		textField.requestFocus();
		textField.positionCaret(textField.getLength());
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
		if (fromHints.size() == 0) {
			showInfoBox("could not find station", "the station: " + fromAutoComplete.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		if (toHints.size() == 0) {
			showInfoBox("could not find station", "the station: " + toAutoComplete.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		fromAutoComplete.setText(fromHints.get(0).getName());
		toAutoComplete.setText(toHints.get(0).getName());
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
