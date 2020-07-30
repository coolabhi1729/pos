package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.model.SalesReportForm;
import com.increff.employee.service.ApiException;

@Repository
public class SalesReportDao extends AbstractDao{
	
	private static String get_report = "select b.category,sum(o.quantity),sum(o.quantity*o.sellingPrice) "
			+ "from OrderItemsPojo o,brand b, product p "
			+ "where orderId in (select ord.id from OrderPojo ord where date between :startDate and :endDate)"
			+ " and o.productId=p.id and b.id=p.brand_category group by ";

	@Transactional(readOnly = true)
	public List<Object[]> getSalesReport(SalesReportForm form) throws ApiException {
		
		if (!form.getBrand().isEmpty() && !form.getCategory().isEmpty()) {
			// throw new ApiException(" " + form.getBrand() + form.getCategory() + "  " + form.getStartDate() + " "+form.getEndDate());
			String get_report1 = get_report + " b.brand,b.category having b.brand=:brand and b.category=:category";
			Query query = em.createNativeQuery(get_report1);
			query.setParameter("startDate", form.getStartDate());
			query.setParameter("endDate", form.getEndDate());
			query.setParameter("brand", form.getBrand());
			query.setParameter("category", form.getCategory());
			return query.getResultList();
		}
		if (!form.getCategory().isEmpty()) {
			String get_report1 = get_report  + " b.category having b.category=:category";
			Query query = em.createNativeQuery(get_report1);
			query.setParameter("startDate", form.getStartDate());
			query.setParameter("endDate", form.getEndDate());
			query.setParameter("category", form.getCategory());
			return query.getResultList();
		}
		if (!form.getBrand().isEmpty()) {
			String get_report1 = get_report +" b.brand,b.category having b.brand=:brand";
			Query query = em.createNativeQuery(get_report1);
			query.setParameter("startDate", form.getStartDate());
			query.setParameter("endDate", form.getEndDate());
			query.setParameter("brand", form.getBrand());
			return query.getResultList();
		}
		if (form.getBrand().isEmpty() && form.getCategory().isEmpty()) {
			String get_report1 = get_report +" b.category";
			Query query = em.createNativeQuery(get_report1);
			query.setParameter("startDate", form.getStartDate());
			query.setParameter("endDate", form.getEndDate());
			return query.getResultList();
		}
		Query query = em.createNativeQuery(get_report);
		return query.getResultList();
	}

}