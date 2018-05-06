package com.quadrosepaineisapi.model.release;

import com.quadrosepaineisapi.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FinancialRelease.class)
public abstract class FinancialRelease_ {

	public static volatile SingularAttribute<FinancialRelease, String> obs;
	public static volatile SingularAttribute<FinancialRelease, Product> product;
	public static volatile SingularAttribute<FinancialRelease, LocalDate> dueDate;
	public static volatile SingularAttribute<FinancialRelease, String> description;
	public static volatile SingularAttribute<FinancialRelease, Long> id;
	public static volatile SingularAttribute<FinancialRelease, FinancialReleaseType> type;
	public static volatile SingularAttribute<FinancialRelease, FinancialReleaseCategory> category;
	public static volatile SingularAttribute<FinancialRelease, BigDecimal> value;
	public static volatile SingularAttribute<FinancialRelease, LocalDate> registerDate;
	public static volatile SingularAttribute<FinancialRelease, LocalDate> payDate;

}

