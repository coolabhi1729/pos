package com.increff.employee.controller;
//interaction with the front end and what service should be performed is written in this section
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BrandMasterData;
import com.increff.employee.model.BrandMasterForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandMasterApiController {

	@Autowired
	private BrandMasterService service;

	@ApiOperation(value = "Adds a Brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandMasterForm form) throws ApiException {
		BrandMasterPojo p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes a brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
	// /api/1
	public void delete(@PathVariable int id) throws ApiException {
		service.delete(id);
	}

	@ApiOperation(value = "Gets a brand by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandMasterData get(@PathVariable int id) throws ApiException {
		BrandMasterPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandMasterData> getAll() {
		List<BrandMasterPojo> list = service.getAll();
		List<BrandMasterData> list2 = new ArrayList<BrandMasterData>();
		for (BrandMasterPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandMasterForm f) throws ApiException {
		BrandMasterPojo p = convert(f);
		service.update(id, p);
	}

	//converting brand-master-data
	protected static BrandMasterData convert(BrandMasterPojo p) {
		BrandMasterData d = new BrandMasterData();
		d.setId(p.getId());
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());

		return d;
	}
	//converting into brand-master-pojo
	protected static BrandMasterPojo convert(BrandMasterForm f) {
		BrandMasterPojo p = new BrandMasterPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}

}
