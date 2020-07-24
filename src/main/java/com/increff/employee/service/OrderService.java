package com.increff.employee.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderPojo;

@Service
public class OrderService {
	@Autowired
	private OrderDao dao;
	
	@Transactional(rollbackOn = ApiException.class)
	public OrderPojo add(OrderPojo orderPojo) {
		OrderPojo order = dao.add(orderPojo);
		return order;
	}
	
	@Transactional(rollbackOn = ApiException.class)
	public List<OrderPojo> selectByDate(Date startDate, Date endDate) {
		return dao.selectByDate(startDate, endDate);
	}
	
	@Transactional
	public List<OrderPojo> selectAll() {
		return dao.selectAll();
	}
}
