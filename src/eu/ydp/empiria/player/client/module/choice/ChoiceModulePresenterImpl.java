package eu.ydp.empiria.player.client.module.choice;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import eu.ydp.empiria.player.client.module.choice.structure.SimpleChoiceBean;
import eu.ydp.empiria.player.client.module.components.choicebutton.ChoiceGroupController;

public class ChoiceModulePresenterImpl implements ChoiceModulePresenter {

	@UiTemplate("ChoiceModuleView.ui.xml")
	interface ChoiceModuleUiBinder extends UiBinder<Widget, ChoiceModulePresenterImpl> {
	};

	private final ChoiceModuleUiBinder uiBinder = GWT.create(ChoiceModuleUiBinder.class);

	@UiField
	Panel mainPanel;

	@UiField
	Widget promptWidget;

	@UiField
	Panel choicesPanel;

	private List<SimpleChoiceView> choiceViews;

	private InlineBodyGeneratorSocket bodyGenerator;

	@Override
	public void bindView() {
		uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void setInlineBodyGenerator(InlineBodyGeneratorSocket bodyGenerator) {
		this.bodyGenerator = bodyGenerator;
	}

	@Override
	public void setPrompt(String value) {
		bodyGenerator.generateInlineBody(value, promptWidget.getElement());
	}

	@Override
	public void setChoices(List<SimpleChoiceBean> choices) {
		choicesPanel.clear();

		choiceViews = new ArrayList<SimpleChoiceView>();
		ChoiceGroupController groupController = new ChoiceGroupController();

		for (SimpleChoiceBean choice : choices) {
			SimpleChoiceView choiceView = new SimpleChoiceView(choice, groupController, bodyGenerator);
			choiceViews.add(choiceView);
			choicesPanel.add(choiceView.asWidget());
		}
	}
	
	public void showAnswers(List<String> answers) {
		for (SimpleChoiceView choice : choiceViews) {
			boolean select = answers.contains(choice.getIdentifier());
			choice.setSelected(select);
		}
	}

	@Override
	public void setLocked(boolean locked) {
		for (SimpleChoiceView choice : choiceViews) {
			choice.setLocked(locked);
		}
	}

	@Override
	public void reset() {
		for (SimpleChoiceView choice : choiceViews) {
			choice.reset();
		}
	}

	@Override
	public void switchChoiceSelection(String identifier) {
		for (SimpleChoiceView choice : choiceViews) {
			if (identifier.equals(choice.getIdentifier())) {
				choice.setSelected(!choice.isSelected());
			}
		}
	}

	@Override
	public boolean isChoiceSelected(String identifier) {
		boolean selected = false;

		try {
			selected = getChoiceByIdentifier(identifier).isSelected();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return selected;
	}

	private SimpleChoiceView getChoiceByIdentifier(String identifier) {
		SimpleChoiceView searchedChoice = null;

		for (SimpleChoiceView choice : choiceViews) {
			if (identifier.equals(choice.getIdentifier())) {
				searchedChoice = choice;
				break;
			}
		}

		return searchedChoice;
	}

	@Override
	public void markCorrectAnswers() {
		for (SimpleChoiceView choice : choiceViews) {
			// boolean markCorrect =
			// correctAnswers.contains(choice.getIdentifier());
			// choice.markAnswers(mark, markCorrect);
		}
	}
	
	@Override
	public void markWrongAnswers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unmarkCorrectAnswers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unmarkWrongAnswers() {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget getFeedbackPlaceholderByIdentifier(String identifier) {
		Widget placeholder = null;

		for (SimpleChoiceView choice : choiceViews) {
			if (identifier.equals(choice.getIdentifier())) {
				placeholder = choice.getFeedbackPlaceHolder();
				break;
			}
		}

		return placeholder;
	}

	@Override
	public void showCorrectAnswers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showCurrentAnswers() {
		// TODO Auto-generated method stub
		
	}
}
