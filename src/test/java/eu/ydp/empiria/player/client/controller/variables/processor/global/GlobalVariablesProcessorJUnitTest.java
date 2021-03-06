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

package eu.ydp.empiria.player.client.controller.variables.processor.global;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import eu.ydp.empiria.player.client.controller.variables.manager.VariableManager;
import eu.ydp.empiria.player.client.controller.variables.objects.response.Response;
import eu.ydp.empiria.player.client.controller.variables.processor.results.model.DtoModuleProcessingResult;
import eu.ydp.empiria.player.client.controller.variables.processor.results.model.GlobalVariables;
import eu.ydp.empiria.player.client.controller.variables.processor.results.model.LastMistaken;
import eu.ydp.empiria.player.client.module.expression.model.ExpressionBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static eu.ydp.empiria.player.client.controller.variables.processor.global.GlobalVariablesTestHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GlobalVariablesProcessorJUnitTest {

    private final GlobalVariablesProcessor globalVariablesProcessor = Guice.createInjector().getInstance(GlobalVariablesProcessor.class);

    // expression
    String ID_0 = "id0";
    final int TODO_0 = 1;
    final int DONE_0 = 0;
    final int ERRORS_0 = 1;
    final LastMistaken LAST_MISTAKEN_0 = LastMistaken.CORRECT;
    final int MISTAKES_0 = 9;

    String ID_1 = "id1";
    final int TODO_1 = 1;
    final int DONE_1 = 1;
    final int ERRORS_1 = 0;
    final LastMistaken LAST_MISTAKEN_1 = LastMistaken.CORRECT;
    final int MISTAKES_1 = 83;

    // default 1
    String ID_2 = "id2";
    final int TODO_2 = 4;
    final int DONE_2 = 1;
    final int ERRORS_2 = 3;
    final LastMistaken LAST_MISTAKEN_2 = LastMistaken.WRONG;
    final int MISTAKES_2 = 9;

    // default 2
    String ID_3 = "id3";
    final int TODO_3 = 1;
    final int DONE_3 = 1;
    final int ERRORS_3 = 0;
    final LastMistaken LAST_MISTAKEN_3 = LastMistaken.CORRECT;
    final int MISTAKES_3 = 2;

    @Mock
    private VariableManager<Response> variableManager;

    @Before
    public void setUp() throws Exception {
        prepareResponses();
    }

    @Test
    public void shouldCalculateSumOfTodo() throws Exception {
        // given
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = createResults();

        // when
        GlobalVariables globalVariables = globalVariablesProcessor.calculateGlobalVariables(modulesProcessingResults, variableManager);

        // then
        assertThat(globalVariables.getTodo(), equalTo(6));
    }

    @Test
    public void shouldCalculateGlobalSumOfErrors() throws Exception {
        // given
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = createResults();

        // when
        GlobalVariables globalVariables = globalVariablesProcessor.calculateGlobalVariables(modulesProcessingResults, variableManager);

        // then
        assertThat(globalVariables.getErrors(), equalTo(4));
    }

    @Test
    public void shouldCalculateGlobalSumOfDone() throws Exception {
        // given
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = createResults();

        // when
        GlobalVariables globalVariables = globalVariablesProcessor.calculateGlobalVariables(modulesProcessingResults, variableManager);

        // then
        assertThat(globalVariables.getDone(), equalTo(2));
    }

    @Test
    public void shouldCalculateGlobalSumOfMistakes() throws Exception {
        // given
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = createResults();

        // when
        GlobalVariables globalVariables = globalVariablesProcessor.calculateGlobalVariables(modulesProcessingResults, variableManager);

        // then
        assertThat(globalVariables.getMistakes(), equalTo(103));
    }

    @Test
    public void shouldSetGlobalLastmistakenWhenEvenOneLocalIsSet() throws Exception {
        // given
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = createResults();

        // when
        GlobalVariables globalVariables = globalVariablesProcessor.calculateGlobalVariables(modulesProcessingResults, variableManager);

        // then
        assertThat(globalVariables.getLastMistaken(), equalTo(LastMistaken.WRONG));
    }

    @Test
    public void shouldNotSetGlobalLastmistakenWhenAllLocalAreWithoutLastmistaken() throws Exception {
        // given
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = createResults(LastMistaken.CORRECT);

        // when
        GlobalVariables globalVariables = globalVariablesProcessor.calculateGlobalVariables(modulesProcessingResults, variableManager);

        // then
        assertThat(globalVariables.getLastMistaken(), equalTo(LastMistaken.CORRECT));
    }

    private void prepareResponses() {

        ExpressionBean expressionBean = new ExpressionBean();
        Response response0 = createExpressionResponse(ID_0, expressionBean);
        Response response1 = createExpressionResponse(ID_1, expressionBean);
        Response response2 = createResponse(ID_2);
        Response response3 = createResponse(ID_3);

        when(variableManager.getVariable(ID_0)).thenReturn(response0);
        when(variableManager.getVariable(ID_1)).thenReturn(response1);
        when(variableManager.getVariable(ID_2)).thenReturn(response2);
        when(variableManager.getVariable(ID_3)).thenReturn(response3);
        when(variableManager.getVariableIdentifiers()).thenReturn(Sets.newHashSet(ID_0, ID_1, ID_2, ID_3));
    }

    private Map<String, DtoModuleProcessingResult> createResults() {
        return createResults(LAST_MISTAKEN_2);
    }

    private Map<String, DtoModuleProcessingResult> createResults(LastMistaken lastMistaken2) {
        DtoModuleProcessingResult processingResult0 = prepareProcessingResults(TODO_0, DONE_0, ERRORS_0, MISTAKES_0, LAST_MISTAKEN_0);
        DtoModuleProcessingResult processingResult1 = prepareProcessingResults(TODO_1, DONE_1, ERRORS_1, MISTAKES_1, LAST_MISTAKEN_1);
        DtoModuleProcessingResult processingResult2 = prepareProcessingResults(TODO_2, DONE_2, ERRORS_2, MISTAKES_2, lastMistaken2);
        DtoModuleProcessingResult processingResult3 = prepareProcessingResults(TODO_3, DONE_3, ERRORS_3, MISTAKES_3, LAST_MISTAKEN_3);
        Map<String, DtoModuleProcessingResult> modulesProcessingResults = ImmutableMap.of(ID_0, processingResult0, ID_1, processingResult1, ID_2,
                processingResult2, ID_3, processingResult3);
        return modulesProcessingResults;
    }
}
