package ch.sircremefresh;

import ch.sircremefresh.pages.departureboard.DepartureBoardController;
import ch.sircremefresh.pages.timetable.TimetableController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {

	@FXML
	private TimetableController timetableController;

	@FXML
	private AnchorPane timetable;

	@FXML
	private DepartureBoardController departureBoardController;

	@FXML
	private AnchorPane departureBoard;

	@FXML
	public void initialize() {
		System.out.println("started");
	}
}
