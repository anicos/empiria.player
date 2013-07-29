package eu.ydp.empiria.player.client.module.selection.model;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import eu.ydp.empiria.player.client.gin.scopes.module.ModuleScoped;
import eu.ydp.empiria.player.client.module.MarkAnswersType;
import eu.ydp.empiria.player.client.module.selection.SelectionModuleModel;
import eu.ydp.empiria.player.client.module.selection.controller.GroupAnswersController;
import eu.ydp.empiria.player.client.module.selection.controller.IdentifiableAnswersByTypeFinder;

public class GroupAnswersControllerModel {

	@Inject
	private IdentifiableAnswersByTypeFinder identifiableAnswersByTypeFinder;
	@Inject @ModuleScoped
	private SelectionModuleModel model;
	
	private List<GroupAnswersController> groupChoicesControllers;

	public void setGroupChoicesControllers(List<GroupAnswersController> groupChoicesControllers) {
		this.groupChoicesControllers = groupChoicesControllers;
	}
	
	public List<GroupAnswersController> getGroupChoicesControllers() {
		return groupChoicesControllers;
	}

	public int indexOf(GroupAnswersController groupChoicesController) {
		return groupChoicesControllers.indexOf(groupChoicesController);
	}

	public List<SelectionAnswerDto> getSelectedAnswers() {
		List<SelectionAnswerDto> selectedAnswers = new ArrayList<SelectionAnswerDto>();
		for (GroupAnswersController item : groupChoicesControllers) {
			selectedAnswers.addAll(item.getSelectedAnswers());
		}
		return selectedAnswers;
	}

	public List<SelectionAnswerDto> getNotSelectedAnswers() {
		List<SelectionAnswerDto> notSelectedAnswers = new ArrayList<SelectionAnswerDto>();
		for (GroupAnswersController item : groupChoicesControllers) {
			notSelectedAnswers.addAll(item.getNotSelectedAnswers());
		}
		return notSelectedAnswers;
	}
	
	public List<SelectionAnswerDto> getButtonsToMarkForType(MarkAnswersType type) {
		return identifiableAnswersByTypeFinder.findAnswersObjectsOfGivenType(type, getSelectedAnswers(), model);
	}
}