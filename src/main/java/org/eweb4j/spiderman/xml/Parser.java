package org.eweb4j.spiderman.xml;

import org.eweb4j.util.xml.AttrTag;

public class Parser {

	@AttrTag
	private String xpath;
	
	@AttrTag
	private String attribute;
	
	@AttrTag
	private String regex;

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

}
