package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemsDao;
import com.increff.employee.model.OrderItemsData;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemsPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class OrderItemsService {
	@Autowired
	private OrderItemsDao dao;

	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderItemsPojo p) {
		dao.add(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public List<OrderItemsPojo> getOrder(int orderId) {
		return dao.getOrder(orderId);
	}

	@Transactional
	public OrderItemsData getItem(String barcode) throws ApiException {

		ProductPojo product = productService.get(barcode);
		if (product == null) {
			throw new ApiException("No product has this barcode. Enter correct barcode!");
		}
		InventoryPojo inventory = inventoryService.get(product.getId());
		if (inventory == null) {
			throw new ApiException("No items exist in the inventory for this product.");
		}

		OrderItemsData orderItem = new OrderItemsData();
		orderItem.setBarcode(product.getBarcode());
		orderItem.setMrp(product.getMrp());
		orderItem.setProductName(product.getProduct_name());
		orderItem.setProductId(product.getId());
		orderItem.setProductQuantity(inventory.getQuantity());
		return orderItem;
	}
}
