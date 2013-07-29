package eu.ydp.empiria.player.client.module.selection.view;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.peterfranza.gwt.jaxb.client.parser.utils.XMLContent;

import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import eu.ydp.empiria.player.client.module.selection.model.SelectionGridElementPosition;
import eu.ydp.empiria.player.client.module.selection.model.UserAnswerType;

public class SelectionModuleViewImpl implements SelectionModuleView{

	public SelectionModuleViewImpl() {};
	
	@Inject
	public SelectionModuleViewImpl(
			SelectionElementGenerator gridElementGenerator) {
		this.gridElementGenerator = gridElementGenerator;
	}
	
	private SelectionElementGenerator gridElementGenerator;

	private Map<SelectionGridElementPosition, SelectionButtonGridElement> buttonsGridMap = new HashMap<SelectionGridElementPosition, SelectionButtonGridElement>();
	private Map<SelectionGridElementPosition, SelectionItemGridElement> itemsGridMap = new HashMap<SelectionGridElementPosition, SelectionItemGridElement>();
	private Map<SelectionGridElementPosition, SelectionChoiceGridElement> choicesGridMap = new HashMap<SelectionGridElementPosition, SelectionChoiceGridElement>();

	@UiField
	Panel mainPanel;

	@UiField
	Widget promptWidget;

	@UiField
	Grid selectionGrid;

	@UiTemplate("SelectionModuleView.ui.xml")
	interface SelectionModuleUiBinder extends UiBinder<Widget, SelectionModuleViewImpl> {
	};

	@Override
	public void initialize(InlineBodyGeneratorSocket inlineBodyGeneratorSocket) {
		SelectionModuleUiBinder uiBinder = GWT.create(SelectionModuleUiBinder.class);
		uiBinder.createAndBindUi(this);
		gridElementGenerator.setInlineBodyGenerator(inlineBodyGeneratorSocket);
	}

	@Override
	public void setGridSize(int amountOfItems, int amountOfChoices) {
		selectionGrid.resize(amountOfItems + 1, amountOfChoices + 1);
	}
	
	@Override
	public void setItemDisplayedName(XMLContent itemName, SelectionGridElementPosition position){
		SelectionItemGridElement itemTextGridElement = gridElementGenerator.createItemDisplayElement(itemName.getValue());
		addToGrid(itemTextGridElement, position);
		itemsGridMap.put(position, itemTextGridElement);
	}

	@Override
	public void setChoiceOptionDisplayedName(XMLContent choiceName, SelectionGridElementPosition position){
		SelectionChoiceGridElement choiseTextGridElement = gridElementGenerator.createChoiceDisplayElement(choiceName.getValue());
		addToGrid(choiseTextGridElement, position);
		choicesGridMap.put(position, choiseTextGridElement);
	}

	@Override
	public void createButtonForItemChoicePair(SelectionGridElementPosition position, String moduleStyleName){
		SelectionButtonGridElement choiceButtonGridElement = gridElementGenerator.createChoiceButtonElement(moduleStyleName);
		addToGrid(choiceButtonGridElement, position);
		buttonsGridMap.put(position, choiceButtonGridElement);
	}
	
	@Override
	public void addClickHandlerToButton(SelectionGridElementPosition position, ClickHandler clickHandler){
		final SelectionButtonGridElement gridElement = buttonsGridMap.get(position);
		gridElement.addClickHandler(clickHandler);
	}

	@Override
	public void selectButton(SelectionGridElementPosition position){
		SelectionButtonGridElement gridElement = buttonsGridMap.get(position);
		gridElement.select();
	}

	@Override
	public void unselectButton(SelectionGridElementPosition position) {
		SelectionButtonGridElement gridElement = buttonsGridMap.get(position);
		gridElement.unselect();
	}

	@Override
	public void lockButton(SelectionGridElementPosition position, boolean lock){
		SelectionButtonGridElement gridElement = buttonsGridMap.get(position);
		gridElement.setButtonEnabled(!lock);
	}

	@Override
	public void updateButtonStyle(SelectionGridElementPosition position, UserAnswerType styleState){
		SelectionButtonGridElement gridElement = buttonsGridMap.get(position);
		gridElement.updateStyle(styleState);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}
	
	private <V extends SelectionGridElement> void addToGrid(V gridElement, SelectionGridElementPosition position) {
		selectionGrid.setWidget(position.getRowNumber(), position.getColumnNumber(), gridElement.asWidget());
	}
}