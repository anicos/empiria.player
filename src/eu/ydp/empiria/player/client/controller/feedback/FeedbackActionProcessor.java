package eu.ydp.empiria.player.client.controller.feedback;

import java.util.List;

import eu.ydp.empiria.player.client.controller.feedback.structure.action.FeedbackAction;

public interface FeedbackActionProcessor {
	
	void processActions(List<FeedbackAction> actions);
	
}
