package ch.sircremefresh;

import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.StationDto;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import lombok.val;

import java.util.List;

public class Controller {
	private TransportService transportService = new TransportService();

	private ObservableList<StationDto> fromHints = FXCollections.observableArrayList();
	private ObservableList<StationDto> toHints = FXCollections.observableArrayList();

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
	public void initialize() {
		initializeStationSearcherField(fromTextField, fromHintsListView, fromHints);
		initializeStationSearcherField(toTextField, toHintsListView, toHints);
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
			hints.addAll(stations);
		});

		textField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
			listView.visibleProperty().setValue(newPropertyValue || listView.focusedProperty().getValue());
		});
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
	public void updateSearchHints() {
		val result = transportService.getConntection(fromTextField.getText(), toTextField.getText());
		System.out.println(result);
	}

	private void showInfoBox(String title, String msg, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}
