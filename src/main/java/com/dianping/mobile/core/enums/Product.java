package com.dianping.mobile.core.enums;

public enum Product {
	API(0, "api"),
	YELLOWPAGE(100, "yellowpage"),
	GROUPON(200, "groupon"),
	BOOKING(300, "booking"),
	WAP(400, "wap"),
	TERMINAL(500, "terminal");

	private Product(int value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}

	private final int value;
	private final String name;
}
