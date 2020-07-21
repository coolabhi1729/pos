package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandMasterDao;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.util.StringUtil;

@Service
public class BrandMasterService {

	@Autowired
	private BrandMasterDao dao;

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
			throw new ApiException("brand and category shoul be of unique combination");
		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
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
