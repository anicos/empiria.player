package eu.ydp.empiria.player.client.module.external;

import eu.ydp.empiria.player.client.structure.ModuleBean;
import eu.ydp.gwtutil.client.StringUtils;
import javax.xml.bind.annotation.XmlAttribute;

public class ExternalInteractionModuleBean extends ModuleBean {

	@XmlAttribute(name = "src")
	private String responseIdentifier = StringUtils.EMPTY_STRING;

	@XmlAttribute(name = "todo")
	private Integer todo = 0;

	public String getResponseIdentifier() {
		return responseIdentifier;
	}

	public void setResponseIdentifier(String responseIdentifier) {
		this.responseIdentifier = responseIdentifier;
	}

	public Integer getTodo() {
		return todo;
	}

	public void setTodo(Integer todo) {
		this.todo = todo;
	}
}
