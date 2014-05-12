package eu.ydp.empiria.player.client.module.info.handler;


import eu.ydp.empiria.player.client.controller.session.datasockets.ItemSessionDataSocket;
import eu.ydp.empiria.player.client.controller.session.datasupplier.SessionDataSupplier;
import eu.ydp.empiria.player.client.controller.variables.ResultExtractorsFactory;
import eu.ydp.empiria.player.client.controller.variables.VariableProviderSocket;
import eu.ydp.empiria.player.client.controller.variables.VariableResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ResultForPageIndexProviderTest {

	@InjectMocks
	private ResultForPageIndexProvider testObj;

	@Mock
	private ResultExtractorsFactory variableResultFactory;
	@Mock
	private SessionDataSupplier sessionDataSupplier;
	@Mock
	private ItemSessionDataSocket itemSessionDataSocket;
	@Mock
	private VariableProviderSocket variableProviderSocket;
	@Mock
	private VariableResult variableResult;

	private static final int EXPECTED_RESULT = 10;
	private int pageIndex = 1;

	@Before
	public void setUp() {
		when(sessionDataSupplier.getItemSessionDataSocket(pageIndex)).thenReturn(itemSessionDataSocket);
		when(itemSessionDataSocket.getVariableProviderSocket()).thenReturn(variableProviderSocket);
		when(variableResultFactory.createVariableResult(variableProviderSocket)).thenReturn(variableResult);
		when(variableResult.getResult()).thenReturn(EXPECTED_RESULT);
	}

	@Test
	public void shouldGetCorrectResultForIndex() {
		// given

		// when
		int actualResult = testObj.get(pageIndex);

		// then
		assertThat(actualResult).isEqualTo(EXPECTED_RESULT);
	}
}