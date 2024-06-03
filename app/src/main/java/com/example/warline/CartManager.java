package com.example.warline;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<KeranjangI> selectedProducts = new ArrayList<>();
    public static List<KeranjangI> getSelectedProducts() {
        return selectedProducts;
    }
    public static void setSelectedProducts(List<KeranjangI> selectedProducts) {
        CartManager.selectedProducts = selectedProducts;
    }
}