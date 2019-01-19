package com.quadrosepaineisapi.repository.release;

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

import com.quadrosepaineisapi.model.release.FinancialRelease;
import com.quadrosepaineisapi.repository.filter.FinancialReleaseFilter;
import com.quadrosepaineisapi.repository.projection.FinancialReleaseResume;

public class FinancialReleaseRepositoryImpl implements FinancialReleaseRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	public Page<FinancialRelease> filter(FinancialReleaseFilter financialReleaseFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<FinancialRelease> criteria = builder.createQuery(FinancialRelease.class);
		Root<FinancialRelease> root = criteria.from(FinancialRelease.class);
		criteria.where(createRestrictions(builder, root, financialReleaseFilter));
		
		TypedQuery<FinancialRelease> query = manager.createQuery(criteria);
		addPageRestrictions(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(financialReleaseFilter));
	}

	private Predicate[] createRestrictions(CriteriaBuilder builder, Root<FinancialRelease> root, 
			FinancialReleaseFilter filter) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(filter.getDescription())) {
			predicates.add(builder.like(builder.lower(root.get("description")), 
					"%" + filter.getDescription() + "%"));
		}
		
		if (!StringUtils.isEmpty(filter.getObs())) {
			predicates.add(builder.like(builder.lower(root.get("obs")), 
					"%" + filter.getObs() + "%"));
		}
		
		if (filter.getDueDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dueDate"), 
					filter.getDueDateFrom()));
		}
		
		if (filter.getDueDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dueDate"), 
					filter.getDueDateTo()));
		}
		
		if (filter.getPayDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("payDate"), 
					filter.getPayDateFrom()));
		}
		
		if (filter.getPayDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("payDate"), 
					filter.getPayDateTo()));
		}
		
		if (filter.getRegisterDateFrom() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("registerDate"), 
					filter.getRegisterDateFrom()));
		}
		
		if (filter.getRegisterDateTo() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("registerDate"), 
					filter.getRegisterDateTo()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	@Override
	public Page<FinancialReleaseResume> resume(FinancialReleaseFilter financialReleaseFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<FinancialReleaseResume> criteria = builder.createQuery(FinancialReleaseResume.class);
		Root<FinancialRelease> root = criteria.from(FinancialRelease.class);
		
		criteria.select(builder.construct(FinancialReleaseResume.class
				, root.get("id"), root.get("description")
				, root.get("obs")
				, root.get("value")));
		
		Predicate[] predicates = createRestrictions(builder, root, financialReleaseFilter);
		criteria.where(predicates);
		
		TypedQuery<FinancialReleaseResume> query = manager.createQuery(criteria);
		addPageRestrictions(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(financialReleaseFilter));
	}
	
	private void addPageRestrictions(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(FinancialReleaseFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<FinancialRelease> root = criteria.from(FinancialRelease.class);
		
		Predicate[] predicates = createRestrictions(builder, root, filter);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

}
