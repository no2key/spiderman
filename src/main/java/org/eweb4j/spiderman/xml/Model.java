package org.eweb4j.spiderman.xml;

import java.util.ArrayList;
import java.util.List;

import org.eweb4j.util.xml.AttrTag;

public class Model {

	@AttrTag
	private String clazz ;
	
	private List<Field> field = new ArrayList<Field>();

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public List<Field> getField() {
		return field;
	}

	public void setField(List<Field> field) {
		this.field = field;
	}
	
}
