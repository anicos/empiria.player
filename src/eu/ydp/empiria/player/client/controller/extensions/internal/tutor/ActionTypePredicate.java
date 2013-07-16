package eu.ydp.empiria.player.client.controller.extensions.internal.tutor;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.js.TutorActionJs;
import eu.ydp.empiria.player.client.module.tutor.ActionType;

public final class ActionTypePredicate implements Predicate<TutorActionJs> {
	
	private final ActionType type;

	ActionTypePredicate(ActionType type) {
		this.type = type;
	}

	@Override
	public boolean apply(@Nullable TutorActionJs actionJs) {
		return type.toString().equalsIgnoreCase( actionJs.getType() );
	}
}