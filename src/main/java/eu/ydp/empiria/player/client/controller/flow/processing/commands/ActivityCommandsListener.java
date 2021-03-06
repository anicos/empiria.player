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

package eu.ydp.empiria.player.client.controller.flow.processing.commands;

import eu.ydp.empiria.player.client.module.containers.group.GroupIdentifier;

public interface ActivityCommandsListener {

    void checkPage();

    void showAnswersPage();

    void continuePage();

    void resetPage();

    void lockPage();

    void unlockPage();

    void checkGroup(GroupIdentifier gi);

    void showAnswersGroup(GroupIdentifier gi);

    void continueGroup(GroupIdentifier gi);

    void resetGroup(GroupIdentifier gi);

    void lockGroup(GroupIdentifier gi);

    void unlockGroup(GroupIdentifier gi);
}
