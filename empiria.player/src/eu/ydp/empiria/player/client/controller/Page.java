package eu.ydp.empiria.player.client.controller;

import eu.ydp.empiria.player.client.controller.extensions.internal.InternalExtension;
import eu.ydp.empiria.player.client.controller.extensions.types.FlowDataSocketUserExtension;
import eu.ydp.empiria.player.client.controller.flow.FlowDataSupplier;

public class Page extends InternalExtension implements
		FlowDataSocketUserExtension {

	private static FlowDataSupplier supplier;
	boolean isInitialized = false;

	public Page() {
	}

	public static int getCurrentPageNumber() {
		return supplier != null ? supplier.getCurrentPageIndex() : 0;

	}

	@Override
	public void setFlowDataSupplier(FlowDataSupplier supplier) {
		Page.supplier = supplier;
		if (supplier != null) {
			isInitialized = true;
		}
	}

	@Override
	public void init() {

	}
}