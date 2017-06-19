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

package eu.ydp.empiria.player.client.module.identification.predicates;

import eu.ydp.empiria.player.client.module.identification.presenter.SelectableChoicePresenter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChoiceToIdentifierTransformerTest {
    @InjectMocks
    private ChoiceToIdentifierTransformer testObj;
    @Mock
    private SelectableChoicePresenter presenter;

    @Test
    public void shouldReturnTrue_whenChoiceIsSelected() {
        // given
        String identifier = "id";
        when(presenter.getIdentifier()).thenReturn(identifier);

        // when
        String result = testObj.apply(presenter);

        // then
        assertThat(result).isEqualTo(identifier);
    }
}