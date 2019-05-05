package com.quadrosepaineisapi.service;

import com.quadrosepaineisapi.builders.ProductBuilder;
import com.quadrosepaineisapi.exceptionhandler.BadRequestException;
import com.quadrosepaineisapi.exceptionhandler.ResourceNotFoundException;
import com.quadrosepaineisapi.model.Product;
import com.quadrosepaineisapi.repository.ProductRepository;
import com.quadrosepaineisapi.util.QuadrosePaineisServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(prodRepository.findOne(anyLong())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                serviceUtil.getProductById(anyLong()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o produto estiver desabilitado")
    public void shouldThrowProductInactived() {
        when(prodRepository.findOne(anyLong())).thenReturn(productBuilder().inActive().build());

        assertThrows(BadRequestException.class, () ->
                serviceUtil.getProductById(anyLong()));

    }

    @Test
    @DisplayName("Deve retornar um produto")
    public void shouldReturnAnProduct() {
        when(prodRepository.findOne(anyLong())).thenReturn(productBuilder().build());

        Product product = serviceUtil.getProductById(anyLong());

        assertAll("product",
                () -> assertThat(product.getName(), equalTo("Teste 1")),
                () -> assertTrue(product.getIsActive()),
                () -> assertThat(product.getPrice(), equalTo(15.0)));
    }

}
