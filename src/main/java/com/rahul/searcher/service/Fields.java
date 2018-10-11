package com.rahul.searcher.service;

public enum Fields {
	REGION("Region"), COUNTRY("Country"), ITEM_TYPE("Item Type"), SALES_CHANNEL("Sales Channel"), ORDER_PRIORITY(
			"Order Priority"), ORDER_DATE("Order Date"), ORDER_ID("Order ID"), SHIP_DATE("Ship Date"), UNITS_SOLD(
					"Units Sold"), UNIT_PRICE("Unit Price"), UNIT_COST("Unit Cost"), TOTAL_REVENUE(
							"Total Revenue"), TOTAL_COST("Total Cost"), TOTAL_PROFIT("Total Profit");
	public String value;

	Fields(String value) {
		this.value = value;
	}

}
