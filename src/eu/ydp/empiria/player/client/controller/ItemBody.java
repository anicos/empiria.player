package eu.ydp.empiria.player.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

import eu.ydp.empiria.player.client.controller.body.BodyGenerator;
import eu.ydp.empiria.player.client.controller.body.ModuleHandlerManager;
import eu.ydp.empiria.player.client.controller.body.ModulesInstalator;
import eu.ydp.empiria.player.client.controller.body.ParenthoodManager;
import eu.ydp.empiria.player.client.controller.communication.DisplayContentOptions;
import eu.ydp.empiria.player.client.controller.events.interaction.InteractionEventsListener;
import eu.ydp.empiria.player.client.controller.events.widgets.WidgetWorkflowListener;
import eu.ydp.empiria.player.client.controller.variables.objects.response.Response;
import eu.ydp.empiria.player.client.module.HasChildren;
import eu.ydp.empiria.player.client.module.IGroup;
import eu.ydp.empiria.player.client.module.IInteractionModule;
import eu.ydp.empiria.player.client.module.ILifecycleModule;
import eu.ydp.empiria.player.client.module.IModule;
import eu.ydp.empiria.player.client.module.IStateful;
import eu.ydp.empiria.player.client.module.IUniqueModule;
import eu.ydp.empiria.player.client.module.ModuleSocket;
import eu.ydp.empiria.player.client.module.ParenthoodSocket;
import eu.ydp.empiria.player.client.module.containers.group.GroupIdentifier;
import eu.ydp.empiria.player.client.module.containers.group.ItemBodyModule;
import eu.ydp.empiria.player.client.module.registry.ModulesRegistrySocket;
import eu.ydp.empiria.player.client.util.js.JSArrayUtils;

public class ItemBody implements WidgetWorkflowListener {

	public List<IModule> modules;

	protected ParenthoodManager parenthood;

	protected InteractionEventsListener interactionEventsListener;
	protected DisplayContentOptions options;
	protected ModuleSocket moduleSocket;
	protected ModulesRegistrySocket modulesRegistrySocket;
	private final ModuleHandlerManager moduleHandlerManager;

	protected ItemBodyModule itemBodyModule;

	private JSONArray stateAsync;
	private boolean attached = false;


	public ItemBody(DisplayContentOptions options, ModuleSocket moduleSocket,
			final InteractionEventsListener interactionEventsListener, ModulesRegistrySocket modulesRegistrySocket, ModuleHandlerManager moduleHandlerManager) {

		this.moduleSocket = moduleSocket;
		this.options = options;
		this.modulesRegistrySocket = modulesRegistrySocket;
		this.moduleHandlerManager = moduleHandlerManager;

		parenthood = new ParenthoodManager();

		this.interactionEventsListener = interactionEventsListener;

	}

	public Widget init(Element itemBodyElement) {

		ModulesInstalator modulesInstalator = new ModulesInstalator(parenthood, modulesRegistrySocket, moduleSocket, interactionEventsListener);
		BodyGenerator generator = new BodyGenerator(modulesInstalator, options);

		itemBodyModule = new ItemBodyModule();
		modulesInstalator.setInitialParent(itemBodyModule);
		itemBodyModule.initModule(itemBodyElement, moduleSocket, interactionEventsListener, generator);

		modules = new ArrayList<IModule>();
		modules.add(itemBodyModule);
		modules.addAll(modulesInstalator.installMultiViewUniqueModules());
		modules.addAll(modulesInstalator.getInstalledSingleViewModules());

		for (IModule module : modules){
			moduleHandlerManager.registerModule(module);
		}

		for (IModule currModule : modules) {
			if (currModule instanceof IUniqueModule){
				Response currResponse = moduleSocket.getResponse( ((IUniqueModule) currModule).getIdentifier() );
				if (currResponse != null) {
					currResponse.setModuleAdded();
				}
			}
		}

		return itemBodyModule.getView();


	}

	// ------------------------- EVENTS --------------------------------

	@Override
	public void onLoad() {
		for (IModule currModule : modules) {
			if (currModule instanceof ILifecycleModule) {
				((ILifecycleModule) currModule).onBodyLoad();
			}
		}

		attached = true;
	}

	@Override
	public void onUnload() {
		for (IModule currModule : modules) {
			if (currModule instanceof ILifecycleModule) {
				((ILifecycleModule) currModule).onBodyUnload();
			}
		}
	}

	public void setUp() {
		setState(stateAsync);
		for (IModule currModule : modules) {
			if (currModule instanceof ILifecycleModule) {
				((ILifecycleModule) currModule).onSetUp();
			}
		}
	}

	public void start() {
		for (IModule currModule : modules) {
			if (currModule instanceof ILifecycleModule) {
				((ILifecycleModule) currModule).onStart();
			}
		}
	}

	public void close() {
		for (IModule currModule : modules) {
			if (currModule instanceof ILifecycleModule) {
				((ILifecycleModule) currModule).onClose();
			}
		}
	}

	public int getModuleCount() {
		return modules.size();
	}

