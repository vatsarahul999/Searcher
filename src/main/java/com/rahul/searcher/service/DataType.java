package com.rahul.searcher.service;

public enum DataType {
	S("String"),I("int"),D("double");
	public String value;
	private DataType(String value) {
		this.value = value;
	}

}
