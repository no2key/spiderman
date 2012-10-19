package org.eweb4j.spiderman.xml;

import org.eweb4j.util.xml.AttrTag;

public class Field {

	@AttrTag
	private String name;
	
	@AttrTag
	private String isArray;
	
	private Parser parser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsArray() {
		return isArray;
	}

	public void setIsArray(String isArray) {
		this.isArray = isArray;
	}

	public Parser getParser() {
		return parser;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}
	
}
