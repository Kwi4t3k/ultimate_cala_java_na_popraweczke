package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageProcessor {
    private BufferedImage image;

    public void loadImage(String imagePath) throws IOException {
        File inputFile = new File(imagePath);
        if (!inputFile.exists()) {
            throw new IOException("Plik nie istnieje: " + imagePath);
        }
        image = ImageIO.read(inputFile);
        if (image == null) {
            throw new IOException("Nie udało się wczytać obrazu z pliku: " + imagePath);
        }
    }

    public void saveImage(String outputPath, String format) throws IOException {
        if (image == null) {
            throw new IOException("Brak obrazu do zapisania.");
        }
        File outputFile = new File(outputPath);

        boolean result = ImageIO.write(image, format, outputFile);
        if (!result) {
            throw new IOException("Nie udało się zapisać obrazu w formacie: " + format);
        }
    }

    public void brightenImage(int brightness) {
        // Tworzenie nowego obrazu, który będzie zawierał rozjaśnioną wersję oryginału
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Iteracja przez każdy piksel obrazu
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                // Pobranie koloru piksela
                int rgb = image.getRGB(i, j);

                // Rozdzielenie koloru na komponenty R, G, B
                int r = ((rgb >> 16) & 0xFF) + brightness;
                int g = ((rgb >> 8) & 0xFF) + brightness;
                int b = (rgb & 0xFF) + brightness;

                // Ograniczenie wartości R, G, B do zakresu [0, 255]
                r = Math.min(Math.max(r, 0), 255);
                g = Math.min(Math.max(g, 0), 255);
                b = Math.min(Math.max(b, 0), 255);

                // Składanie nowych wartości R, G, B z powrotem w jeden kolor (RGB)
                int newRgb = (0xFF << 24) | (r << 16) | (g << 8) | b;

                // Ustawienie nowego koloru w nowym obrazie
                newImage.setRGB(i, j, newRgb);
            }
        }

        // Aktualizacja obrazu w obiekcie ImageProcessor do nowo utworzonego, rozjaśnionego obrazu
        image = newImage;
    }

    public void brightenImageMultithreaded(int brightness) {
        // Ustalamy liczbę dostępnych wątków na podstawie liczby rdzeni procesora
        int numberOfThreads = Runtime.getRuntime().availableProcessors();

        // Pobieramy szerokość i wysokość obrazu
        int width = image.getWidth();
        int height = image.getHeight();

        // Obliczamy wysokość fragmentu obrazu, który będzie przetwarzany przez jeden wątek
        int blockSize = height / numberOfThreads;

        // Tworzymy pulę wątków o liczbie wątków równej liczbie dostępnych procesorów
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // Pętla, która uruchomi tyle wątków, ile mamy dostępnych procesorów
        for (int i = 0; i < numberOfThreads; i++) {
            // Ustalanie początku i końca fragmentu obrazu, który dany wątek ma przetwarzać
            int startHeight = i * blockSize;
            int endHeight = (i == numberOfThreads - 1) ? height : startHeight + blockSize;

            // Tworzymy zadanie dla wątku, które rozjaśnia wyznaczony fragment obrazu
            Runnable task = () -> {
                // Przetwarzamy piksele od lewej do prawej, wiersz po wierszu w wyznaczonym przedziale
                for (int x = 0; x < width; x++) {
                    for (int y = startHeight; y < endHeight; y++) {
                        // Pobieramy wartość RGB danego piksela
                        int rgb = image.getRGB(x, y);

                        // Wyodrębniamy składowe koloru (czerwony, zielony, niebieski) i dodajemy jasność
                        int r = ((rgb >> 16) & 0xFF) + brightness;
                        int g = ((rgb >> 8) & 0xFF) + brightness;
                        int b = (rgb & 0xFF) + brightness;

                        // Upewniamy się, że wartości RGB są w przedziale 0-255 (obsługujemy sytuacje przepełnienia)
                        r = Math.min(Math.max(r, 0), 255);
                        g = Math.min(Math.max(g, 0), 255);
                        b = Math.min(Math.max(b, 0), 255);

                        // Tworzymy nową wartość RGB z poprawionymi składowymi koloru
                        int newRgb = (0xFF << 24) | (r << 16) | (g << 8) | b;

                        // Ustawiamy nową wartość RGB dla piksela w obrazie
                        image.setRGB(x, y, newRgb);
                    }
                }
            };

            // Przekazujemy zadanie do wykonania przez jeden z wątków w puli
            executor.submit(task);
        }

        // Kończymy przyjmowanie nowych zadań i czekamy, aż wszystkie wątki zakończą swoje zadania
        executor.shutdown();

        try {
            // Oczekujemy na zakończenie wszystkich wątków, zanim pozwolimy programowi przejść dalej
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // Obsługa wyjątku, jeśli oczekiwanie na zakończenie wątków zostało przerwane
            e.printStackTrace();
        }
    }

    // Metoda do pomiaru czasu wykonania zadania
    public long measureExecutionTime(Runnable task) {
        // Pobranie bieżącego czasu w nanosekundach przed rozpoczęciem zadania
        long startTime = System.nanoTime();

        // Uruchomienie zadania (metoda run() w interfejsie Runnable wykonuje zadanie)
        task.run();

        // Pobranie bieżącego czasu w nanosekundach po zakończeniu zadania
        long endTime = System.nanoTime();

        // Zwrócenie różnicy między czasem zakończenia a czasem rozpoczęcia,
        // co daje czas wykonania zadania w nanosekundach
        return endTime - startTime;
    }

    // Metoda zwiększająca jasność obrazu z wykorzystaniem puli wątków
    public void brightenImageWithThreadPool(int brightness) throws InterruptedException {
        // Tworzenie nowego obrazu, w którym przechowywane będą zmodyfikowane piksele
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Tworzenie puli wątków o liczbie wątków równej wysokości obrazu (jeden wątek na każdy wiersz)
        ExecutorService executorService = Executors.newFixedThreadPool(image.getHeight());

        // Przetwarzanie każdego wiersza obrazu w osobnym wątku
        for (int j = 0; j < image.getHeight(); j++) {
            int row = j; // Zmienna pomocnicza do użycia w lambdzie (wątkach)
            executorService.submit(() -> {
                for (int i = 0; i < image.getWidth(); i++) {
                    int rgb = image.getRGB(i, row);
                    int r = Math.min(((rgb >> 16) & 0xFF) + brightness, 255);
                    int g = Math.min(((rgb >> 8) & 0xFF) + brightness, 255);
                    int b = Math.min((rgb & 0xFF) + brightness, 255);

                    // Zachowanie przezroczystości piksela
                    int p = (255 << 24) | (r << 16) | (g << 8) | b;
                    newImage.setRGB(i, row, p);
                }
            });
        }

        // Zamknięcie puli wątków, nie pozwala na dodawanie nowych zadań
        executorService.shutdown();
        // Oczekiwanie na zakończenie wszystkich wątków
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        // Zastąpienie oryginalnego obrazu nowym, z rozjaśnionymi pikselami
        this.image = newImage;
    }

    // Metoda do obliczania histogramu wybranego kanału (0 - czerwony, 1 - zielony, 2 - niebieski)
    public int[] calculateHistogram(int channel) throws InterruptedException {
        // Tworzenie tablicy histogramu o rozmiarze 256 (dla każdego możliwego poziomu intensywności)
        int[] histogram = new int[256];

        // Pobieranie liczby dostępnych rdzeni procesora
        int cores = Runtime.getRuntime().availableProcessors();

        // Tworzenie puli wątków o wielkości odpowiadającej liczbie rdzeni procesora
        ExecutorService executorService = Executors.newFixedThreadPool(cores);

        // Pobranie szerokości i wysokości obrazu, aby móc przetwarzać każdy piksel
        int width = image.getWidth();
        int height = image.getHeight();

        // Wyliczenie liczby wierszy obrazu, które zostaną przydzielone do każdego wątku
        int rowsPerThread = height / cores;

        // Podział przetwarzania obrazu na zakresy wierszy dla każdego wątku
        for (int t = 0; t < cores; t++) {
            // Obliczenie zakresu wierszy, który będzie przetwarzany przez bieżący wątek
            int startRow = t * rowsPerThread;  // Wiersz początkowy dla tego wątku
            int endRow = (t == cores - 1) ? height : startRow + rowsPerThread;  // Wiersz końcowy

            // Przesyłanie zadania do wykonania przez wątek
            executorService.submit(() -> {
                // Lokalny histogram dla tego wątku (dla jego zakresu wierszy)
                int[] localHistogram = new int[256];

                // Przetwarzanie przypisanego zakresu wierszy
                for (int y = startRow; y < endRow; y++) {
                    for (int x = 0; x < width; x++) {
                        // Pobieranie wartości koloru piksela (RGB) na współrzędnych (x, y)
                        int rgb = image.getRGB(x, y);

                        // Ekstrakcja wartości wybranego kanału z wartości RGB
                        // Kanał jest wybierany przez przesunięcie bitowe (0 dla czerwonego, 1 dla zielonego, 2 dla niebieskiego)
                        int value = (rgb >> (8 * (2 - channel))) & 0xFF;

                        // Inkrementacja wartości lokalnego histogramu dla danej intensywności
                        localHistogram[value]++;
                    }
                }

                // Synchronizacja dodawania lokalnego histogramu do globalnego histogramu
                synchronized (histogram) {
                    for (int i = 0; i < histogram.length; i++) {
                        histogram[i] += localHistogram[i];
                    }
                }
            });
        }

        // Zamknięcie puli wątków, po dodaniu wszystkich zadań
        executorService.shutdown();
        // Oczekiwanie na zakończenie wszystkich zadań w wątkach
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        // Zwracanie ostatecznego histogramu, który zawiera zsumowane wyniki z wszystkich wątków
        return histogram;
    }
}
