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

package eu.ydp.empiria.player.client.module.dictionary.external.controller;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import eu.ydp.empiria.player.client.module.dictionary.external.model.Entry;
import eu.ydp.empiria.player.client.module.dictionary.external.model.EntryFactory;
import eu.ydp.gwtutil.client.debug.log.Logger;
import eu.ydp.jsfilerequest.client.FileRequest;
import eu.ydp.jsfilerequest.client.FileRequestCallback;
import eu.ydp.jsfilerequest.client.FileResponse;

public class DictionaryFileRequestCallback implements FileRequestCallback {

    private static final int ENTRIES_PER_FILE = 50;
    private final int index;
    private final ExplanationListener explanationListener;
    private final Logger logger;
    private final EntryFactory entryFactory;

    @Inject
    public DictionaryFileRequestCallback(@Assisted int index, ExplanationListener explanationListener,
                                         EntryFactory entryFactory, Logger logger) {

        this.index = index;
        this.explanationListener = explanationListener;
        this.entryFactory = entryFactory;
        this.logger = logger;
    }

    @Override
    public void onResponseReceived(FileRequest request, FileResponse response) {
        Entry entry = createEntryFromResponse(response);

        explanationListener.onEntryLoaded(entry);
    }

    private Entry createEntryFromResponse(FileResponse response) {
        String responseText = response.getText();
        int positionInFile = index % ENTRIES_PER_FILE;

        return entryFactory.createEntryFromXMLString(responseText, positionInFile);
    }

    @Override
    public void onError(FileRequest request, Throwable exception) {
        logger.error(exception);
    }
}
