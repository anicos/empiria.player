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

package eu.ydp.empiria.player.client.module.img.events.handlers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import eu.ydp.gwtutil.client.util.UserAgentUtil;

public class TouchHandlerOnImageProvider implements Provider<ITouchHandlerOnImageInitializer> {

    @Inject
    private UserAgentUtil userAgentUtil;

    @Inject
    private PointerHandlersOnImageInitializer pointerHandlersOnImageInitializer;

    @Inject
    private TouchHandlersOnImageInitializer touchHandlersOnImageInitializer;

    @Override
    public ITouchHandlerOnImageInitializer get() {
        return userAgentUtil.isIE() ? pointerHandlersOnImageInitializer : touchHandlersOnImageInitializer;
    }
}
