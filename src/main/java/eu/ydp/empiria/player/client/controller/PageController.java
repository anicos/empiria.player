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

package eu.ydp.empiria.player.client.controller;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.ydp.empiria.player.client.controller.communication.*;
import eu.ydp.empiria.player.client.controller.communication.sockets.PageInterferenceSocket;
import eu.ydp.empiria.player.client.controller.events.interaction.StateChangedInteractionEvent;
import eu.ydp.empiria.player.client.controller.flow.IFlowSocket;
import eu.ydp.empiria.player.client.controller.log.OperationLogEvent;
import eu.ydp.empiria.player.client.controller.log.OperationLogManager;
import eu.ydp.empiria.player.client.controller.session.sockets.PageSessionSocket;
import eu.ydp.empiria.player.client.module.core.base.ParenthoodSocket;
import eu.ydp.empiria.player.client.util.events.internal.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.internal.reset.LessonResetEvent;
import eu.ydp.empiria.player.client.util.events.internal.reset.LessonResetEventHandler;
import eu.ydp.empiria.player.client.util.events.internal.reset.LessonResetEventTypes;
import eu.ydp.empiria.player.client.util.events.internal.state.StateChangeEvent;
import eu.ydp.empiria.player.client.util.events.internal.state.StateChangeEventTypes;
import eu.ydp.empiria.player.client.util.js.JSArrayUtils;
import eu.ydp.empiria.player.client.view.page.PageViewCarrier;
import eu.ydp.empiria.player.client.view.page.PageViewSocket;

import java.util.List;

public class PageController implements PageInterferenceSocket, LessonResetEventHandler {
    private final PageViewSocket pageViewSocket;
    private final PageSessionSocket pageSessionSocket;
    private final IFlowSocket flowSocket;
    private ParenthoodSocket parenthoodSocket;
    List<ItemController> items;

    private final AssessmentControllerFactory controllerFactory;
    private final EventsBus eventsBus;

    @Inject
    public PageController(@Assisted PageViewSocket pageViewSocket, @Assisted IFlowSocket flowSocket, @Assisted PageSessionSocket pageSessionSocket,
                          AssessmentControllerFactory controllerFactory, EventsBus eventsBus) {
        this.pageViewSocket = pageViewSocket;
        this.flowSocket = flowSocket;
        this.pageSessionSocket = pageSessionSocket;
        this.controllerFactory = controllerFactory;
        this.eventsBus = eventsBus;
    }

    public void initPage(PageData pageData) {
        items = Lists.newArrayList();
        eventsBus.addHandler(LessonResetEvent.getType(LessonResetEventTypes.RESET), this);

        // conception compatibility issue
        if (pageData.type == PageType.ERROR) {
            pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataError) pageData));
            OperationLogManager.logEvent(OperationLogEvent.DISPLAY_PAGE_FAILED);
        } else if (pageData.type == PageType.TEST) {
            PageDataTest pageDataTest = (PageDataTest) pageData;
            pageViewSocket.initItemViewSockets(pageDataTest.datas.length);
            pageViewSocket.setPageViewCarrier(new PageViewCarrier());

            for (int i = 0; i < pageDataTest.datas.length; i++) {
                ItemController controller = controllerFactory.getItemController(pageViewSocket.getItemViewSocket(i), pageSessionSocket.getItemSessionSocket());
                controller.init(pageDataTest.displayOptions);
                controller.setAssessmentParenthoodSocket(parenthoodSocket);
                if (pageDataTest.flowOptions.activityMode == ActivityMode.CHECK) {
                    controller.checkItem();
                }
                items.add(controller);
            }

        } else if (pageData.type == PageType.TOC) {
            items.clear();
            pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataToC) pageData, flowSocket));
        } else if (pageData.type == PageType.SUMMARY) {
            items.clear();
            pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataSummary) pageData, flowSocket));
        }
    }

    public void close() {
        for (ItemController itemController : items) {
            itemController.close();
        }
    }

    public void onShow() {
        for (ItemController itemController : items) {
            itemController.onShow();
        }
    }

    public void reset() {
        items.clear();
    }

    @Override
    public JavaScriptObject getJsSocket() {
        return createPageSocket();
    }

    private JavaScriptObject getItemJsSockets() {
        JavaScriptObject itemSockets = JavaScriptObject.createArray();
        for (int i = 0; i < items.size(); i++) {
            JSArrayUtils.fillArray(itemSockets, i, items.get(i).getItemSocket().getJsSocket());
        }
        return itemSockets;
    }

    private native JavaScriptObject createPageSocket()/*-{
        var socket = {};
        var instance = this;
        socket.getItemSockets = function () {
            return instance.@eu.ydp.empiria.player.client.controller.PageController::getItemJsSockets()();
        };
        return socket;
    }-*/;

    public void setAssessmentParenthoodSocket(ParenthoodSocket assessmentParenthoodSocket) {
        parenthoodSocket = assessmentParenthoodSocket;
    }

    public boolean hasInteractiveModules() {
        return Iterables.any(items, new Predicate<ItemController>() {
            @Override
            public boolean apply(ItemController item) {
                return item.hasInteractiveModules();
            }
        });
    }

    @Override
    public void onLessonReset(LessonResetEvent event) {
        for (ItemController item : items) {
            item.resetItem();
        }

        eventsBus.fireEvent(new StateChangeEvent(StateChangeEventTypes.STATE_CHANGED, new StateChangedInteractionEvent(false, true, null)));
    }
}
