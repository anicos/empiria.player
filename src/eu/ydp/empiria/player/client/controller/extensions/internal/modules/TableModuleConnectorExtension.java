package eu.ydp.empiria.player.client.controller.extensions.internal.modules;

import eu.ydp.empiria.player.client.controller.extensions.types.ModuleConnectorExtension;
import eu.ydp.empiria.player.client.module.IModule;
import eu.ydp.empiria.player.client.module.ModuleCreator;
import eu.ydp.empiria.player.client.module.table.TableModule;

public class TableModuleConnectorExtension extends ModuleExtension implements
		ModuleConnectorExtension {

	@Override
	public ModuleCreator getModuleCreator() {
		return new ModuleCreator() {
			
			@Override
			public boolean isMultiViewModule() {
				return false;
			}
			
			@Override
			public boolean isInlineModule() {
				return false;
			}
			
			@Override
			public IModule createModule() {
				return new TableModule();
			}
		};
	}

	@Override
	public String getModuleNodeName() {
		return "table";
	}

}
