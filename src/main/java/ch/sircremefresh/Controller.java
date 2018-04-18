package ch.sircremefresh;

import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.StationDto;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import lombok.val;

import java.util.List;

public class Controller {
	private TransportService transportService = new TransportService();

	private ObservableList<StationDto> fromHints = FXCollections.observableArrayList();

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
	public void initialize() {
		initializeFromTextField();
	}

	private void initializeFromTextField() {
		fromHintsListView.setCellFactory(StationDtoCellFactory);
		fromHintsListView.setItems(fromHints);

		fromHintsListView.setOnMouseClicked((mouseEvent) -> {
			EventTarget target = mouseEvent.getTarget();
			String station;
			if (target instanceof ListCell && ((ListCell) target).getText() != null) {
				station = ((ListCell) target).getText();
			} else if (target instanceof LabeledText) {
				station = ((LabeledText) target).getText();
			} else {
				fromHintsListView.visibleProperty().setValue(false);
				return;
			}
			fromTextField.setText(station);
			setFocusOnTextField(fromTextField);
		});

		fromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			List<StationDto> stations = transportService.getStations(newValue);
			fromHints.clear();
			fromHints.addAll(stations);
		});

		fromTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) -> {
			fromHintsListView.visibleProperty().setValue(newPropertyValue || fromHintsListView.focusedProperty().getValue());
		});
	}

	private StationDto getStationWithNameFromList(String stationName, List<StationDto> list) {
		for(val item: list) {
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
		val station = new StationDto();
		station.setName("dd");
		fromHints.add(station);
	}
}
