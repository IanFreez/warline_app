package com.example.warline;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private static ProductManager instance;
    private List<ProductI> productList;

    private ProductManager() {
        // Initialize your product list here
        productList = new ArrayList<>();
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public List<ProductI> getProductList() {
        return productList;
    }

}
