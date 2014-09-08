package eu.ydp.empiria.player.client.module.img.events.coordinates;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;

import eu.ydp.empiria.player.client.module.img.events.handlers.touchonimage.TouchOnImageEvent;
import eu.ydp.empiria.player.client.util.events.dom.emulate.events.pointer.PointerEvent;
import eu.ydp.empiria.player.client.util.position.Point;

public class PointerEventsCoordinates {

	private final Map<Long, Point> pointersPoints = new LinkedHashMap<Long, Point>();

	public void addEvent(PointerEvent<? extends EventHandler> pointerEvent) {
		if (isFirstTouch(pointerEvent)) {
			pointersPoints.clear();
		}
		Point coordinates = new Point(pointerEvent.getClientX(), pointerEvent.getClientY());
		pointersPoints.put(pointerEvent.getPointerId(), coordinates);
	}

	public Point getPoint(int index) {
		return (Point) pointersPoints.values().toArray()[index];
	}

	public void removeEvent(PointerEvent<?> pointerEvent) {
		pointersPoints.remove(pointerEvent.getPointerId());
	}

	public int getLength() {
		return pointersPoints.size();
	}

	public TouchOnImageEvent getTouchOnImageEvent() {
		return new TouchOnImageEvent(new ArrayList<Point>(pointersPoints.values()));
	}

	private boolean isFirstTouch(PointerEvent<? extends EventHandler> pointerEvent) {
		return pointerEvent.isPrimary() && !pointersPoints.containsKey(pointerEvent.getPointerId());
	}
}
