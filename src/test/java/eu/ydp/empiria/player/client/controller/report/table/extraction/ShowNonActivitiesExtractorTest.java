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

package eu.ydp.empiria.player.client.controller.report.table.extraction;

import com.google.gwt.xml.client.Element;
import eu.ydp.empiria.player.client.style.StyleSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static eu.ydp.empiria.player.client.resources.EmpiriaStyleNameConstants.EMPIRIA_REPORT_SHOW_NON_ACTIVITES;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowNonActivitiesExtractorTest {

    @InjectMocks
    private ShowNonActivitiesExtractor testobj;

    @Mock
    private StyleSocket styleSocket;
    @Mock
    private Element element;

    @Test
    public void shouldExtractTrue() {
        // given
        Map<String, String> styles = new HashMap<String, String>();
        styles.put(EMPIRIA_REPORT_SHOW_NON_ACTIVITES, "true");

        when(styleSocket.getStyles(element)).thenReturn(styles);

        // when
        boolean isShowNonActivities = testobj.extract(element);

        // then
        assertThat(isShowNonActivities).isTrue();
    }

    @Test
    public void shouldExtractFalse() {
        // given
        Map<String, String> styles = new HashMap<String, String>();
        styles.put(EMPIRIA_REPORT_SHOW_NON_ACTIVITES, "false");

        when(styleSocket.getStyles(element)).thenReturn(styles);

        // when
        boolean isShowNonActivities = testobj.extract(element);

        // then
        assertThat(isShowNonActivities).isFalse();
    }
}
