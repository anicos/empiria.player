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

package eu.ydp.empiria.player.client.module.expression.evaluate;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import eu.ydp.empiria.player.client.controller.variables.objects.CheckMode;
import eu.ydp.empiria.player.client.controller.variables.objects.Evaluate;
import eu.ydp.empiria.player.client.controller.variables.objects.response.CorrectAnswers;
import eu.ydp.empiria.player.client.controller.variables.objects.response.Response;
import eu.ydp.empiria.player.client.controller.variables.objects.response.ResponseBuilder;
import eu.ydp.empiria.player.client.controller.variables.objects.response.ResponseValue;
import eu.ydp.empiria.player.client.module.expression.ExpressionToPartsDivider;
import eu.ydp.empiria.player.client.module.expression.IdentifiersFromExpressionExtractor;
import eu.ydp.empiria.player.client.module.expression.PipedReplacementsParser;
import eu.ydp.empiria.player.client.module.expression.ResponseFinder;
import eu.ydp.empiria.player.client.module.expression.adapters.DefaultExpressionCharactersAdapter;
import eu.ydp.empiria.player.client.module.expression.adapters.ExpressionCharacterMappingProvider;
import eu.ydp.empiria.player.client.module.expression.adapters.ExpressionCharactersMappingParser;
import eu.ydp.empiria.player.client.module.expression.model.ExpressionBean;
import eu.ydp.empiria.player.client.style.StyleSocket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CommutationEvaluatorJUnitTest {

    private static int COUNTER;


    private CommutationEvaluator testObj;

    @Before
    public void setUp() throws Exception {
        ResponseValuesFetcherFunctions fetcherFunctions = new ResponseValuesFetcherFunctions();
        ResponseFinder responseFinder = new ResponseFinder(new ExpressionToPartsDivider(), new IdentifiersFromExpressionExtractor());
        StyleSocket styleSocket = Mockito.mock(StyleSocket.class);
        PipedReplacementsParser expressionReplacementsParser = new PipedReplacementsParser();
        ExpressionCharactersMappingParser parser = new ExpressionCharactersMappingParser(expressionReplacementsParser);
        ExpressionCharacterMappingProvider replacementsProvider = new ExpressionCharacterMappingProvider(styleSocket, parser);
        DefaultExpressionCharactersAdapter defaultExpressionCharactersAdapter = new DefaultExpressionCharactersAdapter(replacementsProvider);

        testObj = new CommutationEvaluator(fetcherFunctions, responseFinder, defaultExpressionCharactersAdapter);
    }

    @Test
    public void evaluateCorrect() {
        // given
        ExpressionBean bean = new ExpressionBean();

        List<Response> responses = Lists.newArrayList(correctResponse(), correctResponse(), correctResponse(), correctResponse(), correctResponse());
        bean.setTemplate("'0'+'2'+'3'='1'+'4'");
        bean.getResponses().addAll(responses);

        Multiset<Multiset<String>> correctAnswerMultiSet = HashMultiset.create(Lists.<Multiset<String>>newArrayList(
                HashMultiset.create(Lists.newArrayList("answer_1", "answer_4")),
                HashMultiset.create(Lists.newArrayList("answer_0", "answer_2", "answer_3")),
                HashMultiset.create(Lists.newArrayList("answer_0", "answer_2", "answer_3", "answer_1", "answer_4"))));
        bean.setCorectResponses(correctAnswerMultiSet);

        // when
        boolean result = testObj.evaluate(bean);

        // then
        assertThat(result, equalTo(true));
    }

    @Test
    public void evaluateCorrect_commutated() {
        // given
        ExpressionBean bean = new ExpressionBean();

        List<Response> responses = Lists.newArrayList(response(0, 3), response(1, 4), response(2, 0), response(3, 2), (response(4, 1)));
        bean.setTemplate("'0'+'2'+'3'='1'+'4'");
        bean.getResponses().addAll(responses);

        Multiset<Multiset<String>> correctAnswerMultiSet = HashMultiset.create(Lists.<Multiset<String>>newArrayList(
                HashMultiset.<String>create(Lists.newArrayList("answer_1", "answer_4")),
                HashMultiset.<String>create(Lists.newArrayList("answer_0", "answer_2", "answer_3")),
                HashMultiset.<String>create(Lists.newArrayList("answer_0", "answer_2", "answer_3", "answer_1", "answer_4"))));
        bean.setCorectResponses(correctAnswerMultiSet);

        // when
        boolean result = testObj.evaluate(bean);

        // then
        assertThat(result, equalTo(true));
    }

    @Test
    public void evaluateCorrect_commutated_equalSignInGap() {
        // given
        ExpressionBean bean = new ExpressionBean();

        List<Response> responses = Lists.newArrayList(response("=", "2", "id_equal"), response("1", "5", "id1"), response("5", "3", "id5"),
                response("3", "1", "id3"), (response("2", "=", "id2")));
        bean.setTemplate("'id1'+'id2'+'id3''id_equal'+'id5'");
        bean.getResponses().addAll(responses);

        Multiset<Multiset<String>> correctAnswerMultiSet = HashMultiset.create(Lists.<Multiset<String>>newArrayList(
                HashMultiset.create(Lists.newArrayList("5")), HashMultiset.create(Lists.newArrayList("1", "2", "3")),
                HashMultiset.create(Lists.newArrayList("1", "2", "3", "=", "5"))));
        bean.setCorectResponses(correctAnswerMultiSet);

        // when
        boolean result = testObj.evaluate(bean);

        // then
        assertThat(result, equalTo(true));
    }

    @Test
    public void evaluateCorrectWithCharsConversionAdapter_commutated_equalSignInGap() {
        // given
        ExpressionBean bean = new ExpressionBean();

        List<Response> responses = Lists.newArrayList(response("=", "=", "id_equal"), response("/", ":", "id_oper"), response("15.1", "15,1", "id1"),
                response("5,1", "5.1", "id2"), response("2", "2", "id3"), response("12,5", "12,5", "id5"));
        bean.setTemplate("'id1'-'id2''id_oper''id3''id_equal''id5'");
        bean.getResponses().addAll(responses);

        Multiset<Multiset<String>> correctAnswerMultiSet = HashMultiset.create(Lists.<Multiset<String>>newArrayList(
                HashMultiset.create(Lists.newArrayList("12.5")), HashMultiset.create(Lists.newArrayList("15.1", "5,1", "/", "2")),
                HashMultiset.create(Lists.newArrayList("15.1", "5,1", "/", "2", "=", "12.5"))));
        bean.setCorectResponses(correctAnswerMultiSet);

        // when
        boolean result = testObj.evaluate(bean);

        // then
        assertThat(result, equalTo(true));
    }

    @Test
    public void evaluateWrong_someWrongs() {
        // given
        ExpressionBean bean = new ExpressionBean();
        List<Response> responses = Lists.newArrayList(correctResponse(), correctResponse(), correctResponse(), correctResponse(), wrongResponse());
        bean.setTemplate("'0'+'2'+'3'='1'+'4'");
        bean.getResponses().addAll(responses);

        Multiset<Multiset<String>> correctAnswerMultiSet = HashMultiset.create(Lists.<Multiset<String>>newArrayList(
                HashMultiset.<String>create(Lists.newArrayList("answer_1", "answer_4")),
                HashMultiset.<String>create(Lists.newArrayList("answer_0", "answer_2", "answer_3")),
                HashMultiset.<String>create(Lists.newArrayList("answer_0", "answer_2", "answer_3", "answer_1", "answer_4"))));
        bean.setCorectResponses(correctAnswerMultiSet);

        // when
        boolean result = testObj.evaluate(bean);

        // then
        assertThat(result, equalTo(false));
    }

    @Test
    public void evaluateWrong_commutated() {
        // given
        ExpressionBean bean = new ExpressionBean();
        List<Response> responses = Lists.newArrayList(response(0, 4), response(1, 3), response(2, 0), response(3, 2), (response(4, 1)));
        bean.setTemplate("'0'+'2'+'3'='1'+'4'");
        bean.getResponses().addAll(responses);

        Multiset<Multiset<String>> correctAnswerMultiSet = HashMultiset.create(Lists.<Multiset<String>>newArrayList(
                HashMultiset.<String>create(Lists.newArrayList("answer_1", "answer_4")),
                HashMultiset.<String>create(Lists.newArrayList("answer_0", "answer_2", "answer_3")),
                HashMultiset.<String>create(Lists.newArrayList("answer_0", "answer_2", "answer_3", "answer_1", "answer_4"))));
        bean.setCorectResponses(correctAnswerMultiSet);


        // when
        boolean result = testObj.evaluate(bean);

        // then
        assertThat(result, equalTo(false));
    }

    private Response response(int correct, int user) {
        CorrectAnswers correctAnswers = new CorrectAnswers();
        correctAnswers.add(new ResponseValue("answer_" + correct));
        List<String> values = Lists.newArrayList("answer_" + user);

        return getBuilder().withCorrectAnswers(correctAnswers).withValues(values).withIdentifier(String.valueOf(correct)).build();
    }

    private Response response(String correct, String user, String id) {
        CorrectAnswers correctAnswers = new CorrectAnswers();
        correctAnswers.add(new ResponseValue(correct));
        List<String> values = Lists.newArrayList(user);

        return getBuilder().withCorrectAnswers(correctAnswers).withValues(values).withIdentifier(id).build();
    }

    private ResponseBuilder getBuilder() {
        return new ResponseBuilder().withEvaluate(Evaluate.DEFAULT).withExpression(new ExpressionBean()).withCheckMode(CheckMode.EXPRESSION);
    }

    private Response correctResponse() {
        Response response = response(COUNTER, COUNTER);
        COUNTER++;
        return response;
    }

    private Response wrongResponse() {
        Response response = response(COUNTER++, COUNTER++);
        return response;
    }

}
