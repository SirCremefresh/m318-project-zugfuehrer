package ch.sircremefresh.pages.departureboard;

import ch.sircremefresh.controls.autocomplete.AutoCompleteController;
import ch.sircremefresh.transport.TransportApiException;
import ch.sircremefresh.transport.TransportService;
import ch.sircremefresh.transport.dto.StationboardDto;
import ch.sircremefresh.transport.dto.StationboardEntryDto;
import ch.sircremefresh.util.InfoBox;
import ch.sircremefresh.util.StationSearchAutoComplete;
import ch.sircremefresh.util.TimeFormatter;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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
		StationboardDto stationBoard;
		try {
			stationBoard = transportService.getStationboard(stationSearchAutoComplete.getText());

		} catch (TransportApiException e) {
			InfoBox.show("error while getting station board", e.getMessage(), Alert.AlertType.ERROR);
			return;
		} catch (Throwable e) {
			InfoBox.show("something went wrong", "while getting stationboard", Alert.AlertType.ERROR);
			return;
		}
		stationboardEntries.clear();
		stationboardEntries.addAll(stationBoard.getStationboard());
	}

	private void initializeStationBoardTable() {
		stationBoardTableView.focusedProperty().addListener((arg0, oldVal, newVal) -> {
			((BehaviorSkinBase) stationBoardTableView.getSkin()).getBehavior().traverseNext();
		});

		stationBoardTableView.setItems(stationboardEntries);

		stationBoardTableFromColumn.prefWidthProperty().bind(stationBoardTableView.widthProperty().divide(3));
		stationBoardTableToColumn.prefWidthProperty().bind(stationBoardTableView.widthProperty().divide(3));
		stationBoardTableDepartureColumn.prefWidthProperty().bind(stationBoardTableView.widthProperty().divide(3));

		stationBoardTableFromColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getStop().getStation().getName()));
		stationBoardTableToColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> cd.getValue().getTo()));
		stationBoardTableDepartureColumn.setCellValueFactory(cd -> Bindings.createStringBinding(() -> TimeFormatter.getFormattedTime(cd.getValue().getStop().getDeparture())));
	}
}
