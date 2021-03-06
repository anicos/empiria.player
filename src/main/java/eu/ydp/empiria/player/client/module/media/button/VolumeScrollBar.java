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

package eu.ydp.empiria.player.client.module.media.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;
import eu.ydp.empiria.player.client.gin.factory.PageScopeFactory;
import eu.ydp.empiria.player.client.module.media.MediaStyleNameConstants;
import eu.ydp.empiria.player.client.util.events.internal.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.internal.media.MediaEvent;
import eu.ydp.empiria.player.client.util.events.internal.media.MediaEventHandler;
import eu.ydp.empiria.player.client.util.events.internal.media.MediaEventTypes;
import eu.ydp.empiria.player.client.util.position.PositionHelper;

import java.util.Iterator;

public class VolumeScrollBar extends AbstractMediaScroll {

    private final MediaStyleNameConstants styleNames; // NOPMD

    private static VolumeScrollBarUiBinder uiBinder = GWT.create(VolumeScrollBarUiBinder.class);

    interface VolumeScrollBarUiBinder extends UiBinder<Widget, VolumeScrollBar> {
    }

    @UiField(provided = true)
    protected SimpleMediaButton button;
    @UiField
    protected FlowPanel progressBar;

    @UiField
    protected FlowPanel mainProgressDiv;

    @UiField
    protected FlowPanel beforeButton;

    @UiField
    protected FlowPanel afterButton;

    protected HandlerRegistration durationchangeHandlerRegistration; // NOPMD
    private final EventsBus eventsBus;
    private final PositionHelper positionHelper;
    private final PageScopeFactory pageScopeFactory;

    @Inject
    public VolumeScrollBar(MediaStyleNameConstants styleNames, EventsBus eventsBus, PositionHelper positionHelper, PageScopeFactory pageScopeFactory) {
        this.styleNames = styleNames;
        this.eventsBus = eventsBus;
        this.positionHelper = positionHelper;
        this.pageScopeFactory = pageScopeFactory;
        button = new SimpleMediaButton(styleNames.QP_MEDIA_VOLUME_SCROLLBAR_BUTTON(), false);
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public boolean isSupported() {
        return getMediaAvailableOptions().isVolumeChangeSupported();
    }

    /**
     * wielkosc przycisku wyswietlanego na pasku postepu
     *
     * @return
     */
    protected int getButtonLength() {
        return button.getElement().getAbsoluteBottom() - button.getElement().getAbsoluteTop();
    }

    /**
     * dlugosc paska postepu
     *
     * @return
     */
    protected int getScrollLength() {
        return (mainProgressDiv.getElement().getAbsoluteBottom() - mainProgressDiv.getElement().getAbsoluteTop()) - getButtonLength();
    }

    @Override
    public void init() {
        super.init();
        if (isSupported()) {
            MediaEventHandler handler = new MediaEventHandler() {
                @Override
                public void onMediaEvent(MediaEvent event) {
                    if (getMediaWrapper().isMuted()) {
                        moveScroll(getScrollLength());
                    }
                }
            };
            eventsBus.addAsyncHandlerToSource(MediaEvent.getType(MediaEventTypes.ON_VOLUME_CHANGE), getMediaWrapper(), handler, pageScopeFactory.getCurrentPageScope());
            handler = new MediaEventHandler() {
                @Override
                public void onMediaEvent(MediaEvent event) {
                    double volume = getMediaWrapper().getVolume();
                    moveScroll((int) (getScrollLength() * volume));
                    durationchangeHandlerRegistration.removeHandler();
                }
            };
            durationchangeHandlerRegistration = eventsBus.addAsyncHandlerToSource(MediaEvent.getType(MediaEventTypes.ON_DURATION_CHANGE), getMediaWrapper(),
                    handler);

        } else {
            progressBar.setStyleName(styleNames.QP_MEDIA_VOLUME_SCROLLBAR() + UNSUPPORTED_SUFFIX);
            Iterator<Widget> iter = progressBar.iterator();
            while (iter.hasNext()) {
                iter.next().removeFromParent();
            }
        }
    }

    protected void setVolume(double value) {
        MediaEvent event = new MediaEvent(MediaEventTypes.CHANGE_VOLUME, getMediaWrapper());
        event.setVolume(value);
        eventsBus.fireAsyncEventFromSource(event, getMediaWrapper());
    }

    @Override
    protected void setPosition(NativeEvent event) {// NOPMD
        if (isPressed() && ((Element) event.getEventTarget().cast()).getClassName().contains("qp-media-volume-scrollbar-center")) {
            event.preventDefault();
            int positionY = positionHelper.getYPositionRelativeToTarget(event, mainProgressDiv.getElement());
            positionY = positionY > 0 ? positionY : 0;
            double volume = (1f / getScrollLength()) * (getScrollLength() - positionY);
            setVolume(volume > 1 ? 1 : volume < 0 ? 0 : volume);
            positionY = positionY - getButtonLength() / 2;
            moveScroll(positionY > 0 ? positionY : 0);
        }
    }

    /**
     * ustawia suwak na odpowiedniej pozycji
     *
     * @param positionY
     */
    protected void moveScroll(int positionY) {// NOPMD
        int scrollSize = getScrollLength();
        positionY = positionY > scrollSize ? scrollSize : positionY;
        button.getElement().getStyle().setTop(positionY, Unit.PX);
        beforeButton.getElement().getStyle().setHeight(positionY, Unit.PX);
        afterButton.getElement().getStyle().setTop(positionY + getButtonLength(), Unit.PX);
        afterButton.getElement().getStyle().setHeight(getScrollLength() - positionY, Unit.PX);
    }

    @Override
    public void setStyleNames() {
        if (isInFullScreen()) {
            progressBar.removeStyleName(styleNames.QP_MEDIA_VOLUME_SCROLLBAR());
            progressBar.addStyleName(styleNames.QP_MEDIA_VOLUME_SCROLLBAR() + FULL_SCREEN_SUFFIX);
        }
    }
}
