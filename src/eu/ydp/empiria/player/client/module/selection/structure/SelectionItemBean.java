package eu.ydp.empiria.player.client.module.selection.structure;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import eu.ydp.empiria.player.client.module.abstractmodule.structure.HasFixed;
import eu.ydp.gwtutil.client.StringUtils;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "item")
public class SelectionItemBean implements HasFixed{

	@XmlAttribute
	private int matchMax;
	@XmlAttribute
	private String identifier=StringUtils.EMPTY_STRING;
	@XmlValue
	private String value;
	@XmlAttribute
	private boolean fixed;

	public int getMatchMax() {
		return matchMax;
	}

	public void setMatchMax(int matchMax) {
		this.matchMax = matchMax;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
}