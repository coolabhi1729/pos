package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandMasterDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.OrderItemsDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.BrandMasterPojo;
import com.increff.employee.pojo.OrderItemsPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class SalesReportService {

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private BrandMasterDao brandDao;

	@Autowired
	private OrderItemsDao orderItemsDao;

	@Autowired
	private ProductDao productDao;

	public SalesReportData getSalesReport(SalesReportForm form) throws ApiException {
		normalize(form);
		
		if (StringUtil.isEmpty(form.getCategory())) {
			throw new ApiException("Category cannot be empty!");
		}
		if (StringUtil.isEmpty(form.getBrand())) {
			throw new ApiException("Brand cannot be empty!");
		}

		List<OrderPojo> orderPojoList = orderDao.selectByDate(form.getStartDate(), form.getEndDate());
		if(orderPojoList.isEmpty()) {
			throw new ApiException("No order between these point of dates");
		}
		int quantity = 0;
		double revenue = 0;
		String brand = form.getBrand();
		String category = form.getCategory();
		BrandMasterPojo brandMasterPojo = brandDao.select(brand, category);
		if(brandMasterPojo==null) {
			throw new ApiException("This brand and category combination is not present!");
		}
		int brandMasterId = brandMasterPojo.getId();

		for (OrderPojo orderPojo : orderPojoList) {
			List<OrderItemsPojo> orderItemsPojoList = orderItemsDao.getOrder(orderPojo.getId());
			for (OrderItemsPojo orderItemsPojo : orderItemsPojoList) {
				ProductPojo productPojo = productDao.select(orderItemsPojo.getProductId());
				if (productPojo.getBrand_category() == brandMasterId) {
					quantity += orderItemsPojo.getQuantity();
					revenue += (orderItemsPojo.getSellingPrice()) * (orderItemsPojo.getQuantity());
				}
			}
		}
		SalesReportData data=new SalesReportData();
		data.setCategory(category);
		data.setBrand(brand);
		data.setQuantity(quantity);
		data.setRevenue(revenue);
		data.setStartDate(form.getStartDate());
		data.setEndDate(form.getEndDate());
		return data;
	}
	
	protected static void normalize(SalesReportForm form) {
		form.setBrand(StringUtil.toLowerCase(form.getBrand()).trim());
		form.setCategory(StringUtil.toLowerCase(form.getCategory()).trim());
	}
}
