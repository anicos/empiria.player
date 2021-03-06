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

package eu.ydp.empiria.player.client.controller.extensions.internal.sound;

import eu.ydp.empiria.player.client.module.media.MediaWrapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class MediaExecutorsStopperTest {

    private MediaExecutorsStopper testObj = new MediaExecutorsStopper();

    @SuppressWarnings("unused")
    private Object[] parametersForForceStop() {
        MediaWrapper<?> currentMediaWrapper = createWrapper(false);
        MediaWrapper<?> currentMediaWrapperPauseSupport = createWrapper(false);
        return $($(null, createExecutor(null), ExpectedAction.NOOP), $(createWrapper(false), createExecutor(null), ExpectedAction.STOP),
                $(createWrapper(true), createExecutor(null), ExpectedAction.STOP), $(null, createExecutor(createWrapper(false)), ExpectedAction.STOP),
                $(null, createExecutor(createWrapper(true)), ExpectedAction.PAUSE),
                $(createWrapper(false), createExecutor(createWrapper(false)), ExpectedAction.STOP),
                $(createWrapper(false), createExecutor(createWrapper(true)), ExpectedAction.PAUSE),
                $(createWrapper(true), createExecutor(createWrapper(false)), ExpectedAction.STOP),
                $(createWrapper(true), createExecutor(createWrapper(true)), ExpectedAction.PAUSE),
                $(currentMediaWrapper, createExecutor(currentMediaWrapper), ExpectedAction.NOOP),
                $(currentMediaWrapperPauseSupport, createExecutor(currentMediaWrapperPauseSupport), ExpectedAction.NOOP));
    }

    @SuppressWarnings("rawtypes")
    private MediaExecutor createExecutor(MediaWrapper<?> mediaWrapper) {
        MediaExecutor exec = mock(MediaExecutor.class);
        stub(exec.getMediaWrapper()).toReturn(mediaWrapper);
        return exec;
    }

    private MediaWrapper<?> createWrapper(boolean withPauseSupport) {
        MediaWrapper<?> mediaWrapper = mock(MediaWrapper.class, RETURNS_DEEP_STUBS);
        stub(mediaWrapper.getMediaAvailableOptions().isPauseSupported()).toReturn(withPauseSupport);
        return mediaWrapper;
    }

    private static enum ExpectedAction {
        NOOP, STOP, PAUSE
    }

    @Test
    @Parameters
    public void forceStop(MediaWrapper<?> currentMediaWrapper, MediaExecutor<?> executor, ExpectedAction result) {
        // given
        List<MediaExecutor<?>> executors = Arrays.asList(new MediaExecutor<?>[]{executor});

        // when
        testObj.forceStop(currentMediaWrapper, executors);

        // then
        assertResult(executor, result);
    }

    private void assertResult(MediaExecutor<?> executor, ExpectedAction result) {
        switch (result) {
            case NOOP:
                verify(executor, never()).stop();
                verify(executor, never()).pause();
                break;

            case STOP:
                verify(executor).stop();
                verify(executor, never()).pause();
                break;

            case PAUSE:
                verify(executor, never()).stop();
                verify(executor).pause();
                break;

        }
    }

}
