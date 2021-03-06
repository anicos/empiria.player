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

package eu.ydp.empiria.player.client.module.slideshow.sound;

import com.google.gwt.thirdparty.guava.common.collect.Maps;
import com.google.gwt.user.client.ui.Widget;
import eu.ydp.empiria.player.client.media.MediaWrapperCreator;
import eu.ydp.empiria.player.client.module.media.MediaWrapper;
import eu.ydp.empiria.player.client.module.media.MimeSourceProvider;
import eu.ydp.empiria.player.client.util.events.internal.callback.CallbackReceiver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SlideshowSoundsTest {

    @InjectMocks
    private SlideshowSounds testObj;
    @Mock
    private MediaWrapperCreator mediaWrapperCreator;
    @Mock
    private MimeSourceProvider mimeSourceProvider;
    @Mock
    private MediaWrapper<Widget> mediaWrapper;

    @Captor
    private ArgumentCaptor<CallbackReceiver<MediaWrapper<Widget>>> callbackReceiverCaptor;

    private final Map<String, String> sourceWithType = Maps.newHashMap();

    @Test
    public void shouldAddSoundToList_whenSoundIsNotOnList() {
        // given
        String filepath = "test.mp3";
        String filepath2 = "test2.mp3";

        // when
        addSound(filepath);
        addSound(filepath2);

        MediaWrapper<Widget> result = testObj.getSound(filepath);

        // then
        assertThat(result).isEqualTo(mediaWrapper);
    }

    @Test
    public void shouldNotCreateSound_whenSoundIsOnList() {
        // given
        String filepath = "test.mp3";

        // when;
        addSound(filepath);
        addSound(filepath);

        // then
        verify(mimeSourceProvider, times(1)).getSourcesWithTypeByExtension(filepath);
        verify(mediaWrapperCreator, times(1)).createMediaWrapper(eq(sourceWithType), callbackReceiverCaptor.capture());
    }

    @Test
    public void shouldReturnAllSounds() {
        // given
        addSound("test.mp3");

        // when
        Collection<MediaWrapper<Widget>> result = testObj.getAllSounds();

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void shouldReturnTrue_whenContainsSound() {
        // given
        addSound("test.mp3");

        // when
        boolean result = testObj.containsWrapper(mediaWrapper);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnFalse_whenDoesNotContainsSound() {
        // given
        addSound("test.mp3");
        MediaWrapper<Widget> otherMediaWrapper = mock(MediaWrapper.class);

        // when
        boolean result = testObj.containsWrapper(otherMediaWrapper);

        // then
        assertThat(result).isFalse();

    }

    private void addSound(String filepath) {
        sourceWithType.put(filepath, "audio/mp4");
        when(mimeSourceProvider.getSourcesWithTypeByExtension(filepath)).thenReturn(sourceWithType);
        testObj.initSound(filepath);

        verify(mimeSourceProvider).getSourcesWithTypeByExtension(filepath);
        verify(mediaWrapperCreator, atLeastOnce()).createMediaWrapper(eq(sourceWithType), callbackReceiverCaptor.capture());

        callbackReceiverCaptor.getValue().setCallbackReturnObject(mediaWrapper);
    }
}
