package eu.ydp.empiria.player.client.module.containers;

import com.google.gwt.xml.client.Element;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.controller.body.BodyGeneratorSocket;
import eu.ydp.empiria.player.client.resources.StyleNameConstants;

public class AssessmentBodyModule extends SimpleContainerModuleBase {

    @Inject
    public AssessmentBodyModule(StyleNameConstants styleNames) {
        setContainerStyleName(styleNames.QP_BODY());
    }

    public void initModule(Element element, BodyGeneratorSocket generator) {
        initModule(element, null, generator);
    }
}
