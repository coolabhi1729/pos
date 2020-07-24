package com.increff.employee.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemsPojo;

@Repository
public class OrderItemsDao extends AbstractDao{
private static String select_by_order_id = "select p from OrderItemsPojo p where orderId=:orderId";
	

	public void add(OrderItemsPojo p) {
		em.persist(p);
	}

	public List<OrderItemsPojo> getOrder(int orderId) {
		TypedQuery<OrderItemsPojo> query = getQuery(select_by_order_id, OrderItemsPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
}
