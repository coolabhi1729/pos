package com.increff.employee.dao;
//data access object layer...this layer interact with the databases...So service layer basically call this layer
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandMasterPojo;

@Repository
public class BrandMasterDao extends AbstractDao {

	private static String delete_id = "delete from BrandMasterPojo p where id=:id";
	private static String select_id = "select p from BrandMasterPojo p where id=:id";
	private static String select_all = "select p from BrandMasterPojo p";
	private static String get_name_category = "select p from BrandMasterPojo p where brand=:name and category=:category";// see
																															// it
																															// on
																															// running

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandMasterPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public BrandMasterPojo select(int id) {
		TypedQuery<BrandMasterPojo> query = getQuery(select_id, BrandMasterPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandMasterPojo> selectAll() {
		TypedQuery<BrandMasterPojo> query = getQuery(select_all, BrandMasterPojo.class);
		return query.getResultList();
	}

	//select is used for selecting anything thus we are using method overloading
	public BrandMasterPojo select(String name, String category) {
		TypedQuery<BrandMasterPojo> query = getQuery(get_name_category, BrandMasterPojo.class);
		query.setParameter("name", name);
		query.setParameter("category", category);
		return getSingle(query);
	}

	//To perform update operations later---put not working correctly<logical bug>
	public void update(BrandMasterPojo p) {
		
	}
}
