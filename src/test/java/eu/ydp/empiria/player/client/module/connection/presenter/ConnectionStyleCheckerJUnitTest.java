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

package eu.ydp.empiria.player.client.module.connection.presenter;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import eu.ydp.empiria.player.client.module.connection.ConnectionStyleNameConstants;
import eu.ydp.empiria.player.client.module.connection.exception.CssStyleException;
import eu.ydp.empiria.player.client.style.StyleSocket;
import eu.ydp.empiria.player.client.util.style.CssHelper;
import eu.ydp.gwtutil.client.xml.XMLParser;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("PMD")
public class ConnectionStyleCheckerJUnitTest {

    @Mock
    private CssHelper cssHelper;
    @Mock
    private ConnectionStyleNameConstants styleNames;
    @Mock
    private XMLParser xmlParser;
    @Mock
    private StyleSocket styleSocket;

    private ConnectionStyleChecker instance;
    private Map<String, String> styles = new HashMap<String, String>();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        instance = new ConnectionStyleChecker(styleSocket, xmlParser, styleNames, cssHelper);

    }

    @BeforeClass
    public static void disarm() {
        GWTMockUtilities.disarm();
    }

    @AfterClass
    public static void rearm() {
        GWTMockUtilities.restore();
    }

    @Test
    public void testIsStylesAreCorrect() {
        styles = new HashMap<String, String>();
        styles.put("display", "inline");
        styles.put("width", "20px");

        Document document = mock(Document.class);
        Element element = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(element);
        when(xmlParser.parse("<root><styleTest class=\"styleTest\"/></root>")).thenReturn(document);

        instance.cssClassNames.add("styleTest");

        instance.areStylesCorrectThrowsExceptionWhenNot(null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = CssStyleException.class)
    public void testIsStylesAreNotCorrect() {
        styles.put("display", "table-cell");
        styles.put("width", "20px");

        Document document = mock(Document.class);
        Element element = mock(Element.class);
        when(document.getDocumentElement()).thenReturn(element);
        when(xmlParser.parse("<root><styleTest class=\"styleTest\"/></root>")).thenReturn(document);

        instance.cssClassNames.add("styleTest");
        when(cssHelper.checkIfEquals(any(Map.class), eq("display"), eq("table-cell"))).thenReturn(true);

        instance.areStylesCorrectThrowsExceptionWhenNot(null);
    }
}
