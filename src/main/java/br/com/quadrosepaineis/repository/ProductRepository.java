package br.com.quadrosepaineis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.quadrosepaineis.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

}
