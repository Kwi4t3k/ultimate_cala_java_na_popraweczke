package shop;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final int id;
    private final Map<Product, Integer> productQuantities;

    public Cart(int id) {
        this.id = id;
        this.productQuantities = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void add(Product product, int quantity) {
        productQuantities.put(product, quantity);
    }

    public double price() {
        double total = 0.0;
        for (Map.Entry<Product, Integer> entry : productQuantities.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            total += product.cena() * quantity;
        }
        return total;
    }
}
