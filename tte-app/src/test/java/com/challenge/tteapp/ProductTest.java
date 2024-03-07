package com.challenge.tteapp;

import com.challenge.tteapp.model.Product;
import com.challenge.tteapp.model.Rating;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductTest {

    @Test
    void createProductTest() {
        // Create a product
        Product product = new Product();
        product.setTitle("WD 2TB Elements Portable External Hard Drive - USB 3.0");
        product.setPrice(BigDecimal.valueOf(64.00));
        product.setDescription("USB 3.0 and USB 2.0 Compatibility Fast data transfers Improve PC Performance High Capacity; Compatibility Formatted NTFS for Windows 10, Windows 8.1, Windows 7; Reformatting may be required for other operating systems; Compatibility may vary depending on user’s hardware configuration and operating system");
        product.setCategory("electronics");
        product.setImage("https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg");
        Rating rating = new Rating();
        rating.setRate(3.3);
        rating.setCount(203);
        product.setRating(rating);

        // Validate product attributes
        assertNotNull(product);
        assertEquals("WD 2TB Elements Portable External Hard Drive - USB 3.0", product.getTitle());
        assertEquals(BigDecimal.valueOf(64.00), product.getPrice());
        assertEquals("USB 3.0 and USB 2.0 Compatibility Fast data transfers Improve PC Performance High Capacity; Compatibility Formatted NTFS for Windows 10, Windows 8.1, Windows 7; Reformatting may be required for other operating systems; Compatibility may vary depending on user’s hardware configuration and operating system", product.getDescription());
        assertEquals("electronics", product.getCategory());
        assertEquals("https://fakestoreapi.com/img/61IBBVJvSDL._AC_SY879_.jpg", product.getImage());
        assertEquals(3.3, product.getRating().getRate());
        assertEquals(203, product.getRating().getCount());
    }
}
