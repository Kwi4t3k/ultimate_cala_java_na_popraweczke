package org.example;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        //zad 1, 2
//        try {
//            ImageProcessor imageProcessor = new ImageProcessor();
//
//            imageProcessor.loadImage("src/main/resources/obraz.jpg");
//
//            imageProcessor.brightenImage(100);
//            imageProcessor.saveImage("src/main/resources/output.png", "png");
//
//            System.out.println("Obraz zapisany pomyślnie");
//
//        } catch (IOException e) {
//            System.err.println("Błąd: " + e.getMessage());
//        }

        //zad 3
//        try {
//            ImageProcessor processor = new ImageProcessor();
//
//            // Wczytanie obrazu
//            processor.loadImage("src/main/resources/obraz.jpg");
//
//            // Pomiar czasu dla jednowątkowego przetwarzania
//            long singleThreadTime = processor.measureExecutionTime(() -> processor.brightenImage(50));
//            System.out.println("Czas wykonania (jednowątkowo): " + singleThreadTime / 1_000_000 + " ms");
//
//            // Pomiar czasu dla wielowątkowego przetwarzania
//            long multiThreadTime = processor.measureExecutionTime(() -> processor.brightenImageMultithreaded(50));
//            System.out.println("Czas wykonania (wielowątkowo): " + multiThreadTime / 1_000_000 + " ms");
//
//            // Zapisanie obrazu
//            processor.saveImage("src/main/resources/output.png", "png");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //zad 4
//        try {
//            ImageProcessor processor = new ImageProcessor();
//
//            // Wczytanie obrazu
//            processor.loadImage("src/main/resources/obraz.jpg");
//
//            // Pomiar czasu wykonania metody jednowątkowej
//            long singleThreadTime = processor.measureExecutionTime(() -> processor.brightenImage(50));
//            System.out.println("Czas wykonania (jednowątkowo): " + singleThreadTime + " ns");
//
//            // Pomiar czasu wykonania metody z pulą wątków
//            long multiThreadTime = processor.measureExecutionTime(() -> {
//                try {
//                    processor.brightenImageWithThreadPool(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            System.out.println("Czas wykonania (wielowątkowo): " + multiThreadTime + " ns");
//
//            // Zapis zmodyfikowanego obrazu
//            processor.saveImage("src/main/resources/output.png", "png");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //zad 5
        try {
            ImageProcessor processor = new ImageProcessor();

            // Wczytanie obrazu
            processor.loadImage("src/main/resources/obraz.jpg");

            // Obliczenie histogramu dla kanału czerwonego
            long startTime = System.nanoTime();
            int[] redHistogram = processor.calculateHistogram(0);
            long duration = System.nanoTime() - startTime;

            System.out.println("Czas wykonania (pula wątków, kanał czerwony): " + duration + " ns");

            // Przykładowe wyświetlenie histogramu
            for (int i = 0; i < redHistogram.length; i++) {
                System.out.println("Intensywność " + i + ": " + redHistogram[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}