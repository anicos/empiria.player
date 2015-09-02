package eu.ydp.empiria.player.client.module.feedback.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.controller.feedback.FeedbackTypeStyleProvider;
import eu.ydp.empiria.player.client.module.feedback.FeedbackStyleNameConstants;
import eu.ydp.gwtutil.client.ui.button.CustomPushButton;

public class TextFeedbackPresenter extends Composite implements TextFeedback {

    private static TextFeedbackViewUiBinder uiBinder = GWT.create(TextFeedbackViewUiBinder.class);

    @Inject
    private FeedbackStyleNameConstants feedbackStyleNameConstants;
    @Inject
    private FeedbackTypeStyleProvider typeStyleProvider;

    @UiTemplate("TextFeedbackView.ui.xml")
    interface TextFeedbackViewUiBinder extends UiBinder<Widget, TextFeedbackPresenter> {
    }

    public TextFeedbackPresenter() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    FlowPanel feedbackTextPanel;
    @UiField
    CustomPushButton feedbackCloseButton;

    @Override
    public void show(Widget widget) {
        addNewFeedback(widget);
        clearStyleNames();
        addStyleName(typeStyleProvider.getStyleName());
    }

    @Override
    public void hide() {
        addStyleName(feedbackStyleNameConstants.QP_FEEDBACK_TEXT_MODULE_HIDDEN());
    }

    @Override
    public void addCloseButtonClickHandler(ClickHandler handler) {
        feedbackCloseButton.addClickHandler(handler);
    }

    private void addNewFeedback(Widget widget) {
        feedbackTextPanel.clear();
        feedbackTextPanel.add(widget);
    }

    private void clearStyleNames() {
        removeStyleName(feedbackStyleNameConstants.QP_FEEDBACK_ALLOK());
        removeStyleName(feedbackStyleNameConstants.QP_FEEDBACK_OK());
        removeStyleName(feedbackStyleNameConstants.QP_FEEDBACK_WRONG());
        removeStyleName(feedbackStyleNameConstants.QP_FEEDBACK_TEXT_MODULE_HIDDEN());
    }
}
