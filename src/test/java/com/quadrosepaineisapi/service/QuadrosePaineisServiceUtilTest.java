package com.quadrosepaineisapi.service;

import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import com.quadrosepaineisapi.product.Product;
import com.quadrosepaineisapi.product.ProductRepository;
import com.quadrosepaineisapi.util.QuadrosePaineisServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.quadrosepaineisapi.builders.ProductBuilder.productBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Tag("service")
@ExtendWith(MockitoExtension.class)
@DisplayName("Valida funcionalidades do serviço de utilidades")
public class QuadrosePaineisServiceUtilTest {

    @Mock
    private ProductRepository prodRepository;

    private QuadrosePaineisServiceUtil serviceUtil;

    @BeforeEach
    public void setUp() {
        serviceUtil = new QuadrosePaineisServiceUtil(prodRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar um produto")
    public void shouldThrowProductNotFound() {
        when(prodRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                serviceUtil.getProductById(anyLong()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto estiver desabilitado")
    public void shouldThrowProductInactived() {
        Optional<Product> optProduct = Optional.of(productBuilder().inActive().build());
        when(prodRepository.findById(anyLong())).thenReturn(optProduct);

        assertThrows(BadRequestException.class, () ->
                serviceUtil.getProductById(anyLong()));
    }

    @Test
    @DisplayName("Deve retornar um produto")
    public void shouldReturnAnProduct() {
        Optional<Product> optProduct = Optional.of(productBuilder().build());
        when(prodRepository.findById(anyLong())).thenReturn(optProduct);

        Product product = serviceUtil.getProductById(anyLong());

        assertAll("product",
                () -> assertThat(product.getName(), equalTo("Teste 1")),
                () -> assertTrue(product.getIsActive()),
                () -> assertThat(product.getPrice(), equalTo(15.0)));
    }

}
