package com.quadrosepaineisapi.model;

import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Double> diameter;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, Double> width;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, Double> weight;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile ListAttribute<Product, Category> categories;
	public static volatile SingularAttribute<Product, Boolean> isActive;
	public static volatile SingularAttribute<Product, LocalDateTime> registerDate;
	public static volatile SingularAttribute<Product, Double> height;

}

