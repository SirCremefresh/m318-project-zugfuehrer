package ch.sircremefresh.util;

import javafx.scene.control.Alert;

public class InfoBox {
	public static void show(String title, String msg, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}
}
