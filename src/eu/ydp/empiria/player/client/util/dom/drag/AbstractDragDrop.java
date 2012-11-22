package eu.ydp.empiria.player.client.util.dom.drag;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import eu.ydp.empiria.player.client.gin.factory.PageScopeFactory;
import eu.ydp.empiria.player.client.module.IModule;
import eu.ydp.empiria.player.client.overlaytypes.OverlayTypesParser;
import eu.ydp.empiria.player.client.util.events.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.dragdrop.DragDropEvent;
import eu.ydp.empiria.player.client.util.events.dragdrop.DragDropEventTypes;
import eu.ydp.gwtutil.client.debug.logger.Debug;

public abstract class AbstractDragDrop<W extends Widget> {
	@Inject
	protected EventsBus eventsBus;

	@Inject
	protected OverlayTypesParser overlayTypesParser;

	@Inject
	protected PageScopeFactory scopeFactory;

	protected boolean valueChangeSelfFire = false;

	protected void fireEvent(DragDropEventTypes type, DragDataObject dataObject) {
		if (type != null && dataObject != null) {
			DragDropEvent dragDropEvent = new DragDropEvent(type, getIModule());
			dragDropEvent.setDragDataObject(dataObject);
			Debug.log(type + " " + dataObject.toJSON());
			eventsBus.fireEventFromSource(dragDropEvent, getIModule(), scopeFactory.getCurrentPageScope());
		}
	}

	protected void registerDropZone() {
		DragDropEvent event = new DragDropEvent(DragDropEventTypes.REGISTER_DROP_ZONE, getIModule());
		event.setIModule(getIModule());
		eventsBus.fireEventFromSource(event, getIModule(), scopeFactory.getCurrentPageScope());
	}

	public HasValueChangeHandlers<?> findHasValueChangeHandlers(Widget widget) {
		HasValueChangeHandlers<?> returnValue = null;
		if (widget instanceof HasValueChangeHandlers) {
			returnValue = (HasValueChangeHandlers<?>) widget;
		} else {
			if (widget instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) widget;
				for (Widget wid : hasWidgets) {
					returnValue = findHasValueChangeHandlers(wid);
					if (returnValue != null) {
						break;
					}
				}
			}
		}
		return returnValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setAutoBehaviorForDrop(boolean disableAutoBehavior) {
		registerDropZone();
		HasValueChangeHandlers widget = findHasValueChangeHandlers(getOriginalWidget());
		if (widget != null) {
			widget.addValueChangeHandler(new ValueChangeHandler() {
				@Override
				public void onValueChange(ValueChangeEvent event) {
					if (valueChangeSelfFire) {
						valueChangeSelfFire = false;
					} else {
						String value = String.valueOf(event.getValue());
						NativeDragDataObject object = overlayTypesParser.get();
						object.setValue(value);
						fireEvent(DragDropEventTypes.DRAG_END, object);
					}
				}
			});
		}
	}

	/**
	 * Szuka w hierarchi widgetu posiadajacego wartosc
	 *
	 * @param widget
	 * @return
	 */
	public HasValue<?> findHasValue(Widget widget) {
		HasValue<?> returnValue = null;
		if (widget instanceof HasValue) {
			returnValue = (HasValue<?>) widget;
		} else {
			if (widget instanceof HasWidgets) {
				HasWidgets hasWidgets = (HasWidgets) widget;
				for (Widget wid : hasWidgets) {
					returnValue = findHasValue(wid);
					if (returnValue != null) {
						break;
					}
				}
			}
		}
		return returnValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String putValue(String jsonObject) {
		String value = null;
		HasValue widget = findHasValue(getOriginalWidget());
		if (widget != null && overlayTypesParser.isValidJSON(jsonObject)) {
			NativeDragDataObject dragData = overlayTypesParser.get(jsonObject);
			value = dragData.getValue();
			if (!widget.getValue().equals(value)) {
				valueChangeSelfFire = true;
				widget.setValue(value, true);
				fireEvent(DragDropEventTypes.DRAG_END, dragData);
			}
		}
		return value;
	}

	protected abstract W getOriginalWidget();

	protected abstract IModule getIModule();
}
