package eu.ydp.empiria.player.client.module.selection.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

import eu.ydp.empiria.player.client.gin.scopes.module.ModuleScoped;
import eu.ydp.empiria.player.client.module.AnswersMarkingTemplate;
import eu.ydp.empiria.player.client.module.MarkAnswersMode;
import eu.ydp.empiria.player.client.module.MarkAnswersType;
import eu.ydp.empiria.player.client.module.selection.model.GroupAnswersControllerModel;
import eu.ydp.empiria.player.client.module.selection.model.SelectionAnswerDto;
import eu.ydp.empiria.player.client.module.selection.model.UserAnswerType;

public class SelectionAnswersMarker extends AnswersMarkingTemplate {
	
	private final GroupAnswersControllerModel answersControllerModel;
	
	@Inject
	public SelectionAnswersMarker(
			@ModuleScoped GroupAnswersControllerModel answersControllerModel) {
				this.answersControllerModel = answersControllerModel;
	}
	
	@Override
	public void unmarkWrong() {
		unMark(MarkAnswersType.WRONG);
	}

	@Override
	public void markWrong() {
		applyAnswerTypeToCollection(UserAnswerType.WRONG, answersControllerModel.getButtonsToMarkForType(MarkAnswersType.WRONG));
		applyAnswerTypeToCollection(UserAnswerType.NONE, answersControllerModel.getNotSelectedAnswers());
	}

	@Override
	public void unmarkCorrect() {
		unMark(MarkAnswersType.CORRECT);
	}

	@Override
	public void markCorrect() {
		applyAnswerTypeToCollection(UserAnswerType.CORRECT, answersControllerModel.getButtonsToMarkForType(MarkAnswersType.CORRECT));
		applyAnswerTypeToCollection(UserAnswerType.NONE, answersControllerModel.getNotSelectedAnswers());
	}

	@Override
	public void markAnswers(MarkAnswersType type, MarkAnswersMode mode) {
		super.markAnswers(type, mode);
	}
	
	private void unMark(MarkAnswersType type) {
		List<SelectionAnswerDto> buttonsToMarkDefaultState = new ArrayList<SelectionAnswerDto>(answersControllerModel.getButtonsToMarkForType(type));
		buttonsToMarkDefaultState.addAll(answersControllerModel.getNotSelectedAnswers());
		applyAnswerTypeToCollection(UserAnswerType.DEFAULT, buttonsToMarkDefaultState);
	}

	private void applyAnswerTypeToCollection(UserAnswerType userAnswerType, Collection<SelectionAnswerDto> answers){
		for (SelectionAnswerDto selectionAnswerDto : answers) {
			selectionAnswerDto.setSelectionAnswerType(userAnswerType);
		}
	}
}