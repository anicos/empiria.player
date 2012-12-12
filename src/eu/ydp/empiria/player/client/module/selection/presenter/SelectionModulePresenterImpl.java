package eu.ydp.empiria.player.client.module.selection.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import eu.ydp.empiria.player.client.gin.factory.SelectionModuleFactory;
import eu.ydp.empiria.player.client.module.MarkAnswersMode;
import eu.ydp.empiria.player.client.module.MarkAnswersType;
import eu.ydp.empiria.player.client.module.ModuleSocket;
import eu.ydp.empiria.player.client.module.ShowAnswersType;
import eu.ydp.empiria.player.client.module.selection.SelectionModuleModel;
import eu.ydp.empiria.player.client.module.selection.controller.GroupAnswersController;
import eu.ydp.empiria.player.client.module.selection.controller.IdentifiableAnswersByTypeFinder;
import eu.ydp.empiria.player.client.module.selection.controller.SelectionModuleViewBuildingController;
import eu.ydp.empiria.player.client.module.selection.controller.SelectionModuleViewUpdatingController;
import eu.ydp.empiria.player.client.module.selection.model.SelectionAnswerDto;
import eu.ydp.empiria.player.client.module.selection.model.UserAnswerType;
import eu.ydp.empiria.player.client.module.selection.structure.SelectionInteractionBean;
import eu.ydp.empiria.player.client.module.selection.structure.SelectionItemBean;
import eu.ydp.empiria.player.client.module.selection.structure.SelectionSimpleChoiceBean;
import eu.ydp.empiria.player.client.module.selection.view.SelectionModuleView;

public class SelectionModulePresenterImpl implements SelectionModulePresenter{

	private SelectionModuleModel model;
	private SelectionInteractionBean bean;
	private ModuleSocket moduleSocket;
	private List<GroupAnswersController> groupChoicesControllers;

	private SelectionModuleView selectionModuleView;
	private SelectionModuleFactory selectionModuleFactory;
	private IdentifiableAnswersByTypeFinder identifiableAnswersByTypeFinder;
	private SelectionModuleViewUpdatingController selectionModuleViewUpdatingController;
	private SelectionModuleViewBuildingController viewBuildingController;
	
	
	@Inject
	public SelectionModulePresenterImpl(
			SelectionModuleView selectionModuleView, 
			SelectionModuleFactory selectionModuleFactory, 
			IdentifiableAnswersByTypeFinder identifiableAnswersByTypeFinder,
			SelectionModuleViewUpdatingController selectionModuleViewUpdatingController) {
		this.selectionModuleView = selectionModuleView;
		this.selectionModuleFactory = selectionModuleFactory;
		this.identifiableAnswersByTypeFinder = identifiableAnswersByTypeFinder;
		this.selectionModuleViewUpdatingController = selectionModuleViewUpdatingController;
	}

	@Override
	public void bindView() {
		List<SelectionItemBean> items = bean.getItems();
		List<SelectionSimpleChoiceBean> simpleChoices = bean.getSimpleChoices();
		InlineBodyGeneratorSocket inlineBodyGeneratorSocket = moduleSocket.getInlineBodyGeneratorSocket();
		
		selectionModuleView.initialize(items.size(), simpleChoices.size(), inlineBodyGeneratorSocket);
		viewBuildingController = selectionModuleFactory.createViewBuildingController(selectionModuleView, this, model, bean);
		
		fillSelectionGrid(items, simpleChoices);
	}

	private void fillSelectionGrid(List<SelectionItemBean> items, List<SelectionSimpleChoiceBean> simpleChoices) {
		viewBuildingController.fillFirstRowWithChoices(simpleChoices);
		viewBuildingController.fillFirstColumnWithItems(items);
		
		this.groupChoicesControllers = viewBuildingController.fillGridWithButtons(items, simpleChoices);
	}

	@Override
	public void reset() {
		for (GroupAnswersController groupChoicesController : groupChoicesControllers) {
			groupChoicesController.reset();
		}
	}

	@Override
	public void setModel(SelectionModuleModel model) {
		this.model = model;
	}

	@Override
	public void setModuleSocket(ModuleSocket socket) {
		this.moduleSocket = socket;
	}

	@Override
	public void setBean(SelectionInteractionBean bean) {
		this.bean = bean;
	}

	@Override
	public void setLocked(boolean locked) {
		for (GroupAnswersController groupChoicesController : groupChoicesControllers) {
			groupChoicesController.setLockedAllAnswers(locked);
		}
		updateView();
	}

	@Override
	public void updateView() {
		selectionModuleViewUpdatingController.updateView(selectionModuleView, groupChoicesControllers);
	}

	@Override
	public void markAnswers(MarkAnswersType type, MarkAnswersMode mode) {
		for(GroupAnswersController groupChoicesController : groupChoicesControllers){
			markAnswersOnButtonsFromController(type, mode, groupChoicesController);
		}
		updateView();
	}

	private void markAnswersOnButtonsFromController(MarkAnswersType type, MarkAnswersMode mode, GroupAnswersController groupChoicesController) {
		List<SelectionAnswerDto> selectedButtons = groupChoicesController.getSelectedAnswers();
		List<SelectionAnswerDto> buttonsToMark = identifiableAnswersByTypeFinder.findAnswersObjectsOfGivenType(type, selectedButtons, model);
		List<SelectionAnswerDto> notSelectedButtons = groupChoicesController.getNotSelectedAnswers();
		
		if(MarkAnswersMode.MARK.equals(mode)){
			if(MarkAnswersType.CORRECT.equals(type)){
				applyAnswerTypeToCollection(UserAnswerType.CORRECT, buttonsToMark);
			}else{
				applyAnswerTypeToCollection(UserAnswerType.WRONG, buttonsToMark);
			}
			
			applyAnswerTypeToCollection(UserAnswerType.NONE, notSelectedButtons);
			
		}else if(MarkAnswersMode.UNMARK.equals(mode)){
			List<SelectionAnswerDto> buttonsToMarkDefaultState = new ArrayList<SelectionAnswerDto>(buttonsToMark);
			buttonsToMarkDefaultState.addAll(notSelectedButtons);
			applyAnswerTypeToCollection(UserAnswerType.DEFAULT, buttonsToMarkDefaultState);
		}
	}

	private void applyAnswerTypeToCollection(UserAnswerType userAnswerType, Collection<SelectionAnswerDto> answers){
		for (SelectionAnswerDto selectionAnswerDto : answers) {
			selectionAnswerDto.setSelectionAnswerType(userAnswerType);
		}
	}
	
	@Override
	public void showAnswers(ShowAnswersType mode) {
		List<String> answersToSelect;
		if(ShowAnswersType.CORRECT.equals(mode))
			answersToSelect = model.getCorrectAnswers();
		else
			answersToSelect = model.getCurrentAnswers();
		
		for (GroupAnswersController groupChoicesController : groupChoicesControllers) {
			groupChoicesController.selectOnlyAnswersMatchingIds(answersToSelect);
		}
		updateView();
	}

	@Override
	public Widget asWidget() {
		return selectionModuleView.asWidget();
	}

}