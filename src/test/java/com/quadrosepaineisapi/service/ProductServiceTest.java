package com.quadrosepaineisapi.service;

import com.quadrosepaineisapi.builders.ProductBuilder;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.ProductRepository;
import com.quadrosepaineisapi.util.QuadrosePaineisServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.quadrosepaineisapi.builders.ProductBuilder.productBuilder;
import static java.util.function.Predicate.isEqual;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("service")
@ExtendWith(MockitoExtension.class)
@DisplayName("Valida funcionalidades do serviço de Produtos")
public class ProductServiceTest {

    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private QuadrosePaineisServiceUtil serviceUtil;

    @BeforeEach
    public void setUp() {
        this.service = new ProductServiceImpl(repository, serviceUtil);
    }

    @Test
    @DisplayName("Deve executar o save")
    public void testCreate() {
        Product product = Product.builder().name("Teste 1").price(50.0).build();
        when(repository.getProductsLength()).thenReturn(0);

        when(repository.save(any(Product.class))).thenReturn(product);

        service.create(product);

        verify(repository).save(product);
    }

    @Test
    @DisplayName("Deve retornar um produto")
    public void testGet() {
        Product product = Product.builder().name("Teste 1").price(50.0).build();
        when(serviceUtil.getProductById(anyLong())).thenReturn(product);

        Product prodReturned = service.view(anyLong());

        assertEquals("Teste 1", prodReturned.getName());
    }

    @Test
    @DisplayName("Deve atualizar um produto")
    public void testUpdate() {

        Product product = productBuilder().build();

        when(serviceUtil.getProductById(anyLong())).thenReturn(product);

        service.update(1L, product);

        verify(repository).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve atualiazar a propriedade isActive")
    public void testUpdateIsActive() {
        when(serviceUtil.getProductById(anyLong())).thenReturn(productBuilder().build());

        service.updateIsActiveProperty(1L, false);

        ArgumentCaptor<Product> argProd = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(argProd.capture());
        assertEquals(false, argProd.getValue().getIsActive());
    }

    @Test
    @DisplayName("Deve atualizar as sequencias")
    public void testUpdateSequence() {
        List<Product> products = new ArrayList<>();
        products.add(productBuilder().withSequence(1).withId(1L).build());
        products.add(productBuilder().withSequence(2).withId(2L).build());
        products.add(productBuilder().withSequence(3).withId(3L).build());

        Product prod1 = new Product();
        prod1.setSequence(10);
        Product prod2 = new Product();
        prod1.setSequence(11);
        Product prod3 = new Product();
        prod1.setSequence(13);

        when(serviceUtil.getProductById(1L)).thenReturn(prod1);
        when(serviceUtil.getProductById(2L)).thenReturn(prod2);
        when(serviceUtil.getProductById(3L)).thenReturn(prod3);

        service.updateSequence(products);

        assertAll("product",
                () -> assertThat(prod1.getSequence(), equalTo(1)),
                () -> assertThat(prod2.getSequence(), equalTo(2)),
                () -> assertThat(prod3.getSequence(), equalTo(3)));

    }

}
