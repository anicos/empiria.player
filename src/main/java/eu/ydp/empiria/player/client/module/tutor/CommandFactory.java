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

package eu.ydp.empiria.player.client.module.tutor;

import com.google.inject.Inject;
import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.PersonaService;
import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.TutorCommandConfig;
import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.TutorConfig;
import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.TutorPersonaProperties;
import eu.ydp.empiria.player.client.gin.factory.TutorCommandsModuleFactory;
import eu.ydp.empiria.player.client.module.model.image.ShowImageDTO;
import eu.ydp.empiria.player.client.module.tutor.view.TutorView;
import eu.ydp.empiria.player.client.resources.EmpiriaPaths;
import eu.ydp.gwtutil.client.animation.Animation;
import eu.ydp.gwtutil.client.animation.AnimationConfig;
import eu.ydp.gwtutil.client.animation.AnimationFactory;
import eu.ydp.gwtutil.client.gin.scopes.module.ModuleScoped;
import eu.ydp.gwtutil.client.util.geom.Size;

import java.util.Iterator;

public class CommandFactory {

    private final TutorConfig tutorConfig;
    private final TutorView moduleView;
    private final AnimationFactory animationFactory;
    private final TutorCommandsModuleFactory commandsModuleFactory;
    private final EmpiriaPaths paths;
    private final PersonaService personaService;

    @Inject
    public CommandFactory(@ModuleScoped TutorConfig tutorConfig, @ModuleScoped TutorView moduleView, @ModuleScoped PersonaService personaService,
                          AnimationFactory animationFactory, TutorCommandsModuleFactory commandsModuleFactory, EmpiriaPaths paths) {
        this.tutorConfig = tutorConfig;
        this.moduleView = moduleView;
        this.personaService = personaService;
        this.animationFactory = animationFactory;
        this.commandsModuleFactory = commandsModuleFactory;
        this.paths = paths;
    }

    public TutorCommand createCommand(ActionType actionType, TutorEndHandler handler) {
        Iterable<TutorCommandConfig> commandsConfig = tutorConfig.getCommandsForAction(actionType);
        Iterator<TutorCommandConfig> commandsIterator = commandsConfig.iterator();
        if (!commandsIterator.hasNext()) {
            throw new RuntimeException("Empty config");
        }
        TutorCommandConfig commandConfig = commandsIterator.next();
        return createCommandFromConfig(commandConfig, handler);
    }

    private TutorCommand createCommandFromConfig(TutorCommandConfig commandConfig, TutorEndHandler handler) {
        CommandType type = commandConfig.getType();
        String asset = commandConfig.getAsset();
        switch (type) {
            case ANIMATION:
                return createAnimationCommand(asset, handler);
            case IMAGE:
                return createImageCommand(asset, handler);
            default:
                throw new RuntimeException("Command type not supported");
        }
    }

    private TutorCommand createAnimationCommand(String assetName, final TutorEndHandler handler) {
        TutorPersonaProperties tutorPersonaProperties = getPersonaProperties();

        int animationFps = tutorPersonaProperties.getAnimationFps();
        Size animationSize = tutorPersonaProperties.getAnimationSize();

        String assetPath = createAssetPath(assetName);

        AnimationConfig config = new AnimationConfig(animationFps, animationSize, assetPath);

        Animation animation = animationFactory.getAnimation(config, moduleView);

        return commandsModuleFactory.createAnimationCommand(animation, handler);
    }

    private TutorCommand createImageCommand(String assetName, TutorEndHandler handler) {
        TutorPersonaProperties tutorPersonaProperties = getPersonaProperties();
        Size size = tutorPersonaProperties.getAnimationSize();

        String assetPath = createAssetPath(assetName);

        ShowImageDTO showImageDTO = new ShowImageDTO(assetPath, size);

        return commandsModuleFactory.createShowImageCommand(moduleView, showImageDTO, handler);
    }

    private String createAssetPath(String assetName) {
        TutorPersonaProperties personaProperties = getPersonaProperties();
        String personaName = personaProperties.getName();
        return paths.getCommonsFilePath(personaName + assetName);
    }

    private TutorPersonaProperties getPersonaProperties() {
        return personaService.getPersonaProperties();
    }
}
