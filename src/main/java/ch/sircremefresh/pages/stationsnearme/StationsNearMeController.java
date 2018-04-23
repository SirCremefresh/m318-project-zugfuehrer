package ch.sircremefresh.pages.stationsnearme;

import ch.sircremefresh.location.LocationService;
import ch.sircremefresh.location.dto.LocationDto;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.StationDto;
import ch.sircremefresh.util.InfoBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class StationsNearMeController {
	private TransportService transportService = new TransportService();
	private LocationService locationService = new LocationService();

	private ObservableList<StationDto> stationsNearMe = FXCollections.observableArrayList();

	@FXML
	private TableView<StationDto> stationNearMeTableView;

	@FXML
	private TableColumn<StationDto, String> stationNearMeTableNameColumn;

	@FXML
	private TableColumn<StationDto, String> stationNearMeTableDistanceColumn;

	@FXML
	public void initialize() {
		stationNearMeTableView.setItems(stationsNearMe);

		stationNearMeTableNameColumn.prefWidthProperty().bind(stationNearMeTableView.widthProperty().divide(2));
		stationNearMeTableDistanceColumn.prefWidthProperty().bind(stationNearMeTableView.widthProperty().divide(2));

		stationNearMeTableNameColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getName()));
		stationNearMeTableDistanceColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getDistance() + " m"));
	}

	@FXML
	public void search() {
		LocationDto location;
		try {
			location = locationService.getLocation();
		} catch (Exception e) {
			InfoBox.show("something went wrong", "while getting location something went wrong", Alert.AlertType.ERROR);
			return;
		}

		List<StationDto> stations;
		try {
			stations = transportService.getStationsNear(location.getLongitude(), location.getLatitude());
		} catch (Exception e) {
			InfoBox.show("something went wrong", "while getting stations near you", Alert.AlertType.ERROR);
			return;
		}
		stationsNearMe.clear();
		stationsNearMe.addAll(stations);
	}

}
