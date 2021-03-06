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

package eu.ydp.empiria.player.client.module.choice.structure;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.module.abstractmodule.structure.AbstractModuleStructure;
import eu.ydp.empiria.player.client.module.abstractmodule.structure.ShuffleHelper;
import eu.ydp.empiria.player.client.resources.EmpiriaTagConstants;
import eu.ydp.gwtutil.client.json.YJsonArray;
import eu.ydp.gwtutil.client.service.json.IJSONService;
import eu.ydp.gwtutil.client.xml.XMLParser;

import java.util.List;

public class ChoiceModuleStructure extends AbstractModuleStructure<ChoiceInteractionBean, ChoiceModuleJAXBParser> {

    @Inject
    private ChoiceModuleJAXBParser parserFactory;

    @Inject
    private XMLParser xmlParser;
    @Inject
    private IJSONService ijsonService;

    @Inject
    private ShuffleHelper shuffleHelper;

    @Override
    protected void prepareStructure(YJsonArray structure) {
        List<SimpleChoiceBean> randomizedChoices = shuffleHelper.randomizeIfShould(bean, bean.getSimpleChoices());
        bean.setSimpleChoices(randomizedChoices);
    }

    @Override
    protected NodeList getParentNodesForFeedbacks(Document xmlDocument) {
        return xmlDocument.getElementsByTagName(EmpiriaTagConstants.NAME_SIMPLE_CHOICE);
    }

    public void setMulti(boolean multi) {
        for (SimpleChoiceBean simpleChoice : getBean().getSimpleChoices()) {
            simpleChoice.setMulti(multi);
        }
    }

    @Override
    protected ChoiceModuleJAXBParser getParserFactory() {
        return parserFactory;
    }

    public List<SimpleChoiceBean> getSimpleChoices() {
        return getBean().getSimpleChoices();
    }

    @Override
    protected XMLParser getXMLParser() {
        return xmlParser;
    }

    @Override
    public YJsonArray getSavedStructure() {
        return ijsonService.createArray();
    }
}
