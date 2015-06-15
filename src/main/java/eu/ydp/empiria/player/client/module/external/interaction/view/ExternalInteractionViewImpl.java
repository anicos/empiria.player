package eu.ydp.empiria.player.client.module.external.interaction.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import eu.ydp.empiria.player.client.module.external.common.ExternalFrameLoadHandler;
import eu.ydp.empiria.player.client.module.external.common.view.ExternalFrame;
import eu.ydp.empiria.player.client.module.external.interaction.api.ExternalInteractionApi;
import eu.ydp.empiria.player.client.module.external.interaction.api.ExternalInteractionEmpiriaApi;

public class ExternalInteractionViewImpl extends Composite implements ExternalInteractionView {

	private static ExternalInteractionViewUiBinder uiBinder = GWT.create(ExternalInteractionViewUiBinder.class);

	@UiTemplate("ExternalInteractionView.ui.xml") interface ExternalInteractionViewUiBinder extends UiBinder<Widget, ExternalInteractionViewImpl> {
	}

	@UiField
	ExternalFrame<ExternalInteractionApi> frame;

	public ExternalInteractionViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void init(ExternalInteractionEmpiriaApi api, ExternalFrameLoadHandler<ExternalInteractionApi> onLoadHandler, String url) {
		frame.init(api, onLoadHandler);
		frame.setUrl(url);
	}
}
