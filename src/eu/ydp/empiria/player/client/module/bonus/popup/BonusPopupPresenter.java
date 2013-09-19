package eu.ydp.empiria.player.client.module.bonus.popup;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import eu.ydp.empiria.player.client.components.animation.swiffy.SwiffyObject;
import eu.ydp.empiria.player.client.module.EndHandler;
import eu.ydp.gwtutil.client.util.geom.Size;

public class BonusPopupPresenter {

	@Inject
	private BonusPopupView view;
	private Optional<EndHandler> lastEndHandler;
	
	public void showImage(String url, Size size){
		view.showImage(url, size);
		view.attachToRoot();
	}

	public void showAnimation(SwiffyObject swiffy, Size size, EndHandler endHandler){
		lastEndHandler = Optional.fromNullable(endHandler);
		IsWidget widget = swiffy.getWidget();
		view.setAnimationWidget(widget, size);
		view.attachToRoot();
		swiffy.start();
	}
	
	public void closeClicked(){
		view.reset();
		
		if(lastEndHandler.isPresent()) {
			lastEndHandler.get().onEnd();
		}
	}
}
