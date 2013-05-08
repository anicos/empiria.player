package eu.ydp.empiria.player.client.controller.variables.processor.module.expression;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import eu.ydp.empiria.player.client.controller.variables.objects.response.Response;
import eu.ydp.empiria.player.client.controller.variables.processor.results.model.LastAnswersChanges;
import eu.ydp.empiria.player.client.module.expression.ExpressionEvaluationController;
import eu.ydp.empiria.player.client.module.expression.model.ExpressionBean;
import eu.ydp.empiria.player.client.module.expression.model.ExpressionEvaluationResult;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionModeVariableProcessorJUnitTest {

	private ExpressionModeVariableProcessor expressionModeVariableProcessor;
	
	@Mock
	private ExpressionEvaluationController expressionEvaluationController;
	@Mock
	private Response response;
	
	@Before
	public void setUp() throws Exception {
		expressionModeVariableProcessor = new ExpressionModeVariableProcessor(expressionEvaluationController);
	}

	@Test
	public void shouldCalculateDoneWhenExpressionIsCorrect() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.CORRECT);
		
		int done = expressionModeVariableProcessor.calculateDone(response);
		
		assertEquals(1, done);
	}
	
	@Test
	public void shouldCalculateDoneWhenExpressionEvaluationIsWrong() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.WRONG);
		
		int done = expressionModeVariableProcessor.calculateDone(response);
		
		assertEquals(0, done);
	}
	
	@Test
	public void shouldCalculateDoneWhenExpressionHaveNotUnsetValues() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.VALUES_NOT_SET);
		
		int done = expressionModeVariableProcessor.calculateDone(response);
		
		assertEquals(0, done);
	}
	
	@Test
	public void shouldCalculateErrorsWhenExpressionEvaluationIsCorrect() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.CORRECT);
		
		int errors = expressionModeVariableProcessor.calculateErrors(response);
		
		assertEquals(0, errors);
	}
	
	@Test
	public void shouldCalculateErrorsWhenExpressionEvaluationIsWrong() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.WRONG);
		
		int errors = expressionModeVariableProcessor.calculateErrors(response);
		
		assertEquals(1, errors);
	}
	
	@Test
	public void shouldCalculateErrorsWhenExpressionHaveUnsetValues() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.VALUES_NOT_SET);
		
		int errors = expressionModeVariableProcessor.calculateErrors(response);
		
		assertEquals(0, errors);
	}
	
	@Test
	public void shouldBeLastmistakenWhenWasChangesInAnswersAndExpressionEvaluationIsWrong() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.WRONG);
		
		LastAnswersChanges answersChanges = Mockito.mock(LastAnswersChanges.class);
		when(answersChanges.containChanges())
			.thenReturn(true);
		
		boolean lastmistaken = expressionModeVariableProcessor.checkLastmistaken(response, answersChanges);
		
		assertEquals(true, lastmistaken);
	}
	
	@Test
	public void shouldBeNotLastmistakenWhenWasChangesInAnswersAndExpressionEvaluationIsCorrect() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.CORRECT);
		
		LastAnswersChanges answersChanges = Mockito.mock(LastAnswersChanges.class);
		when(answersChanges.containChanges())
		.thenReturn(true);
		
		boolean lastmistaken = expressionModeVariableProcessor.checkLastmistaken(response, answersChanges);
		
		assertEquals(false, lastmistaken);
	}
	
	@Test
	public void shouldBeNotLastmistakenExpressionEvaluationIsWrongButWasNotChangesInAnswers() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.WRONG);
		
		LastAnswersChanges answersChanges = Mockito.mock(LastAnswersChanges.class);
		when(answersChanges.containChanges())
		.thenReturn(false);
		
		boolean lastmistaken = expressionModeVariableProcessor.checkLastmistaken(response, answersChanges);
		
		assertEquals(false, lastmistaken);
	}
	
	@Test
	public void shouldCalculateMistakesWhenWasLastmistake() throws Exception {
		boolean lastmistaken = true;
		int previousMistakes = 12;
		
		int mistakes = expressionModeVariableProcessor.calculateMistakes(lastmistaken, previousMistakes);
		
		assertEquals(previousMistakes+1, mistakes);
	}
	
	@Test
	public void shouldCalculateMistakesWhenWasNotLastmistake() throws Exception {
		boolean lastmistaken = false;
		int previousMistakes = 12;
		
		int mistakes = expressionModeVariableProcessor.calculateMistakes(lastmistaken, previousMistakes);
		
		assertEquals(previousMistakes, mistakes);
	}
	
	@Test
	public void shouldEvaluateAnswersWhenExpressionIsCorrect() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.CORRECT);
		
		List<Boolean> answersEvaluation = expressionModeVariableProcessor.evaluateAnswers(response);
		
		List<Boolean> expectedResult = Lists.newArrayList(true);
		assertEquals(expectedResult, answersEvaluation);
	}
	
	@Test
	public void shouldEvaluateAnswersWhenExpressionIsWrong() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.WRONG);
		
		List<Boolean> answersEvaluation = expressionModeVariableProcessor.evaluateAnswers(response);
		
		List<Boolean> expectedResult = Lists.newArrayList(false);
		assertEquals(expectedResult, answersEvaluation);
	}
	
	@Test
	public void shouldEvaluateAnswersWhenExpressionHaveUnsetValues() throws Exception {
		expectExpressionEvaluationCalls(ExpressionEvaluationResult.VALUES_NOT_SET);
		
		List<Boolean> answersEvaluation = expressionModeVariableProcessor.evaluateAnswers(response);
		
		List<Boolean> expectedResult = Lists.newArrayList(false);
		assertEquals(expectedResult, answersEvaluation);
	}

	private void expectExpressionEvaluationCalls(ExpressionEvaluationResult expressionEvaluationResult) {
		ExpressionBean expression = new ExpressionBean();
		when(response.getExpression())
			.thenReturn(expression);
		
		when(expressionEvaluationController.evaluateExpression(expression))
			.thenReturn(expressionEvaluationResult);
	}
	
}
