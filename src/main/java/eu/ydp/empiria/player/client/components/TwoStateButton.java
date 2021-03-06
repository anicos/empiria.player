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

package eu.ydp.empiria.player.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;

public class TwoStateButton extends PushButton {

    protected boolean stateDown = false;
    protected String upStyleName;
    protected String downStyleName;

    public TwoStateButton(String upStyleName, String downStyleName) {
        super();

        this.upStyleName = upStyleName;
        this.downStyleName = downStyleName;
        updateStyleName();

        addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                stateDown = !stateDown;
                updateStyleName();
            }
        });
    }

    protected void updateStyleName() {
        if (stateDown) {
            setStylePrimaryName(downStyleName);
        } else {
            setStylePrimaryName(upStyleName);
        }
    }

    public boolean isStateDown() {
        return stateDown;
    }

    public void setStateDown(boolean d) {
        stateDown = d;
        updateStyleName();
    }
}
