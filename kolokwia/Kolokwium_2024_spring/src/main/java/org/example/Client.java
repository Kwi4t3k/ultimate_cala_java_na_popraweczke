// Implementacja klienta GUI i komunikacja z serwerem
package org.example;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import static java.lang.System.exit;

// Klasa klienta, który komunikuje się z serwerem
public class Client {
    // Adres serwera
    private final static String uriBase = "http://localhost:8080";
    // Rozmiary płótna (canvas) do rysowania pikseli
    private final static int width = 512;
    private final static int height = 512;

    // Pole do wprowadzenia tokena
    private final TextField token;
    // Pole do wprowadzenia koloru (w formacie hex)
    private final TextField color;
    // Klient REST do komunikacji z serwerem
    private final RestClient restClient = RestClient.create();

    // Struktura przechowująca informacje o pikselu
    private record Pixel(String id, int x, int y, String color) {}

    // Klasa wewnętrzna reprezentująca płótno do rysowania pikseli
    private class PixelCanvas extends Canvas {
        Color[][] pixels; // Tablica dwuwymiarowa przechowująca kolory pikseli
        Consumer<Point> pixelClicked; // Funkcja do wywołania, gdy użytkownik kliknie piksel

        // Ustawienie funkcji obsługi kliknięcia na pikselu
        public void setPixelClicked(Consumer<Point> setPixel) {
            this.pixelClicked = setPixel;
        }

        // Konstruktor płótna, inicjalizuje czarne piksele (kolor 0)
        public PixelCanvas() {
            setSize(new Dimension(width, height));
            pixels = new Color[width][height];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    pixels[i][j] = new Color(0); // Inicjalizacja na kolor czarny
                }
            }

            // Dodanie nasłuchiwacza zdarzeń myszy (obsługa kliknięć)
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    // Pobranie współrzędnych kliknięcia myszą
                    int x = event.getX();
                    int y = event.getY();
                    // Ustawienie koloru piksela na podstawie wprowadzonego koloru
                    pixels[y][x] = new Color(Integer.parseInt(color.getText(), 16));
                    // Wywołanie funkcji kliknięcia z podanymi współrzędnymi
                    pixelClicked.accept(new Point(x, y));
                    // Odświeżenie płótna
                    repaint();
                }
            });
        }

        // Metoda malująca piksele na płótnie
        @Override
        public void paint(Graphics g) {
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    g.setColor(pixels[i][j]); // Ustawienie koloru piksela
                    g.fillRect(j, i, 1, 1); // Rysowanie pojedynczego piksela
                }
            }
        }
    }

    // Konstruktor klienta
    public Client() {
        // Tworzenie okna GUI
        Frame frame = new Frame("Client");
        frame.setSize(600, 640); // Ustawienie rozmiaru okna
        frame.setLayout(new GridBagLayout()); // Użycie układu GridBagLayout

        // Tworzenie przycisku rejestracji i pól tekstowych
        Button button = new Button("Register");
        token = new TextField(50);
        color = new TextField(50);
        color.setText("FF0000"); // Ustawienie domyślnego koloru na czerwony (hex)

        // Obsługa przycisku rejestracji - po kliknięciu pobiera nowy token
        button.addActionListener(actionEvent -> {
            token.setText(register());
        });

        // Tworzenie płótna do rysowania pikseli
        PixelCanvas canvas = new PixelCanvas();

        // Przypisanie funkcji do obsługi kliknięcia piksela
        canvas.setPixelClicked(point -> setPixel(point.x, point.y));

        // Dodanie elementów GUI do okna
        frame.add(button, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(token, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(new Label("Color"), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(color, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(canvas, new GridBagConstraints(0, 2, 2, 1, 1.0, 100.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));

        frame.setVisible(true); // Wyświetlenie okna

        // Dodanie nasłuchiwacza zamykania okna
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(0); // Zamykanie aplikacji
            }
        });
    }

    // Metoda rejestracji - wysyła żądanie na serwer i zwraca nowy token
    public String register() {
        ResponseEntity<String> response = restClient.post()
                .uri(uriBase + "/register")
                .retrieve()
                .toEntity(String.class);
        return response.getBody(); // Zwraca token otrzymany z serwera
    }

    // Metoda ustawiająca piksel na serwerze
    public void setPixel(int x, int y) {
        Pixel pixel = new Pixel(token.getText(), x, y, color.getText());
        restClient.post()
                .uri(uriBase + "/pixel")
                .contentType(MediaType.APPLICATION_JSON)
                .body(pixel) // Przesyła dane piksela
                .retrieve()
                .toBodilessEntity(); // Wysłanie żądania bez oczekiwania odpowiedzi
    }

    // Główna metoda aplikacji - uruchamia klienta
    public static void main(String[] args) {
        Client client = new Client(); // Tworzy nową instancję klienta
    }
}