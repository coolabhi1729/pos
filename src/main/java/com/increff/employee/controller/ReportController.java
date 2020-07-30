package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BrandMasterData;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandMasterService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.service.SalesReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportController {
	@Autowired
	private BrandMasterService brandService;

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private SalesReportService salesReportService;
	
	@ApiOperation(value = "Gets list of all brand")
	@RequestMapping(path = "/api/brand_report", method = RequestMethod.GET)
	public List<BrandMasterData> getAllBrand() {
		List<BrandMasterPojo> list = brandService.getAll();
		List<BrandMasterData> list2 = new ArrayList<BrandMasterData>();
		for (BrandMasterPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	private BrandMasterData convert(BrandMasterPojo p) {
		BrandMasterData d = new BrandMasterData();
		d.setId(p.getId());
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());

		return d;
	}

	@ApiOperation(value = "Get list of all Inventory Items with Brand and Category")
	@RequestMapping(path = "/api/inventory_report", method = RequestMethod.GET)
	public List<InventoryReportData> getAllInventory() throws ApiException {
		List<InventoryPojo> list = inventoryService.getAll();
		List<InventoryReportData> inventoryReportList = new ArrayList<InventoryReportData>();
		for (InventoryPojo p : list) {
			inventoryReportList.add(convert(p));
		}
		return inventoryReportList;
	}

	private InventoryReportData convert(InventoryPojo p) throws ApiException {
		// TODO Auto-generated method stub
		ProductPojo product = productService.get(p.getId());
		BrandMasterPojo brand = brandService.get(product.getBrand_category());
		InventoryReportData data = new InventoryReportData();
		
		data.setId(p.getId());
		data.setBarcode(product.getBarcode());
		data.setBrand(brand.getBrand());
		data.setCategory(brand.getCategory());
		data.setQuantity(p.getQuantity());
		return data;
	}

	
	@ApiOperation(value = "Get Sales Report in between dates")
	@RequestMapping(path = "/api/sales_report", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form) throws ApiException {
		return salesReportService.getSalesReport(form);
	}
}
