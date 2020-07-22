package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//interaction with the front end and what service should be performed is written in this section

@Api
@RestController
public class ProductApiController {
	@Autowired
	private ProductService service;
	
	@ApiOperation(value = "Adds a Brand")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.add(p);
	}
	
	@ApiOperation(value = "Deletes a brand")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	// /api/1
	public void delete(@PathVariable int id) throws ApiException {
		service.delete(id);
	}
	
	@ApiOperation(value = "Get a single product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductPojo p = service.get(id);
		return convert(p);
	}
	
	@ApiOperation(value = "Get list of all product")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductPojo> list = service.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		ProductPojo p = convert(f);
		service.update(id, p);
	}

	protected static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setId(p.getId());
		d.setBarcode(p.getBarcode());
		d.setBrand_category(p.getBrand_category());
		d.setProduct_name(p.getProduct_name());
		d.setMrp(p.getMrp());
		return d;
	}
	//converting into product-pojo
	protected static ProductPojo convert(ProductForm f) {
		ProductPojo p = new ProductPojo();
		p.setBarcode(f.getBarcode());
		p.setBrand_category(f.getBrand_category());
		p.setProduct_name(f.getProduct_name());
		p.setMrp(f.getMrp());
		return p;
	}
}
