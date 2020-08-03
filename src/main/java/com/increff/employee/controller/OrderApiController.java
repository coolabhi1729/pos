package com.increff.employee.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
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

	private static final String PATH_XSL = "./templateInvoice.xsl";

	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private OrderItemsService orderItemsService;

	@Autowired
	private OrderService orderService;

	@ApiOperation(value = "Get all orders")
	@RequestMapping(path = "/api/order/viewOrders", method = RequestMethod.GET)
	public List<OrderPojo> getAll() {
		return orderService.selectAll();
	}

	@ApiOperation(value = "Get a product by barcode")
	@RequestMapping(path = "/api/order/{barcode}", method = RequestMethod.GET)
	public OrderItemsData getProduct(@PathVariable String barcode) throws ApiException {
		return orderItemsService.getItem(barcode);
	}

	@ApiOperation(value = "Creates an order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void addOrder(@RequestBody List<OrderItemsForm> forms)
			throws ApiException, FileNotFoundException, JAXBException {
		add(forms);
	}

	@Transactional(rollbackOn = ApiException.class)
	private void add(List<OrderItemsForm> forms) throws ApiException, JAXBException, FileNotFoundException {
		OrderPojo order = new OrderPojo();
		order = orderService.add(order);
		for (OrderItemsForm form : forms) {
			OrderItemsPojo orderItem = convert(order, form);
			orderItemsService.add(orderItem);
		}
	}

	@Transactional
	private OrderItemsPojo convert(OrderPojo order, OrderItemsForm form) throws ApiException {
		// TODO Auto-generated method stub
		OrderItemsPojo orderItem = new OrderItemsPojo();
		ProductPojo product = productService.get(form.getBarcode());
		InventoryPojo inventory = inventoryService.get(product.getId());
		orderItem.setOrderId(order.getId());
		orderItem.setProductId(product.getId());
		if (form.getQuantity() > inventory.getQuantity()) {
			throw new ApiException(
					"Inventory doesn't have sufficient quantity.Problem with this product: " + form.getBarcode());
		}
		int quantity = form.getQuantity();
		orderItem.setQuantity(quantity);
		inventory.setQuantity(-quantity);
		inventoryService.updatePlus(inventory.getId(), inventory);
		
		//form.setSp(setFractionalDigits(form.getSp()));
		orderItem.setSellingPrice(form.getSp());
		return orderItem;
	}

	@ApiOperation(value = "Generate Invoice")
	@RequestMapping(path = "/api/order/viewOrders/invoice/{id}", method = RequestMethod.GET)
	public void get(@PathVariable String id, HttpServletResponse response)
			throws ApiException, ParserConfigurationException, TransformerException {
		int idInt = Integer.parseInt(id);
		orderService.createInvoice(idInt);
		try {

			FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(PATH_XSL));
			// Make sure the XSL transformation's result is piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());
			// Setup input
			Source src = new StreamSource(new File("./src/main/resources/com/increff/employee/invoice.xml"));

			transformer.transform(src, res);
			response.setContentType("application/pdf");
			response.setContentLength(out.size());

			response.getOutputStream().write(out.toByteArray());
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
