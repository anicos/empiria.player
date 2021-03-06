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

package eu.ydp.empiria.player.client.module.drawing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.module.drawing.toolbox.ToolboxView;
import eu.ydp.empiria.player.client.module.drawing.view.CanvasView;
import eu.ydp.gwtutil.client.gin.scopes.module.ModuleScoped;

public class DrawingViewImpl extends Composite implements DrawingView {

    private static DrawingViewUiBinder uiBinder = GWT.create(DrawingViewUiBinder.class);

    @UiTemplate("DrawingView.ui.xml")
    interface DrawingViewUiBinder extends UiBinder<Widget, DrawingViewImpl> {
    }

    @UiField(provided = true)
    ToolboxView toolboxView;
    @UiField(provided = true)
    CanvasView canvasView;

    @Inject
    public DrawingViewImpl(@ModuleScoped ToolboxView toolboxView, @ModuleScoped CanvasView canvasView) {
        this.toolboxView = toolboxView;
        this.canvasView = canvasView;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
