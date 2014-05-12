package eu.ydp.empiria.player.client.module.report.table.extraction;

import static eu.ydp.empiria.player.client.resources.EmpiriaStyleNameConstants.EMPIRIA_REPORT_SHOW_NON_ACTIVITES;

import java.util.Map;

import com.google.gwt.xml.client.Element;

import eu.ydp.empiria.player.client.style.StyleSocket;

public class ShowNonActivitiesExtractor {

	private final StyleSocket styleSocket;

	public ShowNonActivitiesExtractor(StyleSocket styleSocket) {
		this.styleSocket = styleSocket;
	}

	public boolean extract(Element element) {
		Map<String, String> styles = styleSocket.getStyles(element);
		if (styles.containsKey(EMPIRIA_REPORT_SHOW_NON_ACTIVITES)) {
			return Boolean.parseBoolean(styles.get(EMPIRIA_REPORT_SHOW_NON_ACTIVITES));
		}
		return true;
	}
}