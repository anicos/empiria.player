package eu.ydp.empiria.player.client.module.video.presenter;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.ydp.empiria.player.client.module.video.view.VideoView;

@RunWith(MockitoJUnitRunner.class)
public class VideoPresenterTest {

	@InjectMocks
	private VideoPresenter presenter;
	@Mock
	private VideoPlayerReattacher reAttachHack;
	@Mock
	private VideoPlayerBuilder videoPlayerBuilder;
	@Mock
	private VideoView view;

	@Test
	public void shouldCreateViewAndApplyHackWhenStart() {
		// when
		presenter.start();

		// then
		verify(view).createView();
		verify(videoPlayerBuilder).build();
		verify(reAttachHack).apply(view);
	}
	
}
