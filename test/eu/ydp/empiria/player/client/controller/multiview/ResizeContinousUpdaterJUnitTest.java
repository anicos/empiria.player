package eu.ydp.empiria.player.client.controller.multiview;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import eu.ydp.empiria.player.client.controller.multiview.ResizeContinousUpdater.ResizeTimerState;
import eu.ydp.empiria.player.client.gin.factory.PageScopeFactory;
import eu.ydp.empiria.player.client.test.utils.ReflectionsUtils;
import eu.ydp.empiria.player.client.util.events.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.scope.CurrentPageScope;

public class ResizeContinousUpdaterJUnitTest {

	private ResizeContinousUpdater resizeContinousUpdater;
	private ReflectionsUtils reflectionsUtils = new ReflectionsUtils();
	private PageScopeFactory pageScopeFactory;
	private EventsBus eventsBus;
	private MultiPageController pageView;
	private Integer currentVisiblePage = 3;
	
	@Before
	public void setUp() throws Exception {
		pageView = Mockito.mock(MultiPageController.class);
		eventsBus = Mockito.mock(EventsBus.class);
		pageScopeFactory = Mockito.mock(PageScopeFactory.class);
		
		resizeContinousUpdater = new ResizeContinousUpdater(pageScopeFactory, eventsBus, pageView);
	}

	@Test
	public void testWaitForContentAndKeepWaiting() throws Exception {
		int pageHeight = 0;
		expectPageViewInteractions(pageHeight);
		
		setTimerState(ResizeTimerState.WaitingForContent, 0, 0, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.WaitingForContent, 0, 0, 0);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
	}
	
	@Test
	public void testWaitForContentAndChangeToPageIsGrowing() throws Exception {
		int pageHeight = 123;
		expectPageViewInteractions(pageHeight);
		
		setTimerState(ResizeTimerState.WaitingForContent, 0, 0, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageIsGrowing, pageHeight, 0, 0);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
		verifyPageContainerResizeCalls(pageHeight);
	}
	
	@Test
	public void testPageIsStillGrowing() throws Exception {
		int newPageHeight = 123;
		int previousPageHeight = 30;
		expectPageViewInteractions(newPageHeight);
		
		setTimerState(ResizeTimerState.PageIsGrowing, previousPageHeight, 0, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageIsGrowing, newPageHeight, 0, 0);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
		verifyPageContainerResizeCalls(newPageHeight);
	}
	
	@Test
	public void testPageWasGrowingAndStopped() throws Exception {
		int newPageHeight = 123;
		int previousPageHeight = 123;
		expectPageViewInteractions(newPageHeight);
		
		setTimerState(ResizeTimerState.PageIsGrowing, previousPageHeight, 0, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageIsGrowing, newPageHeight, 0, 1);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
	}
	
