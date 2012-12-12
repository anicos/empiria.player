package eu.ydp.empiria.player.client.module.selection.view;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;

import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import eu.ydp.empiria.player.client.module.selection.model.UserAnswerType;

public interface SelectionModuleView extends IsWidget{

	void initialize(int amountOfItems, int amountOfChoices, InlineBodyGeneratorSocket inlineBodyGeneratorSocket);

	void setItemDisplayedName(String itemName, int itemNumber);

	void setChoiceOptionDisplayedName(String choiceName, int choiceNumber);

	void createButtonForItemChoicePair(int itemNumber, int choiceNumber, String moduleStyleName);

	void selectButton(int itemNumber, int choiceNumber);

	void unselectButton(int itemNumber, int choiceNumber);
	
	void lockButton(boolean lock, int itemNumber, int choiceNumber);

	void updateButtonStyle(int itemNumber, int choiceNumber, UserAnswerType styleState);

	void addClickHandlerToButton(int itemNumber, int choiceNumber, ClickHandler clickHandler);
}