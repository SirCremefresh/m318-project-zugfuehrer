package ch.sircremefresh;

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
import javafx.util.Callback;
import lombok.val;

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
	private TextField fromTextField;

	@FXML
	private ListView<StationDto> fromHintsListView;

	@FXML
	private TextField toTextField;

	@FXML
	private ListView<StationDto> toHintsListView;

	@FXML
	private TableView<ConnectionDto> connectionTable;

	@FXML
	private TableColumn<ConnectionDto, String> departureTimeColumn;

	@FXML
	private TableColumn<ConnectionDto, String> fromColumn;

	@FXML
	private TableColumn<ConnectionDto, String> toColumn;

	@FXML
	private TableColumn<ConnectionDto, String> arrivalTimeColumn;

	@FXML
	private TableColumn<ConnectionDto, String> durationColumn;

	@FXML
	public void initialize() {
		initializeStationSearcherField(fromTextField, fromHintsListView, fromHints);
		initializeStationSearcherField(toTextField, toHintsListView, toHints);
		initializeConnectionTable();
	}

	private void initializeStationSearcherField(TextField textField, ListView<StationDto> listView, ObservableList<StationDto> hints) {
		listView.setCellFactory(StationDtoCellFactory);
		listView.setItems(hints);

		listView.setOnMouseClicked((mouseEvent) -> {
			EventTarget target = mouseEvent.getTarget();
			String station;
			if (target instanceof ListCell && ((ListCell) target).getText() != null) {
				station = ((ListCell) target).getText();
			} else if (target instanceof LabeledText) {
				station = ((LabeledText) target).getText();
			} else {
				listView.visibleProperty().setValue(false);
				return;
			}
			textField.setText(station);
			setFocusOnTextField(textField);
		});

		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			List<StationDto> stations = transportService.getStations(newValue);
			hints.clear();
			for (int i = 0; i < stations.size(); i++) {
				if (i > 5)
					break;
				hints.add(stations.get(i));
			}
		});

		textField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
			listView.visibleProperty().setValue(newPropertyValue || listView.focusedProperty().getValue());
		});
	}

	private void initializeConnectionTable() {
		connectionTable.setItems(connections);
		fromColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getStation().getName()));
		toColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getStation().getName()));
		departureTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getFrom().getFormattedDepartureTime()));
		arrivalTimeColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo().getFormattedArrivalTime()));
		durationColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> {
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
		if (fromTextField.getText().isEmpty()) {
			showInfoBox("from station is empty", "the from station input can't be left empty", Alert.AlertType.WARNING);
			return;
		}
		if (toTextField.getText().isEmpty()) {
			showInfoBox("to station is empty", "the to station input can't be left empty", Alert.AlertType.WARNING);
			return;
		}
		if (fromHints.size() == 0) {
			showInfoBox("could not find station", "the station: " + fromTextField.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		if (toHints.size() == 0) {
			showInfoBox("could not find station", "the station: " + toTextField.getText() + " could not be found", Alert.AlertType.WARNING);
			return;
		}
		fromTextField.setText(fromHints.get(0).getName());
		toTextField.setText(toHints.get(0).getName());
		val result = transportService.getConnections(fromTextField.getText(), toTextField.getText());
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
