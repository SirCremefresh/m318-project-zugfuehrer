package ch.sircremefresh.controls.autocomplete;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.LabeledText;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AutoCompleteController extends AnchorPane {
	private EventHandler<KeyEvent> onEnter = null;
	private Runnable onAction = null;

	private ObservableList<String> hints = FXCollections.observableArrayList();

	private Callback<ListView<String>, ListCell<String>> cellFactory = (ListView<String> p) -> new ListCell<String>() {
		@Override
		protected void updateItem(String t, boolean bln) {
			super.updateItem(t, bln);

			if (bln || t == null) {
				setGraphic(null);
			} else {
				Text t2 = new Text(t);
				setGraphic(t2);
			}
		}
	};

	@FXML
	private TextField textField;

	@FXML
	private ListView<String> listView;

	public AutoCompleteController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"autocomplete.fxml"));
		URL cssUrl = this.getClass().getResource("main.css");
		String css = cssUrl.toExternalForm();
		this.getStylesheets().add(css);
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	public void initialize() {
		listView.setCellFactory(cellFactory);
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

		listView.focusedProperty().addListener((arg0, oldVal, newVal) -> {
			((BehaviorSkinBase) listView.getSkin()).getBehavior().traverseNext();
		});

		textField.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
			switch (keyEvent.getCode()) {
				case UP:
					if (listView.getSelectionModel().getSelectedItem() == null)
						listView.getSelectionModel().selectLast();
					else
						listView.getSelectionModel().selectPrevious();
					keyEvent.consume();
					break;
				case DOWN:
					if (listView.getSelectionModel().getSelectedItem() == null)
						listView.getSelectionModel().selectFirst();
					else
						listView.getSelectionModel().selectNext();
					keyEvent.consume();
					break;
				case ENTER:
					if (listView.getSelectionModel().getSelectedItem() != null) {
						textField.setText(listView.getSelectionModel().getSelectedItem());
						setFocusOnTextField(textField);
					} else {
						if (onAction != null) {
							onAction.run();
						}
					}
					break;
				case TAB:
					listView.visibleProperty().setValue(false);
			}
		});



		textField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
			listView.visibleProperty().setValue(newPropertyValue || listView.focusedProperty().getValue());
		});
	}

	private void setFocusOnTextField(final TextField textField) {
		textField.requestFocus();
		textField.positionCaret(textField.getLength());
	}

	public void setHints(List<String> newHints) {
		hints.clear();
		hints.addAll(newHints);
		listView.applyCss();
	}

	public void setOnEnter(EventHandler<KeyEvent> handler) {
		this.onEnter = handler;
	}

	public void setOnAction(Runnable onAction) {
		this.onAction = onAction;
	}

	public String getText() {
		return textField.getText();
	}

	public void setText(String newText) {
		textField.setText(newText);
	}

	public StringProperty getTextProperty() {
		return textField.textProperty();
	}
}
