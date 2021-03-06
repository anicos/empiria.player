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

package eu.ydp.empiria.player.client.gin.module;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.TypeLiteral;
import eu.ydp.empiria.player.client.gin.factory.DragDropObjectFactory;
import eu.ydp.empiria.player.client.module.draggap.DragGapModuleFactory;
import eu.ydp.empiria.player.client.module.draggap.DragGapModuleModel;
import eu.ydp.empiria.player.client.module.draggap.SourceListManagerAdapter;
import eu.ydp.empiria.player.client.module.draggap.math.MathDragGapPresenter;
import eu.ydp.empiria.player.client.module.draggap.math.structure.MathDragGapBean;
import eu.ydp.empiria.player.client.module.draggap.math.structure.MathDragGapModuleJAXBParserFactory;
import eu.ydp.empiria.player.client.module.draggap.math.structure.MathDragGapStructure;
import eu.ydp.empiria.player.client.module.draggap.presenter.DragGapPresenter;
import eu.ydp.empiria.player.client.module.draggap.standard.StandardDragGapPresenter;
import eu.ydp.empiria.player.client.module.draggap.standard.structure.DragGapBean;
import eu.ydp.empiria.player.client.module.draggap.standard.structure.DragGapModuleJAXBParserFactory;
import eu.ydp.empiria.player.client.module.draggap.standard.structure.DragGapStructure;
import eu.ydp.empiria.player.client.module.draggap.structure.DragGapBaseStructure;
import eu.ydp.empiria.player.client.module.draggap.view.DragGapView;
import eu.ydp.empiria.player.client.module.draggap.view.DragGapViewImpl;
import eu.ydp.empiria.player.client.util.dom.drag.DragDropHelper;
import eu.ydp.empiria.player.client.util.dom.drag.DragDropHelperImpl;
import eu.ydp.gwtutil.client.gin.scopes.module.ModuleScopedProvider;

public class DragDropGinModule extends BaseGinModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<DragGapPresenter<DragGapBean>>() {
        }).to(StandardDragGapPresenter.class);
        bind(new TypeLiteral<DragGapPresenter<MathDragGapBean>>() {
        }).to(MathDragGapPresenter.class);
        bind(new TypeLiteral<DragGapBaseStructure<MathDragGapBean, MathDragGapModuleJAXBParserFactory>>() {
        }).to(MathDragGapStructure.class);
        bind(new TypeLiteral<DragGapBaseStructure<DragGapBean, DragGapModuleJAXBParserFactory>>() {
        }).to(DragGapStructure.class);
        bind(DragGapView.class).to(DragGapViewImpl.class);
        bind(DragDropHelper.class).to(DragDropHelperImpl.class);

        bindModuleScoped(DragGapModuleModel.class, new TypeLiteral<ModuleScopedProvider<DragGapModuleModel>>() {
        });
        bindModuleScoped(DragGapView.class, new TypeLiteral<ModuleScopedProvider<DragGapView>>() {
        });
        bindModuleScoped(SourceListManagerAdapter.class, new TypeLiteral<ModuleScopedProvider<SourceListManagerAdapter>>() {
        });

        install(new GinFactoryModuleBuilder().build(DragGapModuleFactory.class));
        install(new GinFactoryModuleBuilder().build(DragDropObjectFactory.class));
    }
}
