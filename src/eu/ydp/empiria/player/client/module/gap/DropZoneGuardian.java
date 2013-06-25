package eu.ydp.empiria.player.client.module.gap;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;

import eu.ydp.empiria.player.client.resources.StyleNameConstants;
import eu.ydp.empiria.player.client.util.dom.drag.DroppableObject;

public class DropZoneGuardian {

	private final DroppableObject<TextBox> droppable;	
	private boolean dropZoneLocked = false;
	protected Panel moduleWidget;
	private StyleNameConstants styleNames;
	
	public DropZoneGuardian(DroppableObject<TextBox> droppable, Panel moduleWidget, StyleNameConstants styleNameConstants) {
		this.droppable = droppable;
		this.moduleWidget  = moduleWidget;
		this.styleNames = styleNameConstants;
	}
	
	public void lockDropZone() {
		dropZoneLocked = true;
		droppable.setDisableDrop(true);
		moduleWidget.addStyleName(styleNames.LOCKED_DROP_ZONE_STYLE());
	}

	public void unlockDropZone() {
		dropZoneLocked = false;
		droppable.setDisableDrop(false);
		moduleWidget.removeStyleName(styleNames.LOCKED_DROP_ZONE_STYLE());
	}

	public boolean isDropZoneLocked() {
		return dropZoneLocked;
	}
	
}
