package com.increff.employee.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.model.OrderItemsForm;
import com.increff.employee.pojo.OrderItemsPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class OrderService {
	@Autowired
	private OrderDao dao;
	@Autowired
	private OrderItemsService orderItemsService;
	@Autowired
	private ProductService productService;

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

	@Transactional
	public OrderPojo select(int id) {
		return dao.select(id);
	}

	@Transactional
	public void createInvoice(int id) throws ApiException, ParserConfigurationException, TransformerException {
		List<OrderItemsPojo> orderItems = orderItemsService.getOrder(id);
		List<OrderItemsForm> orderItemsForm = new ArrayList<OrderItemsForm>();
		for (OrderItemsPojo pojo : orderItems) {
			OrderItemsForm form = new OrderItemsForm();
			form.setQuantity(pojo.getQuantity());
			ProductPojo productPojo = productService.get(pojo.getProductId());
			form.setBarcode(productPojo.getBarcode());
			form.setSp(pojo.getSellingPrice());
			orderItemsForm.add(form);
		}
		make(orderItemsForm, id);
	}

	public final String xmlFilePath = "./src/main/resources/com/increff/employee/invoice.xml";

	public void make(List<OrderItemsForm> formList, int id)
			throws ParserConfigurationException, ApiException, TransformerException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		// root element
		Element root = document.createElement("InvoiceData");
		document.appendChild(root);
		Double sum = 0.0;
		int sno = 1;
		for (OrderItemsForm form : formList) {
			ProductPojo p = productService.get(form.getBarcode());
			Element product = document.createElement("invoice");
			root.appendChild(product);
			Element count = document.createElement("sno");
			count.appendChild(document.createTextNode(Integer.toString(sno)));
			product.appendChild(count);
			Element product_name = document.createElement("name");
			product_name.appendChild(document.createTextNode(p.getProduct_name()));
			product.appendChild(product_name);
			Element barcode = document.createElement("barcode");
			barcode.appendChild(document.createTextNode(p.getBarcode()));
			product.appendChild(barcode);
			Element qty = document.createElement("qty");
			qty.appendChild(document.createTextNode(Integer.toString(form.getQuantity())));
			product.appendChild(qty);
			Element mrp = document.createElement("mrp");
			mrp.appendChild(document.createTextNode(Double.toString(p.getMrp())));
			product.appendChild(mrp);
			Element sp = document.createElement("sp");
			sp.appendChild(document.createTextNode(Double.toString(form.getSp())));
			product.appendChild(sp);
			Element totalPrice = document.createElement("totalPrice");
			totalPrice.appendChild(document.createTextNode(Double.toString(form.getQuantity() * form.getSp())));
			product.appendChild(totalPrice);
			sum += (form.getSp() * form.getQuantity());
			sum=Math.floor(sum*100)/100;
			sno += 1;
		}
		Element totalPrice = document.createElement("totalAmount");
		totalPrice.appendChild(document.createTextNode(Double.toString(sum)));
		root.appendChild(totalPrice);

		Element orderId = document.createElement("ID");
		orderId.appendChild(document.createTextNode(Integer.toString(id)));
		root.appendChild(orderId);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(new File(xmlFilePath));

		// If you use
		// StreamResult result = new StreamResult(System.out);
		// the output will be pushed to the standard output ...
		// You can use that for debugging

		transformer.transform(domSource, streamResult);
	}
}
