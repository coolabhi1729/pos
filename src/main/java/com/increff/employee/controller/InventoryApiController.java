package com.increff.employee.controller;

//@PathVariable is used to tell Spring that part of the URI path is a value you want passed to your method.
/*
 * https://stackoverflow.com/questions/19803117/spring-mvc-missing-uri-template-variable
*/

/*
 * got this error for a stupid mistake, the variable name in 
 * the @PathVariable wasn't matching the one in the @RequestMapping
*/

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {
	@Autowired
	private InventoryService service;

	@ApiOperation(value = "Add a new product to Inventory ")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		InventoryPojo p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes an Inventory item")
	@RequestMapping(path = "/api/inventory/{product_id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int product_id) throws ApiException {
		service.delete(product_id);
	}

	@ApiOperation(value = "Get a single product by ID")
	@RequestMapping(path = "/api/inventory/{product_id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int product_id) throws ApiException {
		InventoryPojo p = service.get(product_id);
		return convert(p);
	}

	@ApiOperation(value = "Get list of all Inventory Items")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an Inventory item")
	@RequestMapping(path = "/api/inventory/{product_id}", method = RequestMethod.PUT)
	public void update(@PathVariable int product_id, @RequestBody InventoryForm f) throws ApiException {
		InventoryPojo p = convert(f);
		service.update(product_id, p);
	}

	protected static InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setProduct_id(p.getProduct_id());
		d.setQuantity(p.getQuantity());
		return d;
	}

	// converting into inventory-pojo
	protected static InventoryPojo convert(InventoryForm f) {
		InventoryPojo p = new InventoryPojo();

		p.setProduct_id(f.getProduct_id());
		p.setQuantity(f.getQuantity());
		return p;
	}
}
