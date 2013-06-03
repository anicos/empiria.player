package eu.ydp.empiria.player.client.controller.extensions.internal.sound;

import static eu.ydp.gwtutil.client.util.MediaChecker.isHtml5Mp3Support;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.media.client.Audio;
import com.google.gwt.media.client.Video;
import com.google.inject.Inject;
import com.google.inject.Provider;

import eu.ydp.empiria.player.client.controller.extensions.ExtensionType;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.LocalSwfMediaExecutor;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.LocalSwfMediaWrapper;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.OldSwfMediaExecutor;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.OldSwfMediaWrapper;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.SwfMediaWrapper;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.external.ExternalFullscreenVideoAvailability;
import eu.ydp.empiria.player.client.controller.extensions.internal.media.external.FullscreenVideoExecutor;
import eu.ydp.empiria.player.client.controller.extensions.internal.sound.factory.HTML5MediaExecutorFactory;
import eu.ydp.empiria.player.client.gin.factory.MediaWrappersPairFactory;
import eu.ydp.empiria.player.client.inject.Instance;
import eu.ydp.empiria.player.client.module.media.BaseMediaConfiguration;
import eu.ydp.empiria.player.client.module.media.BaseMediaConfiguration.MediaType;
import eu.ydp.empiria.player.client.module.media.MediaWrapper;
import eu.ydp.empiria.player.client.module.media.MediaWrappersPair;
import eu.ydp.empiria.player.client.module.object.impl.ExternalFullscreenVideoImpl;
import eu.ydp.empiria.player.client.module.object.impl.HTML5AudioImpl;
import eu.ydp.empiria.player.client.module.object.impl.Media;
import eu.ydp.empiria.player.client.util.SourceUtil;
import eu.ydp.empiria.player.client.util.events.callback.CallbackRecevier;
import eu.ydp.empiria.player.client.util.events.player.PlayerEvent;
import eu.ydp.gwtutil.client.util.UserAgentChecker;

public class DefaultMediaProcessorExtension extends AbstractMediaProcessor {
	protected Set<MediaWrapper<?>> mediaSet = new HashSet<MediaWrapper<?>>();
	protected boolean initialized;

	@Inject private MediaWrappersPairFactory pairFactory;
	@Inject private MediaExecutorsStopper mediaExecutorsStopper;

	@Inject private Instance<HTML5MediaExecutorFactory> html5MediaExecutorFactoryProvider;
	
	@Inject	private ExternalFullscreenVideoAvailability externalFullscreenVideoAvailability;
	@Inject private Provider<FullscreenVideoExecutor> fullscreenVideoExecutorProvider;

	@Override
	public void initMediaProcessor() {
		if (!initialized) {
			initEvents();
			initialized = true;
		}
	}

	@Override
	public ExtensionType getType() {
		return ExtensionType.EXTENSION_LISTENER_DELIVERY_EVENTS;
	}

	@Override
	protected void pauseAllOthers(MediaWrapper<?> mediaWrapper) {
		forceStop(mediaWrapper);
	}

	@Override
	protected void pauseAll() {
		forceStop(null);
	}
	
	protected void forceStop(MediaWrapper<?> mw) {
		mediaExecutorsStopper.forceStop(mw, getMediaExecutors().values());
	}
	

	/**
	 * tworzy obiekt wrappera oraz executora
	 *
	 * @param event
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void createMediaWrapper(PlayerEvent event) {
		if (event.getValue() instanceof BaseMediaConfiguration) {
			BaseMediaConfiguration bmc = (BaseMediaConfiguration) event.getValue();
			Media defaultMedia = null;
			Media fullScreenMedia = null;
			boolean geckoSupport = isGeckoSupport(bmc);

			if (bmc.getMediaType() == MediaType.VIDEO && externalFullscreenVideoAvailability.isAvailable()){
				defaultMedia = new ExternalFullscreenVideoImpl();
			} else if (bmc.getMediaType() == MediaType.VIDEO && Video.isSupported() && geckoSupport) {
				defaultMedia = GWT.create(eu.ydp.empiria.player.client.module.object.impl.Video.class);
				if (bmc.isFullScreenTemplate()) {
					fullScreenMedia = GWT.create(eu.ydp.empiria.player.client.module.object.impl.Video.class);
				}
			} else if (Audio.isSupported() && geckoSupport) {
				defaultMedia = new HTML5AudioImpl();
			}

			MediaExecutor<?> executor;
			MediaExecutor<?> fullScreenExecutor = null;
			if (bmc.getMediaType() == MediaType.VIDEO  &&  externalFullscreenVideoAvailability.isAvailable()){
				executor = fullscreenVideoExecutorProvider.get();
			} else if (!UserAgentChecker.isLocal() && defaultMedia == null) {
				if (bmc.isTemplate() || bmc.isFeedback()) {
					if (bmc.getMediaType() == MediaType.VIDEO) {
						executor = createSWFVideoMediaExecutor();
						if (bmc.isFullScreenTemplate()) {
							fullScreenExecutor = createSWFVideoMediaExecutor();
						}
					} else {
						executor = createSWFSoundMediaExecutor();
					}
				} else {
					OldSwfMediaExecutor exc = new OldSwfMediaExecutor();
					exc.setMediaWrapper(new OldSwfMediaWrapper());
					executor = exc;
				}
			} else if (defaultMedia == null && UserAgentChecker.isLocal()) {
				executor = new LocalSwfMediaExecutor();
				executor.setMediaWrapper((MediaWrapper) new LocalSwfMediaWrapper());
			} else {
				executor = createHTML5MediaExecutor(defaultMedia, bmc.getMediaType() );
				fullScreenExecutor = createHTML5MediaExecutor(fullScreenMedia,bmc.getMediaType() );
			}

			initExecutor(executor, bmc);
			initExecutor(fullScreenExecutor, bmc);
			fireCallback(event, executor, fullScreenExecutor);
		}
	}

	private MediaExecutor<?> createHTML5MediaExecutor(Media defaultMedia, MediaType mediaType) {
		return html5MediaExecutorFactoryProvider.get().createMediaExecutor(defaultMedia, mediaType);
	}

	private void fireCallback(PlayerEvent event, MediaExecutor<?> defaultMediaExecutor, MediaExecutor<?> fullScreenMediaExecutor) {
		if (event.getSource() instanceof CallbackRecevier) {
			if (fullScreenMediaExecutor == null) {
				((CallbackRecevier) event.getSource()).setCallbackReturnObject(defaultMediaExecutor.getMediaWrapper());
			} else {
				MediaWrappersPair pair = pairFactory.getMediaWrappersPair(defaultMediaExecutor.getMediaWrapper(), fullScreenMediaExecutor.getMediaWrapper());
				((CallbackRecevier) event.getSource()).setCallbackReturnObject(pair);
			}
		}
	}

	private boolean isGeckoSupport(BaseMediaConfiguration bmc) {
		boolean containsOgg = SourceUtil.containsOgg(bmc.getSources());
		return containsOgg  ||  isHtml5Mp3Support();
	}

	private MediaExecutor<?> createSWFVideoMediaExecutor() {
		VideoExecutorSwf executor = new VideoExecutorSwf();
		executor.setMediaWrapper(new SwfMediaWrapper());
		return executor;
	}

	private MediaExecutor<?> createSWFSoundMediaExecutor() {
		SoundExecutorSwf executor = new SoundExecutorSwf();
		executor.setMediaWrapper(new SwfMediaWrapper());
		return executor;
	}

}
