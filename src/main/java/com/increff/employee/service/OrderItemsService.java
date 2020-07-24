package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemsDao;
import com.increff.employee.pojo.OrderItemsPojo;

@Service
public class OrderItemsService {
	@Autowired
	private OrderItemsDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderItemsPojo p) {
		dao.add(p);
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<OrderItemsPojo> getOrder(int orderId){
		return dao.getOrder(orderId);
	}
}
