package eu.ydp.empiria.player.client.module.external.common.state;

import com.google.gwt.core.client.JavaScriptObject;

import javax.inject.Singleton;

@Singleton
public class ExternalFrameObjectFixer {

    public native JavaScriptObject fix(JavaScriptObject object) /*-{
        return JSON.parse(JSON.stringify(object));
    }-*/;
}
