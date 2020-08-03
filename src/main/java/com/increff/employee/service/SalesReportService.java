package com.increff.employee.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.dao.SalesReportDao;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;

@Service
public class SalesReportService {

	@Autowired
	private SalesReportDao dao;
	
	@Transactional(readOnly = true)
	public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException{
		if (form.getEndDate() == null) {
			form.setEndDate(new Date());
		}
		if (form.getEndDate().before(form.getStartDate())) {
			throw new ApiException("End Date must be after Start Date!");
		}
		
		form.setEndDate(addToDate(form.getEndDate(), 1));
		List<SalesReportData> reportDataList = new ArrayList<SalesReportData>();
		List<Object[]> reportList = dao.getSalesReport(form);
		if (reportList == null) {
			throw new ApiException("No order between these point of dates by applying these filters.");
		}

		for (Object[] reportItem : reportList) {
			reportDataList.add(convert(reportItem));
		}

		return reportDataList;
	}
	

	private SalesReportData convert(Object[] reportItem) throws ApiException {
		SalesReportData data = new SalesReportData();
		Number revenue = (Number) reportItem[2];
		Number quantity = (Number) reportItem[1];
		data.setRevenue(revenue.doubleValue());
		data.setQuantity(quantity.intValue());
		data.setCategory(reportItem[0].toString());

		return data;
	}

	private static Date addToDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
}
