package eu.ydp.empiria.player.client.module.testmode.submit.view;

import com.google.gwt.user.client.ui.IsWidget;

import eu.ydp.gwtutil.client.event.factory.Command;

public interface TestPageSubmitView extends IsWidget {
	public void addHandler(Command command);

	public void lock();

	public void unlock();
}
