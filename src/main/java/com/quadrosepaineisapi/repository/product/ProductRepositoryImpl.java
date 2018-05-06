package com.quadrosepaineisapi.repository.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.model.Product_;
import com.quadrosepaineisapi.repository.filter.ProductFilter;

public class ProductRepositoryImpl implements ProductRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Product> filter(ProductFilter productFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
		Root<Product> root = criteria.from(Product.class);
		Predicate[] predicates = createRestrictions(productFilter, builder, root);
		
		criteria.where(predicates);
		TypedQuery<Product> query = manager.createQuery(criteria);
		
		addPageableRestrictions(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, getTotal(productFilter));
	}

	private Predicate[] createRestrictions(ProductFilter productFilter, CriteriaBuilder builder, Root<Product> root) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(productFilter.getDescription())) {
			predicates.add(builder.like(builder.lower(root.get(Product_.description)),
					"%" + productFilter.getDescription().toLowerCase() + "%"));
		}
		
		if (!StringUtils.isEmpty(productFilter.getName())) {
			predicates.add(builder.like(builder.lower(root.get(Product_.name)), 
					"%" + productFilter.getName().toLowerCase() + "%"));
		}
		
		if (productFilter.getRegisterDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Product_.registerDate), 
					productFilter.getRegisterDateFrom()));
		}
		
		if (productFilter.getRegisterDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Product_.registerDate), 
					productFilter.getRegisterDateTo()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void addPageableRestrictions(TypedQuery<Product> query, Pageable pageable) {
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
	}
	

	private Long getTotal(ProductFilter productFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Product> root = criteria.from(Product.class);
		
		Predicate[] predicates = createRestrictions(productFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}
	
}
