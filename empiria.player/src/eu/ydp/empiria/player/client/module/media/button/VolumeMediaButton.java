package eu.ydp.empiria.player.client.module.media.button;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Panel;

/**
 * przycisk zmiany glosnosci dzwieku
 *
 * @author plelakowski
 *
 */
public class VolumeMediaButton extends AbstractMediaButton<VolumeMediaButton> {
	public VolumeMediaButton() {
		super("qp-media-volume");
	}

	VolumeScrollBar volumeScrollBar = new VolumeScrollBar();

	@Override
	public VolumeMediaButton getNewInstance() {
		return new VolumeMediaButton();
	}

	@Override
	public void init() {
		if (isSupported()) {
			volumeScrollBar.setMedia(getMedia());
			volumeScrollBar.init();
			volumeScrollBar.setVisible(false);

		}
	}
	boolean attached = false;
	@Override
	protected void onClick() {
		if(!attached){
			volumeScrollBar.getElement().getStyle().setPosition(Position.ABSOLUTE);
			((Panel)getParent()).add(volumeScrollBar);
			attached = true;
		}
		super.onClick();
		if (clicked) {
			volumeScrollBar.setVisible(true);
			int width = volumeScrollBar.getElement().getAbsoluteRight()-volumeScrollBar.getElement().getAbsoluteLeft();
			width = getElement().getAbsoluteRight()-getElement().getAbsoluteLeft()-width;
			volumeScrollBar.getElement().getStyle().setLeft(getElement().getAbsoluteLeft()+width/2, Unit.PX);
			int  height = volumeScrollBar.getElement().getAbsoluteBottom()-volumeScrollBar.getElement().getAbsoluteTop();

			volumeScrollBar.getElement().getStyle().setTop(getElement().getAbsoluteTop()-height, Unit.PX);

		}else{
			volumeScrollBar.setVisible(false);
		}
	}
}