	/**
	 * Checks whether the item body contains at least one interactive module
	 *
	 * @return boolean
	 */
	public boolean hasInteractiveModules() {
		boolean foundInteractive = false;
		if (modules != null) {
			for (IModule currModule : modules) {
				if (currModule instanceof IInteractionModule){
					foundInteractive = true;
					break;
				}
			}
		}
		return foundInteractive;
	}

	// ------------------------- ACTIVITY --------------------------------

	public void markAnswers(boolean mark) {
		itemBodyModule.markAnswers(mark);

	}

	public void markAnswers(boolean mark, GroupIdentifier groupIdentifier) {
		IGroup currGroup = getGroupByGroupIdentifier(groupIdentifier);
		if (currGroup != null){
			currGroup.markAnswers(mark);
		}
	}

	public void showCorrectAnswers(boolean show) {
		itemBodyModule.showCorrectAnswers(show);
	}

	public void showCorrectAnswers(boolean show, GroupIdentifier groupIdentifier) {
		IGroup currGroup = getGroupByGroupIdentifier(groupIdentifier);
		if (currGroup != null){
			currGroup.showCorrectAnswers(show);
		}
	}

	public void reset() {
		itemBodyModule.reset();
	}

	public void reset(GroupIdentifier groupIdentifier) {
		IGroup currGroup = getGroupByGroupIdentifier(groupIdentifier);
		if (currGroup != null){
			currGroup.reset();
		}
	}

	public void lock(boolean lo) {
		itemBodyModule.lock(lo);
	}

	public void lock(boolean lo, GroupIdentifier groupIdentifier) {
		IGroup currGroup = getGroupByGroupIdentifier(groupIdentifier);
		if (currGroup != null){
			currGroup.lock(lo);
		}
	}

	private IGroup getGroupByGroupIdentifier(GroupIdentifier gi){
		IGroup lastGroup = null;
		for (IModule currModule : modules) {
			if (currModule instanceof IGroup){
				lastGroup = (IGroup)currModule;
				if  ( lastGroup.getGroupIdentifier().equals(gi)  ||  ("".equals(gi.getIdentifier()) && lastGroup instanceof ItemBodyModule) ){
					return lastGroup;
				}
			}
		}
		return lastGroup;
	}

	// ------------------------- STATEFUL --------------------------------

	public JSONArray getState() {

		JSONObject states = new JSONObject();

		for (IModule currModule : modules) {
			if (currModule instanceof IStateful
					&& currModule instanceof IUniqueModule) {
				states.put(((IUniqueModule) currModule).getIdentifier(),
						((IStateful) currModule).getState());
			}
		}

		JSONArray statesArr = new JSONArray();
		statesArr.set(0, states);

		return statesArr;
	}

	public void setState(JSONArray newState) {
		if (!attached) {
			stateAsync = newState;
		} else {
			if (newState instanceof JSONArray) {

				try {

					if (newState.isArray() != null  &&  newState.isArray().size() > 0){
						JSONObject stateObj = newState.isArray().get(0).isObject();

						for (int i = 0; i < modules.size(); i++) {
							if (modules.get(i) instanceof IStateful
									&& modules.get(i) instanceof IUniqueModule) {
								String curridentifier = ((IUniqueModule) modules
										.get(i)).getIdentifier();

								if (curridentifier != null && curridentifier != "") {

									if (stateObj.containsKey(curridentifier)) {
										JSONValue currState = stateObj
												.get(curridentifier);
										if (currState != null
												&& currState.isArray() != null) {
											((IStateful) modules.get(i))
													.setState(currState.isArray());
										}
									}
								}
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

	}

	public JavaScriptObject getJsSocket() {
		return createJsSocket();
	}

	private native JavaScriptObject createJsSocket()/*-{
		var socket = {};
		var instance = this;
		socket.getModuleSockets = function(){
			return instance.@eu.ydp.empiria.player.client.controller.ItemBody::getModuleJsSockets()();
		}
		return socket;
	}-*/;

	private JavaScriptObject getModuleJsSockets() {
		eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket[] moduleSockets = getModuleSockets();
		JsArray<JavaScriptObject> moduleSocketsJs = JSArrayUtils.createArray(0);
		for (int i = 0; i < moduleSockets.length; i++) {
			moduleSocketsJs.set(i, moduleSockets[i].getJsSocket());
		}
		return moduleSocketsJs;
	}

	public eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket[] getModuleSockets() {
		List<eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket> moduleSockets = new ArrayList<eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket>();
		for (IModule currModule : modules) {
			if (currModule instanceof eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket) {
				moduleSockets
						.add(((eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket) currModule));
			}
		}
		return moduleSockets
				.toArray(new eu.ydp.empiria.player.client.controller.communication.sockets.ModuleInterferenceSocket[0]);
	}

	public HasChildren getModuleParent(IModule module) {
		return parenthood.getParent(module);
	}

	public List<IModule> getModuleChildren(IModule parent) {
		return parenthood.getChildren(parent);
	}

	public void setUpperParenthoodSocket(ParenthoodSocket parenthoodSocket) {
		parenthood.setUpperLevelParenthood(parenthoodSocket);
	}
}
