package eu.ydp.empiria.player.client.style;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

import eu.ydp.empiria.player.client.util.js.JSOModel;

public class StyleDocument {

	protected JavaScriptObject styleSheet;
	protected String basePath;

	public StyleDocument(JavaScriptObject styleSheet, String basePath) {
		this.styleSheet = styleSheet;
		int index1 = basePath.lastIndexOf('\\');
		int index2 = basePath.lastIndexOf('/');
		if (index1 > index2) {
			this.basePath = basePath.substring(0, index1 + 1);
		} else if (index2 > index1) {
			this.basePath = basePath.substring(0, index2 + 1);
		} else {
			this.basePath = basePath;
		}
	}

	public JSOModel getDeclarationsForSelectors(List<String> selectors, JSOModel model) {
		JsCssModel jsSheet = styleSheet.cast();
		JSOModel result = jsSheet.getDeclarationsForSelectors(selectors, model);
		JsArrayString keys = result.keys();
		for (int i = 0; i < keys.length(); i++) {
			String key = keys.get(i);
			String value = result.get(key);
			if (value.trim().toLowerCase().startsWith("url(")) {
				String value2 = value.trim().toLowerCase();
				String path = value2.substring(value2.indexOf("url(") + 4, value2.lastIndexOf(')'));
				path = path.trim();
				if ((path.charAt(0) == '\"' && path.endsWith("\"")) || (path.charAt(0) == '/' && path.endsWith("/"))) {
					path = path.substring(1, path.length() - 1);
				}
				path = basePath + path;
				result.set(key, path);
			}
		}
		return result;
	}
	
	public String getBasePath(){
		return basePath;
	}

}
