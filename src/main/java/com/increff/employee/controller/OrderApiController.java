package com.increff.employee.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.increff.employee.model.OrderItemsData;
import com.increff.employee.model.OrderItemsForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemsPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemsService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController {

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderItemsService orderItemsService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private InventoryService inventoryService;

	@ApiOperation(value = "Get a product by barcode")
	@RequestMapping(path = "/api/order/{barcode}", method = RequestMethod.GET)
	public OrderItemsData getProduct(@PathVariable String barcode) throws ApiException {
		return getItem(barcode);
	}

	@ApiOperation(value = "Creates an order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void addOrder(@RequestBody List<OrderItemsForm> forms) throws ApiException {
		add(forms);
	}

	@Transactional
	private OrderItemsData getItem(String barcode) throws ApiException {

		ProductPojo product = productService.get(barcode);
		InventoryPojo inventory = inventoryService.get(product.getId());
		OrderItemsData orderItem = new OrderItemsData();
		orderItem.setBarcode(product.getBarcode());
		orderItem.setMrp(product.getMrp());
		orderItem.setProductName(product.getProduct_name());
		orderItem.setProductId(product.getId());
		orderItem.setProductQuantity(inventory.getQuantity());
		return orderItem;
	}

	@Transactional(rollbackOn = ApiException.class)
	protected void add(List<OrderItemsForm> forms) throws ApiException {
		OrderPojo order = new OrderPojo();
		order = orderService.add(order);
		for (OrderItemsForm form : forms) {
			orderItemsService.add(convert(order, form));
		}
	}
	
	@Transactional
	private OrderItemsPojo convert(OrderPojo order, OrderItemsForm form) throws ApiException {
		// TODO Auto-generated method stub
		OrderItemsPojo orderItem = new OrderItemsPojo();
		ProductPojo product = productService.get(form.getBarcode());
		InventoryPojo inventory=inventoryService.get(product.getId());
		orderItem.setOrderId(order.getId());
		orderItem.setProductId(product.getId());
		if(form.getQuantity()>inventory.getQuantity()) {
			throw new ApiException("Inventory doesn't have sufficient quantity.Problem with this product"+form.getBarcode());
		}
		int quantity=form.getQuantity();
		orderItem.setQuantity(quantity);
		inventory.setQuantity(-quantity);
		inventoryService.updatePlus(inventory.getId(), inventory);
		orderItem.setSellingPrice(form.getSp());
		return orderItem;
	}
	
}
