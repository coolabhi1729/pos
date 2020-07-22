package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {
	
	private static String delete_id = "delete from InventoryPojo p where product_id=:product_id";
	private static String select_id = "select p from InventoryPojo p where product_id=:product_id";
	private static String select_all = "select p from InventoryPojo p";
	//private static String select_productid = "select p from InventoryPojo p where product_id=:product_id";

	@PersistenceContext
	private EntityManager em;

	// Inserting into database
	@Transactional
	public void insert(InventoryPojo p) {
		em.persist(p);
	}

	public int delete(int product_id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("product_id", product_id);
		return query.executeUpdate();
	}

	public InventoryPojo select(int product_id) {
		TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
		query.setParameter("product_id", product_id);
		return getSingle(query);
	}

	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}
	
	public void update(InventoryPojo ex) {
		// TODO Auto-generated method stub
		
	}
}
