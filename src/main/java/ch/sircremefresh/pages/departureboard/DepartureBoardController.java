package ch.sircremefresh.pages.departureboard;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.ConnectionDto;
import ch.sircremefresh.transport.dto.StationboardEntryDto;
import ch.sircremefresh.util.StationSearchAutoComplete;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.val;

public class DepartureBoardController {
	private TransportService transportService = new TransportService();

	private ObservableList<StationboardEntryDto> stationboardEntries = FXCollections.observableArrayList();

	@FXML
	private TableView<StationboardEntryDto> stationBoardTableView;

	@FXML
	private TableColumn<StationboardEntryDto, String> stationBoardTableFromColumn;

	@FXML
	private TableColumn<StationboardEntryDto, String> stationBoardTableToColumn;

	@FXML
	private TableColumn<StationboardEntryDto, String> stationBoardTableDepartureColumn;


	@FXML
	private AutoCompleteController stationSearchAutoComplete;

	@FXML
	public void initialize() {
		StationSearchAutoComplete.setupAutoComplete(stationSearchAutoComplete, transportService);
		initializeStationBoardTable();
	}

	@FXML
	public void showStationBoard() {
		val stationBoard = transportService.getStationboard(stationSearchAutoComplete.getText());
		stationboardEntries.clear();
		stationboardEntries.addAll(stationBoard.getStationboard());
	}

	private void initializeStationBoardTable() {
		stationBoardTableView.setItems(stationboardEntries);
		stationBoardTableFromColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getStop().getStation().getName()));
		stationBoardTableToColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo()));
		stationBoardTableDepartureColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getStop().getDeparture()));
	}
}
