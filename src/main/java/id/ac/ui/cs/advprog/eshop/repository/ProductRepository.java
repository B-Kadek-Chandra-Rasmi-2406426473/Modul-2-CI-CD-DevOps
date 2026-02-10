package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.NoSuchElementException;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productData.add(product);
        return product;
    }

    public void delete(String id) {
        boolean wasRemoved = productData.removeIf(p -> p.getProductId().equals(id));
        if (!wasRemoved) {
            throw new NoSuchElementException("Product ID " + id + " not found");
        }
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

}
