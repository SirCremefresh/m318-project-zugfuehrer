package ch.sircremefresh.controls.actionButton;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ActionButtonController extends Button {

	public ActionButtonController() {
		addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				ActionEvent actionEvent = new ActionEvent();
				this.getOnAction().handle(actionEvent);
			}
		});
	}

}
