package com.quadrosepaineisapi.repository.release;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.quadrosepaineisapi.model.release.FinancialRelease;
import com.quadrosepaineisapi.model.release.FinancialRelease_;
import com.quadrosepaineisapi.repository.filter.FinancialReleaseFilter;

public class FinancialReleaseRepositoryImpl implements FinancialReleaseRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	public List<FinancialRelease> filter(FinancialReleaseFilter financialReleaseFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<FinancialRelease> criteria = builder.createQuery(FinancialRelease.class);
		Root<FinancialRelease> root = criteria.from(FinancialRelease.class);
		criteria.where(createRestrictions(builder, root, financialReleaseFilter));
		return manager.createQuery(criteria).getResultList();
	}

	private Predicate[] createRestrictions(CriteriaBuilder builder, Root<FinancialRelease> root, 
			FinancialReleaseFilter filter) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(filter.getDescription())) {
			predicates.add(builder.like(builder.lower(root.get(FinancialRelease_.description)), 
					"%" + filter.getDescription() + "%"));
		}
		
		if (!StringUtils.isEmpty(filter.getObs())) {
			predicates.add(builder.like(builder.lower(root.get(FinancialRelease_.obs)), 
					"%" + filter.getObs() + "%"));
		}
		
		if (filter.getDueDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(FinancialRelease_.dueDate), 
					filter.getDueDateFrom()));
		}
		
		if (filter.getDueDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(FinancialRelease_.dueDate), 
					filter.getDueDateTo()));
		}
		
		if (filter.getPayDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(FinancialRelease_.payDate), 
					filter.getPayDateFrom()));
		}
		
		if (filter.getPayDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(FinancialRelease_.payDate), 
					filter.getPayDateTo()));
		}
		
		if (filter.getRegisterDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(FinancialRelease_.registerDate), 
					filter.getRegisterDateFrom()));
		}
		
		if (filter.getRegisterDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(FinancialRelease_.registerDate), 
					filter.getRegisterDateTo()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
}
