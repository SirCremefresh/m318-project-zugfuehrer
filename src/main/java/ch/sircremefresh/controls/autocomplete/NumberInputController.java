package ch.sircremefresh.controls.autocomplete;

import javafx.scene.control.TextField;

public class NumberInputController extends TextField {
	private int maxSize = 0;

	public NumberInputController() {
		this.setOnKeyTyped((keyEvent) -> {
			if (!isStringOfDigits(keyEvent.getCharacter()) ||
					keyEvent.getCharacter().length() + getText().length() > maxSize)
				keyEvent.consume();
		});
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	private boolean isStringOfDigits(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
}
