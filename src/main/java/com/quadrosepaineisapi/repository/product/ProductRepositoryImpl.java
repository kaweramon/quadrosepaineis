package com.quadrosepaineisapi.repository.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.quadrosepaineisapi.model.Category;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.filter.ProductFilter;
import com.quadrosepaineisapi.repository.projection.ProductResume;

public class ProductRepositoryImpl implements ProductRepositoryQuery {

	@Autowired
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

	@Override
	public Page<ProductResume> resume(ProductFilter productFilter, Pageable pageable) {
	
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ProductResume> criteria = builder.createQuery(ProductResume.class);
		Root<Product> root = criteria.from(Product.class);
		
		criteria.select(builder.construct(ProductResume.class, 
				root.get("id"), root.get("name"), root.get("price"),
				root.get("description"), root.get("sequence")));
		
		Predicate[] predicates = createRestrictions(productFilter, builder, root);
		
		criteria.where(predicates);
		criteria.orderBy(builder.asc(root.get("sequence")));
		TypedQuery<ProductResume> query = manager.createQuery(criteria);
		
		addPageableRestrictions(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, getTotal(productFilter));
	}	
	
	private Predicate[] createRestrictions(ProductFilter productFilter, CriteriaBuilder builder, Root<Product> root) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(productFilter.getDescription())) {
			predicates.add(builder.like(builder.lower(root.get("description")),
					"%" + productFilter.getDescription().toLowerCase() + "%"));
		}
		
		if (!StringUtils.isEmpty(productFilter.getName())) {
			predicates.add(builder.like(builder.lower(root.get("name")), 
					"%" + productFilter.getName().toLowerCase() + "%"));
		}
		
		if (productFilter.getPrice() != null)
			predicates.add(builder.equal(root.get("price"), productFilter.getPrice()));
			
		
		if (productFilter.getRegisterDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("registerDate"), 
					productFilter.getRegisterDateFrom()));
		}
		
		if (productFilter.getRegisterDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("registerDate"), 
					productFilter.getRegisterDateTo()));
		}
		
		if (productFilter.getCategories() != null) {
			Join<Product, Category> joinProductCategories = root.join("categories");
			predicates.add(joinProductCategories.get("id").in(productFilter.getCategories()));
		}
		
		if (productFilter.getIsActive() != null) {
			predicates.add(builder.equal(root.get("isActive"), productFilter.getIsActive()));
		}
		
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void addPageableRestrictions(TypedQuery<?> query, Pageable pageable) {
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