	@Test
	public void testPageWasGrowingAndStopped_shouldChangeStateToPageStopedGrowing() throws Exception {
		int newPageHeight = 123;
		int previousPageHeight = 123;
		expectPageViewInteractions(newPageHeight);
		setTimerState(ResizeTimerState.PageIsGrowing, previousPageHeight, 0, ResizeContinousUpdater.WAIT_STOP_GROWING_ITERATIONS);
		
		CurrentPageScope currentPageScope = Mockito.mock(CurrentPageScope.class);
		when(pageScopeFactory.getCurrentPageScope())
			.thenReturn(currentPageScope);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageStopedGrowing, newPageHeight, 0, ResizeContinousUpdater.WAIT_STOP_GROWING_ITERATIONS+1);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
		verifyPageResizedEventCorrectlyThrown(currentPageScope);
	}
	
	@Test
	public void testPageWasNotGrowingAndStartedGrowing() throws Exception {
		int newPageHeight = 123;
		int previousPageHeight = 30;
		expectPageViewInteractions(newPageHeight);
		setTimerState(ResizeTimerState.PageStopedGrowing, previousPageHeight, 0, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageIsGrowing, newPageHeight, 0, 0);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
	}
	
	@Test
	public void testPageWasNotGrowingAndStillNotGrowing_schouldScheduleShorterTime() throws Exception {
		int newPageHeight = 123;
		int previousPageHeight = 123;
		expectPageViewInteractions(newPageHeight);
		setTimerState(ResizeTimerState.PageStopedGrowing, previousPageHeight, 0, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageStopedGrowing, newPageHeight, 1, 0);
		assertEquals(ResizeContinousUpdater.DELAY_MILLIS, rescheduleTime);
	}
	
	@Test
	public void testPageWasNotGrowingAndStillNotGrowingForLongTime_schouldScheduleIdleTime() throws Exception {
		int newPageHeight = 123;
		int previousPageHeight = 123;
		expectPageViewInteractions(newPageHeight);
		setTimerState(ResizeTimerState.PageStopedGrowing, previousPageHeight, ResizeContinousUpdater.REPEAT_COUNT, 0);
		
		int rescheduleTime = resizeContinousUpdater.runContinousResizeUpdateAndReturnRescheduleTime();
		
		validateTimerState(ResizeTimerState.PageStopedGrowing, newPageHeight, ResizeContinousUpdater.REPEAT_COUNT+1, 0);
		assertEquals(ResizeContinousUpdater.IDLE_DELAY_MILLIS, rescheduleTime);
	}
	
	@Test
	public void testResetFunction() throws Exception {
		setTimerState(ResizeTimerState.PageStopedGrowing, 132, 123, 123);
		
		resizeContinousUpdater.reset();
		
		validateTimerState(ResizeTimerState.WaitingForContent, 0, 0, 0);
	}

	private void verifyPageResizedEventCorrectlyThrown(CurrentPageScope currentPageScope) {
		verify(pageScopeFactory).getCurrentPageScope();
		verify(eventsBus).fireAsyncEvent(resizeContinousUpdater.PAGE_CONTENT_RESIZED_EVENT, currentPageScope);
	}

	private void verifyPageContainerResizeCalls(int pageHeight) {
		Mockito.verify(pageView).setHeight(pageHeight);
		Mockito.verify(pageView).hideProgressBarForPage(currentVisiblePage);
	}

	private void expectPageViewInteractions(int pageHeight) {
		when(pageView.getCurrentVisiblePage())
			.thenReturn(currentVisiblePage);
		
		when(pageView.getHeightForPage(currentVisiblePage))
			.thenReturn(pageHeight);
	}
	
	@After
	public void tearDown() throws Exception {
	}

	private void setTimerState(ResizeContinousUpdater.ResizeTimerState timerState, int previousPageHeight, int resizeCounter, int pageStopedGrowingCounter){
		try{
			reflectionsUtils.setValueInObjectOnField("timerState", resizeContinousUpdater, timerState);
			reflectionsUtils.setValueInObjectOnField("previousPageHeight", resizeContinousUpdater, previousPageHeight);
			reflectionsUtils.setValueInObjectOnField("resizeCounter", resizeContinousUpdater, resizeCounter);
			reflectionsUtils.setValueInObjectOnField("pageStopedGrowingCounter", resizeContinousUpdater, pageStopedGrowingCounter);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private void validateTimerState(ResizeContinousUpdater.ResizeTimerState timerState, int previousPageHeight, int resizeCounter, int pageStopedGrowingCounter) throws Exception{
		ResizeContinousUpdater.ResizeTimerState currentTimerState = (ResizeTimerState) reflectionsUtils.getValueFromFiledInObject("timerState", resizeContinousUpdater);
		int currentPreviousPageHeight = (Integer) reflectionsUtils.getValueFromFiledInObject("previousPageHeight", resizeContinousUpdater);
		int currentResizeCounter = (Integer) reflectionsUtils.getValueFromFiledInObject("resizeCounter", resizeContinousUpdater);
		int currentPageStopedGrowingCounter = (Integer) reflectionsUtils.getValueFromFiledInObject("pageStopedGrowingCounter", resizeContinousUpdater);
		
		assertEquals(timerState, currentTimerState);
		assertEquals(previousPageHeight, currentPreviousPageHeight);
		assertEquals(resizeCounter, currentResizeCounter);
		assertEquals(pageStopedGrowingCounter, currentPageStopedGrowingCounter);
	}
	
}
