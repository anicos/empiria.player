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

package eu.ydp.empiria.player.client.module.registry;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import eu.ydp.empiria.player.client.module.core.base.IModule;

public interface ModulesRegistrySocket {

    public boolean isModuleSupported(String nodeName);

    public boolean isMultiViewModule(Element element);

    public boolean isInlineModule(String nodeName);

    public IModule createModule(Element node);
}
