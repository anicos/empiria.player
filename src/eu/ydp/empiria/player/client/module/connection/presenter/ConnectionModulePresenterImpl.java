package eu.ydp.empiria.player.client.module.connection.presenter;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import eu.ydp.empiria.player.client.module.ModuleSocket;
import eu.ydp.empiria.player.client.module.components.multiplepair.MultiplePairModuleConnectType;
import eu.ydp.empiria.player.client.module.components.multiplepair.MultiplePairModuleView;
import eu.ydp.empiria.player.client.module.connection.ConnectionModuleModel;
import eu.ydp.empiria.player.client.module.connection.structure.MatchInteractionBean;
import eu.ydp.empiria.player.client.util.events.multiplepair.PairConnectEvent;
import eu.ydp.empiria.player.client.util.events.multiplepair.PairConnectEventHandler;
import eu.ydp.gwtutil.client.collections.KeyValue;

public class ConnectionModulePresenterImpl implements ConnectionModulePresenter, PairConnectEventHandler  {
	
	private MatchInteractionBean bean;
	
	ConnectionModuleModel model;

	//@Inject
	private MultiplePairModuleView moduleView;
	
	private ModuleSocket moduleSocket;
	
	@Override
	public void setModuleSocket(ModuleSocket moduleSocket) {
		this.moduleSocket = moduleSocket;
	}
	
	@Override
	public void bindView() {
		moduleView.setBean(bean);
		moduleView.bindView();
		
		moduleView.reset();
	}

	@Override
	public void reset() {
		moduleView.reset();
	}

	@Override
	public void setModuleView(MultiplePairModuleView moduleView) {
		this.moduleView = moduleView;
	}
	
	@Override
	public void setModel(ConnectionModuleModel model) {
		this.model = model;
	}

	@Override
	public void setBean(MatchInteractionBean bean) {
		this.bean = bean;
	}

	@Override
	public void setLocked(boolean locked) {
		moduleView.setLocked(locked);
	}

	@Override
	public void showCorrectAnswers() {
		showAnswers(model.getCorrectAnswers(), MultiplePairModuleConnectType.CORRECT);
	}

	@Override
	public void showCurrentAnswers() {
		showAnswers(model.getCurrentAnswers(), MultiplePairModuleConnectType.NORMAL); 
	}

	@Override
	public void markCorrectAnswers() {
		setAnswersMarked(true, MultiplePairModuleConnectType.CORRECT);
	}

	@Override
	public void markWrongAnswers() {
		setAnswersMarked(true, MultiplePairModuleConnectType.WRONG);
	}
	
	@Override
	public void unmarkCorrectAnswers() {
		setAnswersMarked(false, MultiplePairModuleConnectType.CORRECT);
	}

	@Override
	public void unmarkWrongAnswers() {
		setAnswersMarked(false, MultiplePairModuleConnectType.WRONG);
	}
	
	@Override
	public Widget asWidget() {
		return moduleView.asWidget();
	}
	
	@Override
	public void onConnectionEvent(PairConnectEvent event) {
		switch (event.getType()) {
		case CONNECTED:
			if (isConnectionValid(event.getSourceItem(), event.getTargetItem())) {
				model.addAnswer(event.getItemsPair());
			} else {
				moduleView.disconnect(event.getSourceItem(), event.getTargetItem());
			}
			break;
		case DISCONNECTED:			
			model.removeAnswer(event.getItemsPair());
			break;
		case WRONG_CONNECTION:
		default:
			/* TODO: to handle incorrect situation */
			break;
		}		
	}

	private boolean isConnectionValid(String sourceItem, String targetItem) {
		int errorsCount = 0;

		if (bean.getSourceChoicesIdentifiersSet().contains(targetItem) || bean.getTargetChoicesIdentifiersSet().contains(sourceItem)) {
			// source-target pair allowed
			errorsCount++;
		} else if (bean.getMaxAssociations() > 0 && model.getCurrentOverallPairingsNumber() >= bean.getMaxAssociations()) {
			// The maxAssociations attribute controls the maximum number of pairings the user is allowed to make overall.
			errorsCount++;
		} else if (bean.getChoiceByIdentifier(sourceItem).getMatchMax() > 0
				&& model.getCurrentChoicePairingsNumber(sourceItem) >= bean.getChoiceByIdentifier(sourceItem).getMatchMax()) {
			// Individually, each choice has a matchMax attribute that controls how many pairings it can be part of.
			errorsCount++;
		} else if (bean.getChoiceByIdentifier(targetItem).getMatchMax() > 0
				&& model.getCurrentChoicePairingsNumber(targetItem) >= bean.getChoiceByIdentifier(targetItem).getMatchMax()) {
			errorsCount++;
		}

		return errorsCount == 0;
	}
		
	/**
	 * Sets connections in view using given {@link KeyValue} collection for defined {@link MultiplePairModuleConnectType}
	 * 
	 * @param answers 
	 * @param type
	 */
	private void showAnswers(List<KeyValue<String, String>> answers, MultiplePairModuleConnectType type) {		
		moduleView.reset();
		for (KeyValue<String, String> answer : answers) {			
			moduleView.connect(answer.getKey(), answer.getValue(), type);
		}				
	}

	/**
	 * Marks / unmarks answers
	 * 
	 * @param markMode - {@link Boolean} mark/unmark
	 * @param markingType - {@link MultiplePairModuleConnectType#CORRECT} or {@link MultiplePairModuleConnectType#WRONG} 
	 */
	private void setAnswersMarked(boolean markMode, MultiplePairModuleConnectType markingType) {
		List<Boolean> responseEvaluated = evaluateResponse();		
		List<KeyValue<String, String>> currentAnswers = model.getCurrentAnswers();
				
		int responseCnt = 0;
		for (Boolean isCorrect : responseEvaluated) {
			MultiplePairModuleConnectType type = (isCorrect) ? MultiplePairModuleConnectType.CORRECT : MultiplePairModuleConnectType.WRONG;			
			KeyValue<String, String> answersPair = currentAnswers.get(responseCnt);
			if (markingType.equals(type)) {
				if (markMode) {
					// TODO: disconnect ?
					moduleView.connect(answersPair.getKey(), answersPair.getValue(), type);
					// TODO: jesli dana pozycja nie jest zaznaczona wcale to wyslac MultiplePairModuleConnectType.NONE ??
				} else {
					moduleView.disconnect(answersPair.getKey(), answersPair.getValue());
					moduleView.connect(answersPair.getKey(), answersPair.getValue(), MultiplePairModuleConnectType.NORMAL); // TODO: NORMAL
				}
			}
			responseCnt++; 
		}		
	}		
	
	List<Boolean> evaluateResponse() {
		return moduleSocket.evaluateResponse(model.getResponse());
	}	

}
