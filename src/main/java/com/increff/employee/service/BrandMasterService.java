package com.increff.employee.service;

import java.util.ArrayList;
//This layer implements the application logic....
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
public class BrandMasterService {

	@Autowired
	private BrandMasterDao dao;

	@Autowired
	private ProductDao product_dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandMasterPojo p) throws ApiException {
		normalize(p);
		if (StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if (StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("category cannot be empty");
		}
		if (dao.select(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("brand and category should be of unique combination");
		}
		dao.insert(p);
	}

	/* Important */
	// After implementing backend part of product needs to change delete method
	// since it breaks referential integrity.Time:8.00PM
	// Delete rule is set here for referential integrity:Time 1.00AM
	@Transactional(rollbackOn = ApiException.class)
	public void delete(int id) throws ApiException {

		if (referentialCheck(id)) {
			throw new ApiException("Referential Integrity failed...");
		}

		getCheck(id);
		dao.delete(id);
	}

	// referential check is not working giving server error:500

	@Transactional
	public boolean referentialCheck(int id) throws ApiException {
		List<ProductPojo> prod_list = new ArrayList<ProductPojo>();
		prod_list = product_dao.selectAll();
		for (ProductPojo ex : prod_list) {
			if (ex.getBrand_category() == id) {
				return true;
			}
		}
		return false;
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandMasterPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandMasterPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn = ApiException.class)
	public void update(int id, BrandMasterPojo p) throws ApiException {
		normalize(p);
		// if getCheck succeed then ex will have old BrandMasterPojo object present in
		// the database
		BrandMasterPojo ex = getCheck(id);
		if (StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Please Enter brand name!");
		}
		if (StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Please enter brand category!");
		}
		if (dao.select(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("Requires unique combination of Brand and category.");
		}

		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		dao.update(ex);
	}

	@Transactional
	public BrandMasterPojo getCheck(int id) throws ApiException {
		BrandMasterPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exit, id: " + id);
		}
		return p;
	}

	protected static void normalize(BrandMasterPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()).trim());
		p.setCategory(StringUtil.toLowerCase(p.getCategory()).trim());
	}
}
