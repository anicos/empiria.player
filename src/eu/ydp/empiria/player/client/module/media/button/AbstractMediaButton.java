package eu.ydp.empiria.player.client.module.media.button;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;

import eu.ydp.empiria.player.client.module.Factory;

/**
 * bazowy przycisk dla kontrolerow multimediow
 *
 * @param <T>
 *            typ przycisku dla {@link Factory}
 */
public abstract class AbstractMediaButton<T> extends MediaController<T> {
	private String baseStyleName;
	private String onClickStyleName;
	private String hoverStyleName;
	protected boolean clicked = false;
	private FlowPanel divElement = new FlowPanel();
	private boolean singleClick = true;


	/**
	 * bazowy przycisk dla kontrolerow multimediow
	 *
	 * @param baseStyleName
	 * @param singleClick
	 *            czy element jest zwyklym przyciskiem i mousup jest ignorowany
	 *            wartosc true<br/>
	 *            false wywoluje ponownie akcje na mouseup
	 */
	public AbstractMediaButton(String baseStyleName, boolean singleClick) {
		this.baseStyleName = baseStyleName;
		this.onClickStyleName = baseStyleName + clickSuffix;
		this.hoverStyleName = baseStyleName + hoverSuffx;
		this.singleClick = singleClick;
		initWidget(divElement);

	}

	@Override
	public void init() {
		if (isSupported()) {
			sinkEvents(Event.MOUSEEVENTS | Event.TOUCHEVENTS);
			this.setStyleName(this.baseStyleName);
		} else {
			this.setStyleName(this.baseStyleName + unsupportedSuffx);
		}

	}

	public AbstractMediaButton(String baseStyleName) {
		this(baseStyleName, true);
	}

	@Override
	public void onBrowserEvent(Event event) {
		event.preventDefault();
		switch (event.getTypeInt()) {
		case Event.ONMOUSEDOWN:
		case Event.ONTOUCHSTART:
			onClick();
			break;
		case Event.ONMOUSEUP:
			if (!singleClick) {
				onClick();
			}
			break;
		case Event.ONMOUSEOVER:
			onMouseOver();
			break;
		case Event.ONTOUCHEND:
		case Event.ONMOUSEOUT:
			onMouseOut();
			break;
		}
	}

	/**
	 * zdarzenie click
	 */
	protected void onClick() {
		if (hoverStyleName.trim().length() == 0) {
			return;
		}
		changeStyleForClick();
		clicked = !clicked;
	}

	/**
	 * zmiana stylu elementu dla zdarzenia click
	 */
	protected void changeStyleForClick() {
		if (!clicked) {
			divElement.getElement().addClassName(onClickStyleName);
		} else {
			divElement.getElement().removeClassName(onClickStyleName);
		}

	}

	/**
	 * zmiana stylu elementu dla zdarzenia onMouseOver
	 */
	protected void onMouseOver() {
		if (hoverStyleName.trim().length() > 0) {
			divElement.getElement().addClassName(hoverStyleName);
		}
	}

	/**
	 * zmiana stylu elementu dla zdarzenia onMouseOut
	 */
	void onMouseOut() {
		if (hoverStyleName.trim().length() > 0) {
			divElement.getElement().removeClassName(hoverStyleName);
		}
	}
}
