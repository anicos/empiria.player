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

package eu.ydp.empiria.player.client.controller.extensions.internal.tutor;

import com.google.common.base.Predicate;
import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.js.TutorActionJs;
import eu.ydp.empiria.player.client.module.tutor.ActionType;

import javax.annotation.Nullable;

public final class ActionTypePredicate implements Predicate<TutorActionJs> {

    private final ActionType type;

    ActionTypePredicate(ActionType type) {
        this.type = type;
    }

    @Override
    public boolean apply(@Nullable TutorActionJs actionJs) {
        return type.toString().equalsIgnoreCase(actionJs.getType());
    }
}