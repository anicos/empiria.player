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

package eu.ydp.empiria.player.client.controller;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.ydp.empiria.player.client.controller.body.InlineBodyGenerator;
import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import eu.ydp.empiria.player.client.controller.communication.AssessmentData;
import eu.ydp.empiria.player.client.controller.communication.DisplayContentOptions;
import eu.ydp.empiria.player.client.controller.style.StyleLinkDeclaration;
import eu.ydp.empiria.player.client.gin.factory.AssessmentFactory;
import eu.ydp.empiria.player.client.gin.factory.InlineBodyGeneratorFactory;
import eu.ydp.empiria.player.client.module.*;
import eu.ydp.empiria.player.client.module.containers.group.DefaultGroupIdentifier;
import eu.ydp.empiria.player.client.module.containers.group.GroupIdentifier;
import eu.ydp.empiria.player.client.module.core.base.HasChildren;
import eu.ydp.empiria.player.client.module.core.base.HasParent;
import eu.ydp.empiria.player.client.module.core.base.IModule;
import eu.ydp.empiria.player.client.module.core.base.ParenthoodSocket;
import eu.ydp.empiria.player.client.module.core.base.Group;
import eu.ydp.empiria.player.client.module.registry.ModulesRegistrySocket;
import eu.ydp.empiria.player.client.util.file.xml.XmlData;
import eu.ydp.empiria.player.client.view.assessment.AssessmentBodyView;
import eu.ydp.gwtutil.client.json.YJsonArray;
import eu.ydp.gwtutil.client.json.js.YJsJsonFactory;

import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Assessment {

    /**
     * Whole assessment title
     */
    private final String title;

    /**
     * XML DOM of the assessment
     */
    private final XmlData xmlData;

    private Panel pageSlot;

    public StyleLinkDeclaration styleDeclaration;

    private final ModulesRegistrySocket modulesRegistrySocket;

    private final DisplayContentOptions options;

    private AssessmentBody body;

    private AssessmentBodyView bodyView;

    /**
     * Properties instance prepared by assessmentController (based on item body
     * properties through the page controller)
     */
    private final AssessmentFactory assessmentFactory;
    private final InlineBodyGeneratorFactory inlineBodyGeneratorFactory;

    /**
     * C'tor
     *
     * @param data XMLData object as data source
     */
    @Inject
    public Assessment(@Assisted AssessmentData data, @Assisted DisplayContentOptions options, @Assisted ModulesRegistrySocket modulesRegistrySocket,
                      AssessmentFactory assessmentFactory, InlineBodyGeneratorFactory inlineBodyGeneratorFactory) {

        this.assessmentFactory = assessmentFactory;
        this.inlineBodyGeneratorFactory = inlineBodyGeneratorFactory;
        this.xmlData = data.getData();

        this.modulesRegistrySocket = modulesRegistrySocket;
        this.options = options;

        Document document = xmlData.getDocument();
        Element rootNode = (Element) document.getElementsByTagName("assessmentTest").item(0);
        Element skinBody = getSkinBody(data.getSkinData());

        if (skinBody == null) {
            skinBody = XMLParser.parse("<itemBody><pageInPage /></itemBody>").getDocumentElement();
        }

        styleDeclaration = new StyleLinkDeclaration(xmlData.getDocument().getElementsByTagName("styleDeclaration"), xmlData.getBaseURL());
        title = rootNode.getAttribute("title");

        initializeBody(skinBody);
    }

    private void initializeBody(Element bodyNode) {
        if (bodyNode != null) {
            body = assessmentFactory.createAssessmentBody(options, moduleSocket, modulesRegistrySocket);
            bodyView = assessmentFactory.createAssessmentBodyView(body);
            bodyView.init(body.init(bodyNode));
            pageSlot = body.getPageSlot();
        }
    }

    public Widget getSkinView() {
        return bodyView;
    }

    public Panel getPageSlot() {
        return pageSlot;
    }

    public void setUp() {
        if (body != null) {
            body.setUp();
        }
    }

    public void start() {
        if (body != null) {
            body.start();
        }
    }

    public ParenthoodSocket getAssessmentParenthoodSocket() {
        return (body == null) ? null : body.getParenthoodSocket(); // NOPMD
    }

    /**
     * @return assessment title
     */
    public String getTitle() {
        return (title == null) ? "" : title;
    }

    protected Element getSkinBody(XmlData skinData) {
        Element skinBody = null;

        try {
            Document skinDocument = skinData.getDocument();
            skinBody = (Element) skinDocument.getElementsByTagName("itemBody").item(0);
        } catch (Exception e) {
            System.out.println("Skin body didn't load properly.");
        }

        return skinBody;
    }

    private final ModuleSocket moduleSocket = new ModuleSocket() {

        private InlineBodyGenerator inlineBodyGenerator;

        @Override
        public InlineBodyGeneratorSocket getInlineBodyGeneratorSocket() {
            if (inlineBodyGenerator == null) {
                inlineBodyGenerator = inlineBodyGeneratorFactory.createInlineBodyGenerator(modulesRegistrySocket, this, options, body.getParenthood());
            }
            return inlineBodyGenerator;
        }

        @Override
        public HasChildren getParent(IModule module) {
            if (body != null) {
                return body.getModuleParent(module);
            }
            return null;
        }

        @Override
        public GroupIdentifier getParentGroupIdentifier(IModule module) {
            IModule currParent = module;
            while (currParent != null && !(currParent instanceof Group)) {
                currParent = getParent(currParent);
            }
            if (currParent != null) {
                return ((Group) currParent).getGroupIdentifier();
            }
            return new DefaultGroupIdentifier("");
        }

        @Override
        public List<IModule> getChildren(IModule parent) {
            if (body != null) {
                return body.getModuleChildren(parent);
            }
            return null;
        }

        @Override
        public List<HasParent> getNestedChildren(HasChildren parent) {
            if (body != null) {
                return body.getNestedChildren(parent);
            }
            return null;
        }

        @Override
        public List<HasChildren> getNestedParents(HasParent child) {
            if (body != null) {
                return body.getNestedParents(child);
            }
            return null;
        }

        @Override
        public Stack<HasChildren> getParentsHierarchy(IModule module) {
            Stack<HasChildren> hierarchy = new Stack<HasChildren>();
            HasChildren currParent = getParent(module);
            while (currParent != null) {
                hierarchy.push(currParent);
                currParent = getParent(currParent);
            }
            return hierarchy;
        }

        @Override
        public Set<InlineFormattingContainerType> getInlineFormattingTags(IModule module) {
            InlineContainerStylesExtractor inlineContainerHelper = new InlineContainerStylesExtractor();
            return inlineContainerHelper.getInlineStyles(module);
        }

        @Override
        public YJsonArray getStateById(String id) {
            return YJsJsonFactory.createArray();
        }

    };

}
