package com.increff.employee.model;

public class OrderItemsForm {

	private String barcode;
	private int quantity;
	private double mrp;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double d) {
		this.mrp = d;
	}
}