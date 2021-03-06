/*
 * Copyright 2017 Young Digital Planet S.A.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.ydp.empiria.player.client.module.core.base;

import com.google.gwt.xml.client.Element;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.controller.events.interaction.StateChangedInteractionEvent;
import eu.ydp.empiria.player.client.controller.feedback.counter.event.FeedbackCounterEvent;
import eu.ydp.empiria.player.client.controller.item.ResponseSocket;
import eu.ydp.empiria.player.client.controller.variables.objects.response.Response;
import eu.ydp.empiria.player.client.controller.workmode.WorkModePreviewClient;
import eu.ydp.empiria.player.client.controller.workmode.WorkModeTestSubmittedClient;
import eu.ydp.empiria.player.client.gin.factory.PageScopeFactory;
import eu.ydp.empiria.player.client.gin.scopes.page.PageScoped;
import eu.ydp.empiria.player.client.module.core.flow.Ignored;
import eu.ydp.empiria.player.client.util.events.internal.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.internal.state.StateChangeEvent;
import eu.ydp.empiria.player.client.util.events.internal.state.StateChangeEventTypes;
import eu.ydp.gwtutil.client.xml.XMLUtils;

import java.util.List;

import static eu.ydp.empiria.player.client.controller.feedback.counter.event.FeedbackCounterEventTypes.RESET_COUNTER;

public abstract class InteractionModuleBase extends ModuleBase implements IInteractionModule, WorkModePreviewClient, WorkModeTestSubmittedClient, Ignored {

    private final FeedbackCounterEvent feedbackCounterResetEvent = new FeedbackCounterEvent(RESET_COUNTER, this);

    private Response response;
    private String responseIdentifier;

    @Inject
    private EventsBus eventsBus;

    @Inject
    private PageScopeFactory pageScopeFactory;

    @Inject
    @PageScoped
    private ResponseSocket responseSocket;

    @Override
    public final String getIdentifier() {
        return responseIdentifier;
    }

    protected Response getResponse() {
        return response;
    }

    protected final void setResponseFromElement(Element element) {
        responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
        response = findResponse();
    }

    protected Response findResponse() {
        return responseSocket.getResponse(responseIdentifier);
    }

    protected void fireStateChanged(boolean userInteract, boolean isReset) {
        eventsBus.fireEvent(new StateChangeEvent(StateChangeEventTypes.STATE_CHANGED, new StateChangedInteractionEvent(userInteract, isReset, this)),
                pageScopeFactory.getCurrentPageScope());
    }

    @Override
    public List<IModule> getChildren() {
        return getModuleSocket().getChildren(this);
    }

    @Override
    public void enablePreviewMode() {
        lock(true);
    }

    @Override
    public void enableTestSubmittedMode() {
        lock(true);
    }

    @Override
    public void disableTestSubmittedMode() {
        lock(false);
    }

    @Override
    public boolean isIgnored() {
        return false;
    }

    @Override
    public void reset() {
        getEventsBus().fireEvent(feedbackCounterResetEvent);
    }
}
