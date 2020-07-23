package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "inventory", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
public class InventoryPojo {
	
	@Id
	private int id;
	
	private int quantity;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
