package eu.ydp.empiria.player.client.module.video.view;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import eu.ydp.empiria.player.client.module.video.VideoPlayerControl;
import eu.ydp.empiria.player.client.module.video.wrappers.VideoElementWrapper;
import eu.ydp.gwtutil.client.util.UserAgentUtil;

public class VideoPlayer extends Widget {

	private final VideoPlayerNative nativePlayer;
	private final VideoElementWrapper videoElementWrapper;
	private final UserAgentUtil userAgentUtil;

	@Inject
	public VideoPlayer(@Assisted VideoElementWrapper videoElementWrapper, VideoPlayerNative nativePlayer, UserAgentUtil userAgentUtil) {
		this.nativePlayer = nativePlayer;
		this.videoElementWrapper = videoElementWrapper;
		this.userAgentUtil = userAgentUtil;
		setElement(Document.get().createDivElement());
	}

	@Override
	protected void onLoad() {
		getElement().appendChild(videoElementWrapper.asNode());

		initializeNativePlayer();
	}

	private void initializeNativePlayer() {
		String playerId = videoElementWrapper.getId();
		nativePlayer.initPlayer(playerId);

		if (userAgentUtil.isAndroidBrowser() && userAgentUtil.isAIR()) {
			nativePlayer.disablePointerEvents();
		}
	}

	public VideoPlayerControl getControl() {
		return nativePlayer;
	}

	@Override
	protected void onUnload() {
		nativePlayer.disposeCurrentPlayer();
	}

	public String getId() {
		return videoElementWrapper.getId();
	}

	public List<String> getSources() {
		return videoElementWrapper.getSources();
	}
}
