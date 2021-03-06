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

package eu.ydp.empiria.player.client.module.object;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import eu.ydp.gwtutil.client.xml.XMLUtils;

public class ObjectElementReader {
    private static final String POSTER_ATTRIBUTE_NAME = "poster";
    private static final String HEIGHT_ATTRIBUTE_NAME = "height";
    private static final String WIDTH_ATTRIBUTE_NAME = "width";
    private static final String FULLSCREEN_ATTRIBUTE_VALUE = "fullscreen";
    private static final String DEFAULT_TEMPLATE_VALUE = "default";
    private static final String TEMPLATE_TAG_NAME = "template";
    private static final String NARRATION_SCRIPT_TAG_NAME = "narrationScript";
    private static final String TYPE_ATTRIBUTE_NAME = "type";

    public String getElementType(final Element element) {
            return XMLUtils.getAttributeAsString(element, TYPE_ATTRIBUTE_NAME);
    }

    public String getNarrationText(final Element element) {
        StringBuilder builder = new StringBuilder();
        NodeList nodeList = element.getElementsByTagName(NARRATION_SCRIPT_TAG_NAME);

        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final String text = XMLUtils.getText((Element) nodeList.item(i));
                builder.append(text).append(' ');
            }
        }

        return builder.toString();
    }

    public Element getDefaultTemplate(final Element element) {
        final NodeList templateNodes = element.getElementsByTagName(TEMPLATE_TAG_NAME);

        for (int i = 0; i < templateNodes.getLength(); i++) {
            final Element node = (Element) templateNodes.item(i);
            final String templateType = XMLUtils.getAttributeAsString(node, TYPE_ATTRIBUTE_NAME, DEFAULT_TEMPLATE_VALUE);
            if (DEFAULT_TEMPLATE_VALUE.equalsIgnoreCase(templateType)) {
                return node;
            }
        }

        return null;
    }

    public Element getFullscreenTemplate(Element element) {
        final NodeList templateNodes = element.getElementsByTagName(TEMPLATE_TAG_NAME);

        for (int i = 0; i < templateNodes.getLength(); i++) {
            final Element node = (Element) templateNodes.item(i);
            final String templateType = XMLUtils.getAttributeAsString(node, TYPE_ATTRIBUTE_NAME, DEFAULT_TEMPLATE_VALUE);
            if (FULLSCREEN_ATTRIBUTE_VALUE.equalsIgnoreCase(templateType)) {
                return node;
            }
        }

        return null;
    }

    public Integer getWidthOrDefault(final Element element, final int defaultValue) {
        int result = XMLUtils.getAttributeAsInt(element, WIDTH_ATTRIBUTE_NAME);

        if (result == 0) {
            return defaultValue;
        } else {
            return result;
        }
    }

    public Integer getHeightOrDefault(Element element, int defaultValue) {
        int result = XMLUtils.getAttributeAsInt(element, HEIGHT_ATTRIBUTE_NAME);

        if (result == 0) {
            return defaultValue;
        } else {
            return result;
        }
    }

    public String getPoster(Element element) {
        return XMLUtils.getAttributeAsString(element, POSTER_ATTRIBUTE_NAME);
    }

}
