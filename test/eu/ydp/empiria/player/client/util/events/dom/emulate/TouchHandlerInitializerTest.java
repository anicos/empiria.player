package eu.ydp.empiria.player.client.util.events.dom.emulate;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.pointer.PointerDownHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.pointer.PointerMoveHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.pointer.PointerUpHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touch.TouchCancelHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touch.TouchEndHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touch.TouchMoveHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touch.TouchStartHandlerImpl;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touchon.TouchOnCancelHandler;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touchon.TouchOnEndHandler;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touchon.TouchOnMoveHandler;
import eu.ydp.empiria.player.client.util.events.dom.emulate.handlers.touchon.TouchOnStartHandler;
import eu.ydp.empiria.player.client.util.events.dom.emulate.iepointer.events.PointerDownEvent;
import eu.ydp.empiria.player.client.util.events.dom.emulate.iepointer.events.PointerMoveEvent;
import eu.ydp.empiria.player.client.util.events.dom.emulate.iepointer.events.PointerUpEvent;
import eu.ydp.gwtutil.client.util.UserAgentUtil;

@RunWith(GwtMockitoTestRunner.class)
public class TouchHandlerInitializerTest {

	@InjectMocks
	private TouchHandlersInitializer testObj;

	@Mock
	private UserAgentUtil userAgentUtil;

	@Mock
	private Widget listenOn;

	@Test
	public void shouldAddTouchMoveHandler_ifNotIE() {
		// given
		TouchOnMoveHandler touchOnMoveHandler = mock(TouchOnMoveHandler.class);

		// when
		testObj.addTouchMoveHandler(touchOnMoveHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(TouchMoveHandlerImpl.class), eq(TouchMoveEvent.getType()));
	}

	@Test
	public void shouldAddTouchMoveHandler_ifIE() {
		// given
		TouchOnMoveHandler touchOnMoveHandler = mock(TouchOnMoveHandler.class);
		when(userAgentUtil.isIE()).thenReturn(true);

		// when
		testObj.addTouchMoveHandler(touchOnMoveHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(PointerMoveHandlerImpl.class), eq(PointerMoveEvent.getType()));
	}

	@Test
	public void shouldAddTouchStartHandler_ifNotIE() {
		// given
		TouchOnStartHandler touchOnStartHandler = mock(TouchOnStartHandler.class);

		// when
		testObj.addTouchStartHandler(touchOnStartHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(TouchStartHandlerImpl.class), eq(TouchStartEvent.getType()));
	}

	@Test
	public void shouldAddTouchStartHandler_ifIE() {
		// given
		TouchOnStartHandler touchOnStartHandler = mock(TouchOnStartHandler.class);
		when(userAgentUtil.isIE()).thenReturn(true);

		// when
		testObj.addTouchStartHandler(touchOnStartHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(PointerDownHandlerImpl.class), eq(PointerDownEvent.getType()));
	}

	@Test
	public void shouldAddTouchEndHandler_ifNotIE() {
		// given
		TouchOnEndHandler touchOnStartHandler = mock(TouchOnEndHandler.class);

		// when
		testObj.addTouchEndHandler(touchOnStartHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(TouchEndHandlerImpl.class), eq(TouchEndEvent.getType()));
	}

	@Test
	public void shouldAddTouchEndHandler_ifIE() {
		// given
		TouchOnEndHandler touchOnStartHandler = mock(TouchOnEndHandler.class);
		when(userAgentUtil.isIE()).thenReturn(true);

		// when
		testObj.addTouchEndHandler(touchOnStartHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(PointerUpHandlerImpl.class), eq(PointerUpEvent.getType()));
	}

	@Test
	public void shouldAddTouchCancelHandler() {
		// given
		TouchOnCancelHandler touchOnStartHandler = mock(TouchOnCancelHandler.class);

		// when
		testObj.addTouchCancelHandler(touchOnStartHandler, listenOn);

		// then
		verify(listenOn).addDomHandler(any(TouchCancelHandlerImpl.class), eq(TouchCancelEvent.getType()));
	}
}
