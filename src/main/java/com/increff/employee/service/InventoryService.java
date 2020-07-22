package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class InventoryService {
	@Autowired
	private InventoryDao dao;

	@Autowired
	private ProductDao product_dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p) throws ApiException {
		// normalize(p);--->not needed for inventory table
		if (p.getQuantity() <= 0) {
			throw new ApiException("Quantity can't be lesser or equal zero!");
		}
		referentialCheck(p);

		dao.insert(p);
	}

	/* Important */
	// May be need to come back here on doing UI part of InventoryPojo part
	@Transactional
	public boolean referentialCheck(InventoryPojo p) throws ApiException {
		List<ProductPojo> product_list = new ArrayList<ProductPojo>();
		product_list = product_dao.selectAll();
		int product_id = p.getProduct_id();// local variable brand_category

		for (ProductPojo ex : product_list) {
			if (ex.getId() == product_id) {
				return true;
			}
		}
		throw new ApiException("Referential Integrity Broken...Please Enter Valid Product Id!");
	}

	@Transactional(rollbackOn = ApiException.class)
	public void delete(int product_id) throws ApiException {
		getCheck(product_id);
		dao.delete(product_id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int product_id) throws ApiException {
		return getCheck(product_id);
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	// We can update quantity as well as product_id or id of the Inventory as well..Not working lol.11.42pm--22July
	//if not worked then remove for the product_id
	@Transactional(rollbackOn = ApiException.class)
	public void update(int product_id, InventoryPojo p) throws ApiException {
		// if getCheck succeed then ex will have old InventoryPojo object present in
		// the database
		InventoryPojo ex = getCheck(product_id);

		// Referential integrity check
		referentialCheck(p);

		// Product_Id should be unique check...This logic is working try it
		if (ex.getProduct_id() != p.getProduct_id()) {
			if (dao.select(p.getProduct_id()) != null) {
				throw new ApiException("This product Id already exists. Product must be different");
			}
			ex.setProduct_id(p.getProduct_id());
		}

		// checking quantity not updated to lesser than zero
		if (p.getQuantity() <= 0) {
			throw new ApiException("Update quantity can't be lesser or equal to zero!");
		}

		// ex.setProduct_id(p.getProduct_id());
		ex.setQuantity(p.getQuantity());

		dao.update(ex);
	}

	@Transactional
	public InventoryPojo getCheck(int product_id) throws ApiException {
		InventoryPojo p = dao.select(product_id);
		if (p == null) {
			throw new ApiException("Inventory item with given ID does not exit, id: " + product_id);
		}
		return p;
	}

}
