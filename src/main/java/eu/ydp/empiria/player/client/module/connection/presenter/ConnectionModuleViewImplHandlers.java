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

package eu.ydp.empiria.player.client.module.connection.presenter;

import com.google.common.base.Optional;
import com.google.gwt.dom.client.NativeEvent;
import eu.ydp.empiria.player.client.module.UserAgentCheckerWrapper;
import eu.ydp.empiria.player.client.module.connection.item.ConnectionItem;
import eu.ydp.empiria.player.client.module.connection.view.event.ConnectionMoveEndEvent;
import eu.ydp.empiria.player.client.module.connection.view.event.ConnectionMoveEvent;
import eu.ydp.empiria.player.client.module.connection.view.event.ConnectionMoveStartEvent;
import eu.ydp.empiria.player.client.module.connection.view.event.HasConnectionMoveHandlers;
import eu.ydp.empiria.player.client.util.events.internal.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.internal.player.PlayerEvent;
import eu.ydp.empiria.player.client.util.events.internal.player.PlayerEventTypes;
import eu.ydp.empiria.player.client.util.position.Point;
import eu.ydp.empiria.player.client.util.position.PositionHelper;

import javax.inject.Inject;
import java.util.Set;

import static eu.ydp.empiria.player.client.module.components.multiplepair.MultiplePairModuleConnectType.NORMAL;

public class ConnectionModuleViewImplHandlers implements HasConnectionMoveHandlers {

    private ConnectionModuleViewImpl view;

    @Inject
    private UserAgentCheckerWrapper userAgent;

    @Inject
    private ConnectionsBetweenItemsFinder connectionsFinder;

    @Inject
    private EventsBus eventsBus;

    @Inject
    private PositionHelper positionHelper;

    @Inject
    private ConnectionSurfacesManager surfacesManager;

    @Inject
    private ConnectionItemPairFinder pairFinder;

    public void setView(ConnectionModuleViewImpl view) {
        this.view = view;
    }

    @Override
    public void onConnectionStart(ConnectionMoveStartEvent event) {
        if (view.isLocked())
            return;

        Optional<ConnectionItem> itemOptional = connectionsFinder.findConnectionItemForEventOnWidget(event, view, view.getConnectionItems());

        if (itemOptional.isPresent()) {
            ConnectionItem item = itemOptional.get();
            eventsBus.fireEvent(new PlayerEvent(PlayerEventTypes.TOUCH_EVENT_RESERVATION));
            event.preventDefault();
            view.getSurfaceForLineDrawing(item, NORMAL);
            view.startDrawLine(item);
            item.setConnected(true, NORMAL);

            ConnectionItem source = view.getConnectionItemPair().getSource();
            if (source != null && !item.equals(source)) {
                view.resetIfNotConnected(source.getBean().getIdentifier());
            }

            view.getConnectionItemPair().setSource(item);

        } else {
            NativeEvent nativeEvent = event.getNativeEvent();
            Point clickPoint = getClickPoint(nativeEvent);
            ConnectionPairEntry<String, String> pointOnPath = surfacesManager.findPointOnPath(view.getConnectedSurfaces(), clickPoint);

            if (pointOnPath != null) {
                surfacesManager.removeSurfaceFromParent(view.getConnectedSurfaces(), pointOnPath);
                view.disconnect(pointOnPath.getSource(), pointOnPath.getTarget(), true);
                event.preventDefault();
            }
        }
    }

    @Override
    public void onConnectionMoveEnd(ConnectionMoveEndEvent event) {
        if (view.isLocked())
            return;

        ConnectionItem connectionStartItem = view.getConnectionItemPair().getSource();

        Optional<ConnectionItem> itemOptional = connectionsFinder.findConnectionItemForEventOnWidget(event, view, view.getConnectionItems());
        if (shouldCancelConnection(itemOptional, connectionStartItem)) {
            cancelConnection();
            return;
        }

        final int x = new Double(event.getX()).intValue();
        final int y = new Double(event.getY()).intValue();

        Set<ConnectionItem> connectionItems = view.getConnectionItems().getConnectionItems(connectionStartItem);
        Optional<ConnectionItem> connectionEndItem = pairFinder.findConnectionItemForCoordinates(connectionItems, x, y);

        final ConnectionItem targetConnectionItem = view.getConnectionItemPair().getTarget();
        boolean mayConnect = targetConnectionItem != null && !targetConnectionItem.equals(connectionStartItem);

        if (connectionEndItem.isPresent()) {
            ConnectionItem target = connectionEndItem.get();
            view.drawLineBetween(connectionStartItem, target);
            view.connectItems(connectionStartItem, target, NORMAL, true);
            view.resetTouchConnections();
        } else if (mayConnect) {
            view.connect(connectionStartItem, targetConnectionItem, NORMAL, true);
            view.resetTouchConnections();
        } else {
            view.getConnectionItemPair().setTarget(connectionStartItem);
        }

        view.clearSurface(connectionStartItem);
    }

    @Override
    public void onConnectionMoveCancel() {
        cancelConnection();
    }

    private boolean shouldCancelConnection(Optional<ConnectionItem> itemOptional, ConnectionItem connectionStartItem) {
        if (!itemOptional.isPresent()) {
            return true;
        }

        ConnectionItem connectionItem = itemOptional.get();
        if (connectionItem == connectionStartItem) {
            return false;
        }

        return connectionItem.getColumn().equals(connectionStartItem.getColumn());
    }

    private void cancelConnection() {
        ConnectionItem connectionStartItem = view.getConnectionItemPair().getSource();
        if (connectionStartItem != null) {
            view.resetIfNotConnected(connectionStartItem.getBean().getIdentifier());
            view.resetTouchConnections();
            view.clearSurface(connectionStartItem);
        }
    }

    @Override
    public void onConnectionMove(ConnectionMoveEvent event) {
        ConnectionItem source = view.getConnectionItemPair().getSource();
        if (!view.isLocked() && view.getStartPositions().containsKey(source)) {
            event.preventDefault();
            if (wasMoved(event)) {
                updatePointPosition(event);
                view.drawLineBetween(source, (int) event.getX(), (int) event.getY());
            }
        }
    }

    private Point getClickPoint(NativeEvent event) {
        return positionHelper.getPoint(event, view.getConnectionViewElement());
    }

    private boolean wasMoved(ConnectionMoveEvent event) {
        return Math.abs(view.getLastPoint().getSource() - event.getX()) > approximation()
                || Math.abs(view.getLastPoint().getTarget() - event.getY()) > approximation();
    }

    private void updatePointPosition(ConnectionMoveEvent event) {
        view.getLastPoint().setSource(event.getX());
        view.getLastPoint().setTarget(event.getY());
    }

    private int approximation() {
        return userAgent.isStackAndroidBrowser() ? 15 : 5;
    }

}
