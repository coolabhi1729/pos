package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandMasterPojo;

public class BrandMasterServiceTest extends AbstractUnitTest {
	@Autowired
	private BrandMasterService brandService;

	private BrandMasterPojo brandPojo;

	@Before
	public void init() {
		brandPojo = new BrandMasterPojo();
		brandPojo.setBrand("  AmuL");
		brandPojo.setCategory("  MiLK ");
	}

	// Test for Normalize
	@Test
	public void testNormalize() {
		BrandMasterService.normalize(brandPojo);
		assertEquals("amul", brandPojo.getBrand());
		assertEquals("milk", brandPojo.getCategory());
	}
	
	//Test for Add
	@Test
	public void testAdd() throws ApiException {
		brandService.add(brandPojo);
	}
	
	//Test for getCheck()
//	@Test
//	public void testGetCheck() {
//		
//	}
}
