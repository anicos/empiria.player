package eu.ydp.empiria.player.client.controller.extensions.internal.state;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;
import eu.ydp.empiria.player.client.compressor.LzGwtWrapper;
import eu.ydp.empiria.player.client.controller.extensions.internal.state.json.EmpiriaStateDeserializer;
import eu.ydp.empiria.player.client.controller.extensions.internal.state.json.JsonParserWrapper;

import javax.inject.Singleton;

@Singleton
public class EmpiriaStateImportCreator {

    private final EmpiriaStateDeserializer empiriaStateDeserializer;
    private final LzGwtWrapper lzGwtWrapper;
    private final JsonParserWrapper jsonParser;

    @Inject
    public EmpiriaStateImportCreator(EmpiriaStateDeserializer empiriaStateDeserializer, LzGwtWrapper lzGwtWrapper, JsonParserWrapper jsonParser) {
        this.empiriaStateDeserializer = empiriaStateDeserializer;
        this.lzGwtWrapper = lzGwtWrapper;
        this.jsonParser = jsonParser;
    }

    public String createState(String state) {
        JSONValue jsonValue = jsonParser.parse(state);
        EmpiriaState empiriaState = empiriaStateDeserializer.deserialize(jsonValue);

        if (empiriaState.getFormatType().equals(EmpiriaStateType.DEFAULT)) {
            return empiriaState.getState();
        } else {
            String stateString = empiriaState.getState();
            return lzGwtWrapper.decompress(stateString);
        }
    }
}