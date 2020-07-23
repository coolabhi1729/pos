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
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController {
	@Autowired
	private InventoryService service;
	
	@Autowired
	private ProductService product_service;

	@ApiOperation(value = "Add a new product to Inventory ")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		InventoryPojo p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes an Inventory item")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) throws ApiException {
		service.delete(id);
	}

	@ApiOperation(value = "Get a single product by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Get list of all Inventory Items")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> data_list = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			data_list.add(convert(p));
		}
		return data_list;
	}

	@ApiOperation(value = "Updates an Inventory item")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		InventoryPojo p = convert(f);
		if(p.getId()!=id) {
			throw new ApiException("product barcode and id mismatch!");
		}
		service.updatePlus (id, p);
	}

	private InventoryData convert(InventoryPojo p) throws ApiException{
		InventoryData d = new InventoryData();
		ProductPojo productPojo = product_service.get(p.getId());
		
		d.setId(p.getId());
		d.setBarcode(productPojo.getBarcode());
		d.setQuantity(p.getQuantity());
		return d;
	}

	// converting into inventory-pojo
	private InventoryPojo convert(InventoryForm form) throws ApiException {
		if (StringUtil.isEmpty(form.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		InventoryPojo inventoryPojo = new InventoryPojo();
		ProductPojo productPojo = product_service.get(form.getBarcode());
		
		inventoryPojo.setId(productPojo.getId());
		inventoryPojo.setQuantity(form.getQuantity());
		return inventoryPojo;
	}
}
