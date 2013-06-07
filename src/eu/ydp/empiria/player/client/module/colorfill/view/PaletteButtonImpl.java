package eu.ydp.empiria.player.client.module.colorfill.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import eu.ydp.empiria.player.client.module.colorfill.model.ColorModel;
import eu.ydp.empiria.player.client.resources.StyleNameConstants;

public class PaletteButtonImpl extends Composite implements PaletteButton {

	private static PaletteButtonUiBinder uiBinder = GWT.create(PaletteButtonUiBinder.class);

	@UiTemplate("PaletteButton.ui.xml")
	interface PaletteButtonUiBinder extends UiBinder<Widget, PaletteButtonImpl> {}

	@UiField
	PushButton button;
	
	@UiField
	SimplePanel container;
	
	@Inject
	private StyleNameConstants styleNameConstants;

	public PaletteButtonImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setColor(ColorModel color) {
		container.addStyleDependentName(color.toStringRgba());
	}

	@Override
	public void select() {
		container.addStyleName(styleNameConstants.QP_COLORFILL_PALETTE_BUTTON_CONTAINER_SELECTED());
	}

	@Override
	public void deselect() {
		container.removeStyleName(styleNameConstants.QP_COLORFILL_PALETTE_BUTTON_CONTAINER_SELECTED());
	}

}