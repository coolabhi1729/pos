package com.increff.employee.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
//	private static String select_id = "select p from OrderPojo p where id=:id";
	private static String select_all = "select p from OrderPojo p";
	private static String select_by_date = "select p from OrderPojo p where p between :startDate and :endDate";
//	private static String delete_id = "delete from OrderPojo p where id=:id";

	public OrderPojo add(OrderPojo p) {
		p.setDate(new Date());
		em.persist(p);
		return p;
	}

	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}

	public List<OrderPojo> selectByDate(Date startDate, Date endDate) {
		TypedQuery<OrderPojo> query = getQuery(select_by_date, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

}
