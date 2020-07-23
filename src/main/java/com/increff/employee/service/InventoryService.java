package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;

@Service
public class InventoryService {
	@Autowired
	private InventoryDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p) throws ApiException {
		// normalize(p);--->not needed for inventory table
		if (p.getQuantity() < 0) {
			throw new ApiException("Quantity can't be lesser than zero!");
		}
		//referentialCheck(p);
		if(dao.select(p.getId())!=null) {
			throw new ApiException("Inventory item with given id exists!.");
		}
		dao.insert(p);
	}

	

	@Transactional(rollbackOn = ApiException.class)
	public void delete(int id) throws ApiException {
		getCheck(id);
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void updatePlus(int id, InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo ex = getCheck(id);
		int newQuantity = inventoryPojo.getQuantity() + ex.getQuantity();
		if (newQuantity < 0) {
			throw new ApiException("Total Quatity cannot be negative.");
		}
		ex.setQuantity(newQuantity);
	}

	@Transactional
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Inventory item with given ID does not exit, id: " + id);
		}
		return p;
	}

}
