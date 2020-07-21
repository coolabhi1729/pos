package com.increff.employee.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandMasterPojo;

@Repository
public class BrandMatserInMemDao {

	private HashMap<Integer, BrandMasterPojo> rows;
	private int lastId;

	@PostConstruct
	public void init() {
		rows = new HashMap<Integer, BrandMasterPojo>();
	}

	public void insert(BrandMasterPojo p) {
		lastId++;
		p.setId(lastId);
		rows.put(lastId, p);
	}

	public void delete(int id) {
		rows.remove(id);
	}

	public BrandMasterPojo select(int id) {
		return rows.get(id);
	}

	public List<BrandMasterPojo> selectAll() {
		ArrayList<BrandMasterPojo> list = new ArrayList<BrandMasterPojo>();
		list.addAll(rows.values());
		return list;
	}

	public void update(int id, BrandMasterPojo p) {
		rows.put(id, p);
	}

}
