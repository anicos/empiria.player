package eu.ydp.empiria.player.client.module.slideshow.presenter;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import eu.ydp.empiria.player.client.module.slideshow.structure.*;
import eu.ydp.empiria.player.client.module.slideshow.view.slide.SlideView;
import eu.ydp.gwtutil.client.gin.scopes.module.ModuleScoped;

public class SlidePresenter {

	private final SlideView view;
	private InlineBodyGeneratorSocket inlineBodyGeneratorSocket;

	@Inject
	public SlidePresenter(@ModuleScoped SlideView view) {
		this.view = view;
	}

	public void replaceViewData(SlideBean slide) {
		view.clearTitle();
		view.clearNarration();

		if (slide.hasSlideTitle()) {
			Element title = slide.getSlideTitle().getTitleValue().getValue();
			setTitle(title);
		}

		if (slide.hasNarration()) {
			Element narration = slide.getNarration().getNarrationValue().getValue();
			setNarration(narration);
		}

		SourceBean source = slide.getSource();
		setSource(source);
	}

	private void setSource(SourceBean source) {
		String src = source.getSrc();
		if (!Strings.isNullOrEmpty(src)) {
			view.setImage(src);
		}
	}

	private void setTitle(Element title) {
		Widget titleView = getWidgetFromElement(title);
		view.setSlideTitle(titleView);
	}

	private void setNarration(Element narration) {
		Widget narrationView = getWidgetFromElement(narration);
		view.setNarration(narrationView);
	}

	private Widget getWidgetFromElement(Element element) {
		return inlineBodyGeneratorSocket.generateInlineBody(element);
	}

	public void setInlineBodyGenerator(InlineBodyGeneratorSocket inlineBodyGeneratorSocket) {
		this.inlineBodyGeneratorSocket = inlineBodyGeneratorSocket;
	}
}
