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

package eu.ydp.empiria.player.client.module.img.picture.player.structure;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.peterfranza.gwt.jaxb.client.parser.utils.XMLContent;
import eu.ydp.empiria.player.client.controller.body.InlineBodyGeneratorSocket;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class PictureTitleProviderTest {

    @InjectMocks
    private PictureTitleProvider testObj;
    @Mock
    private InlineBodyGeneratorSocket inlineBodyGeneratorSocket;
    @Mock
    private Widget titleWidget;

    private PicturePlayerBean bean;
    private String titleXml = "titleXml";
    private String titleXmlWithHTMLTags = "<b>titleXml</b>";
    PicturePlayerTitleBean titleBean;

    @Before
    public void setUp() throws Exception {
        bean = new PicturePlayerBean();
        titleBean = new PicturePlayerTitleBean();
        XMLContent xmlContent = mock(XMLContent.class);
        Element titleElement = mock(Element.class, RETURNS_DEEP_STUBS);
        when(xmlContent.getValue()).thenReturn(titleElement);
        titleBean.setTitleName(xmlContent);
        when(inlineBodyGeneratorSocket.generateInlineBody(titleElement)).thenReturn(titleWidget);
        when(titleElement.toString()).thenReturn(titleXmlWithHTMLTags);
    }


    @Test
    public void shouldReturnTitleXmlString() {
        // given
        bean.setTitleBean(titleBean);
        // when
        String titleString = testObj.getPictureTitleString(bean);
        // then
        assertThat(titleString).isEqualTo(titleXml);
    }

    @Test
    public void shouldReturnEmptyXmlString() {
        // when
        String titleString = testObj.getPictureTitleString(bean);
        // then
        assertThat(titleString).isEqualTo("");
    }

    @Test
    public void shouldCreateEmptyTitleWidget() {
        // when
        Widget titleWidgetResult = testObj.getPictureTitleWidget(bean, inlineBodyGeneratorSocket);
        // then
        assertThat(titleWidgetResult).isNotEqualTo(titleWidget);
    }

    @Test
    public void shouldCreateTitleWidget() {
        // given
        bean.setTitleBean(titleBean);
        // when
        Widget titleWidgetResult = testObj.getPictureTitleWidget(bean, inlineBodyGeneratorSocket);
        // then
        assertThat(titleWidgetResult).isEqualTo(titleWidget);
    }
}
