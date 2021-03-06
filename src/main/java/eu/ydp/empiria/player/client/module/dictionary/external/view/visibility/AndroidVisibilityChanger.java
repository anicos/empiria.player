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

package eu.ydp.empiria.player.client.module.dictionary.external.view.visibility;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

public class AndroidVisibilityChanger implements VisibilityChanger {

    private static final int TOP_HIDDEN = -10000;

    @Override
    public void show(VisibilityClient client) {
        Style style = client.getElementStyle();
        style.setPosition(Position.STATIC);
        style.clearTop();
    }

    @Override
    public void hide(VisibilityClient client) {
        final Style style = client.getElementStyle();
        style.setPosition(Position.FIXED);
        style.setTop(TOP_HIDDEN, Unit.PX);
    }

}
