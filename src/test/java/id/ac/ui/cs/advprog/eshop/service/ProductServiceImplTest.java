package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("123");
        product.setProductName("Test");
    }

    @Test
    void testCreate() {
        when(productRepository.create(product)).thenReturn(product);
        Product result = productService.create(product);
        assertEquals("123", result.getProductId());
    }

    @Test
    void testDelete() {
        productService.delete("123");
        verify(productRepository, times(1)).delete("123");
    }

    @Test
    void testFindAll() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productRepository.findAll()).thenReturn(productList.iterator());

        List<Product> result = productService.findAll();
        assertFalse(result.isEmpty());
        assertEquals("123", result.get(0).getProductId());
    }

    @Test
    void testFindByIdSuccess() {
        when(productRepository.findById("123")).thenReturn(product);
        Product result = productService.findById("123");
        assertNotNull(result);
        assertEquals("123", result.getProductId());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById("999")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> productService.findById("999"));
    }

    @Test
    void testUpdate() {
        when(productRepository.update(product)).thenReturn(product);
        Product result = productService.update(product);
        assertEquals("123", result.getProductId());
    }
}