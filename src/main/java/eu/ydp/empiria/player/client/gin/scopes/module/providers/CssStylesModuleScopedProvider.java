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

package eu.ydp.empiria.player.client.gin.scopes.module.providers;

import com.google.gwt.xml.client.Element;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import eu.ydp.empiria.player.client.style.IOSModuleStyle;
import eu.ydp.empiria.player.client.style.ModuleStyle;
import eu.ydp.empiria.player.client.style.ModuleStyleImpl;
import eu.ydp.empiria.player.client.style.StyleSocket;
import eu.ydp.gwtutil.client.gin.scopes.module.ModuleScoped;
import eu.ydp.gwtutil.client.util.UserAgentChecker.MobileUserAgent;
import eu.ydp.gwtutil.client.util.UserAgentUtil;

import java.util.Map;

@Singleton
public class CssStylesModuleScopedProvider implements Provider<ModuleStyle> {

    @Inject
    StyleSocket styleSocket;
    @Inject
    @ModuleScoped
    Provider<Element> xmlProvider;
    @Inject
    UserAgentUtil agentUtil;

    @Override
    public ModuleStyle get() {
        Map<String, String> styles = styleSocket.getStyles(xmlProvider.get());
        if (agentUtil.isMobileUserAgent(MobileUserAgent.SAFARI)) {
            return new IOSModuleStyle(styles);
        } else {
            return new ModuleStyleImpl(styles);
        }
    }

}
