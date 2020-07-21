package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandMasterDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService {
	@Autowired
	private ProductDao dao;

	@Autowired
	BrandMasterDao brand_dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		
		if (StringUtil.isEmpty(p.getBarcode())) {// here can be change to particular fixed length of string
			throw new ApiException("Barcode cannot be empty!");
		}
		if(dao.select(p.getBarcode())!=null) {
			throw new ApiException("This Barcode already exists:"+p.getBarcode());
		}
		if (StringUtil.isEmpty(p.getProduct_name())) {
			throw new ApiException("Product Name cannot be empty!");
		}
		if (p.getMrp() <= 0) {
			throw new ApiException("MRP cannot be zero or less than zero!");
		}
		referentialCheck(p);
		
		dao.insert(p);
	}
	
	/* Important */
	// May be need to come back here on doing UI part of ProductPojo part
	@Transactional
	public boolean referentialCheck(ProductPojo p) throws ApiException {
		List<BrandMasterPojo> brand_list = brand_dao.selectAll();
		int brand_category = p.getBrand_category();// local variable brand_category

		for (BrandMasterPojo ex : brand_list) {
			if (ex.getId() == brand_category) {
				return true;
			}
		}
		throw new ApiException("Referential Integrity Broken...Please Enter Valid Brand and category combination!");
	}

	@Transactional
	public void delete(int id) throws ApiException {
		getCheck(id);
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<ProductPojo> getAll() {
		return dao.selectAll();
	}

	/*
	 * If this much run in update the n try to run code with changing UI for brand+category
	 * combination
	 */
	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		normalize(p);
		//if getCheck succeed then ex will have old BrandMasterPojo object present in the database
		ProductPojo ex = getCheck(id);
		
		// Barcode can't be empty
		if (StringUtil.isEmpty(p.getBarcode())) {// can add string length related features here!
			throw new ApiException("Barcode cannot remain empty!");
		}
		
		/*
		 * // barcode should be unique...it will always throw error due to its own entry...Thinking!!!
		 * if (dao.select(p.getBarcode()) != null) { throw
		 * new ApiException("This barcode exists for other product!"); }
		 */
		
		// product name cant be empty
		if (StringUtil.isEmpty(p.getProduct_name())) {
			throw new ApiException("Product name cannot remain empty!");
		}
		//check on MRP
		if (p.getMrp() <= 0) {
			throw new ApiException("MRP must be greater than zero!");
		}
		//Referential integrity check
		referentialCheck(p);
		
		ex.setBarcode(p.getBarcode());
		ex.setBrand_category(p.getBrand_category());
		ex.setProduct_name(p.getProduct_name());
		ex.setMrp(p.getMrp());
		
		dao.update(ex);
	}

	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}

	protected static void normalize(ProductPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()).trim());
		p.setProduct_name(StringUtil.toLowerCase(p.getProduct_name()).trim());
	}
}
