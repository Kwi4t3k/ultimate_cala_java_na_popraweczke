package shop;

import org.example.auth.Account;
import org.example.auth.AccountClass;

import java.util.ArrayList;
import java.util.List;

//miało dziecziczyć po Account ale jest rekordem a rekord jest final i nie można po nim dziedziczyć więc została zrobiona AccountClass, która jest kopią tylko że klasą
public class ShopClient extends AccountClass {
    // Lista koszyków przypisanych do klienta
    private final List<Cart> carts;

    // Konstruktor ShopClient
    public ShopClient(int id, String name) {
        super(id, name); // Wywołanie konstruktora klasy Account
        this.carts = new ArrayList<>(); // Inicjalizacja pustej listy koszyków
    }

    // Metoda dodająca koszyk do klienta
    public void addCart(Cart cart) {
        carts.add(cart);
    }

    // Metoda zwracająca listę koszyków klienta
    public List<Cart> getCarts() {
        return new ArrayList<>(carts); // Zwracanie kopii listy koszyków
    }
}
