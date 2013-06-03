package eu.ydp.empiria.player.client.module.colorfill.view;

import com.google.gwt.user.client.ui.IsWidget;

import eu.ydp.empiria.player.client.module.colorfill.model.ColorModel;

public interface ColorfillPalette extends IsWidget {
	void createButton(ColorModel color);
	void selectButton(ColorModel color);
	void deselectButton(ColorModel color);
	void setButtonClickListener(ColorfillButtonClickListener listener);
}
