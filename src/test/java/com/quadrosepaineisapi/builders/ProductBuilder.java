package com.quadrosepaineisapi.builders;

import com.quadrosepaineisapi.product.Product;

public class ProductBuilder {

    private Product product;

    private ProductBuilder(){}

    public static ProductBuilder productBuilder() {
        ProductBuilder builder = new ProductBuilder();
        builder.product = new Product();
        builder.product.setIsActive(true);
        builder.product.setName("Teste 1");
        builder.product.setPrice(15.0);
        return builder;
    }

    public ProductBuilder inActive() {
        product.setIsActive(false);
        return this;
    }

    public ProductBuilder withSequence(Integer sequence) {
        product.setSequence(sequence);
        return this;
    }

    public ProductBuilder withId(Long id) {
        product.setId(id);
        return this;
    }

    public Product build() {
        return product;
    }

}
