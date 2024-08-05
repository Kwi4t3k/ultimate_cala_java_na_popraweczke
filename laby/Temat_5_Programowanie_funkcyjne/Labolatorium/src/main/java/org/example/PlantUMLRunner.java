package org.example;

import java.io.*;

public class PlantUMLRunner {
    private static String plantUMLJarPath;

    // Metoda do ustawienia ścieżki do uruchamialnego pliku jar PlantUML
    public static void setplantUMLJarPath(String path){
        plantUMLJarPath = path;
    }

    // Metoda do wygenerowania schematu UML
    public static void generateDiagram(String dataForFile, String outputPath, String outputFileName) throws IOException, InterruptedException {
        // Sprawdzenie czy ścieżka do jar została ustawiona
        if (plantUMLJarPath == null || plantUMLJarPath.isEmpty()){
            throw new IllegalStateException("Ścieżka do pliku jar PlantUML nie istnieje");
        }

        // Tworzenie katalogu wyjściowego jeśli nie istnieje
        File outputDir = new File(outputPath);
        if (!outputDir.exists()){
            outputDir.mkdirs();
        }

        // Tworzenie tymczasowego pliku z zawartością UML
        File tmpUmlFile = new File(outputDir, "tmp.uml");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tmpUmlFile))){
            bufferedWriter.write(dataForFile);
        }

        // Ścieżka do pliku wynikowego
        File outputFile = new File(outputDir, outputFileName);

        // Przygotowanie i uruchomienie procesu generowania diagramu
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", plantUMLJarPath,
                tmpUmlFile.getAbsolutePath(), "-o", outputDir.getAbsolutePath()
        );
//        ProcessBuilder: Jest to klasa używana do tworzenia instancji procesów.
//      "java": Komenda, którą chcemy uruchomić. W tym przypadku jest to java, co oznacza, że chcemy uruchomić JVM.
//      "-jar": Flaga oznaczająca, że chcemy uruchomić aplikację Java z pliku JAR.
//      plantUMLJarPath: Ścieżka do pliku plantuml.jar.
//      tmpUmlFile.getAbsolutePath(): Ścieżka do tymczasowego pliku UML, który zawiera definicję diagramu.
//      "-o": Flaga używana przez PlantUML do określenia katalogu wyjściowego.
//      outputDir.getAbsolutePath(): Ścieżka do katalogu, w którym ma być zapisany wygenerowany diagram.

        Process process = processBuilder.start();
//        start(): Metoda start() uruchamia nowy proces zgodnie z konfiguracją ustawioną w ProcessBuilder. Zwraca obiekt typu Process.

        int exitCode = process.waitFor();
//        waitFor(): Metoda waitFor() sprawia, że bieżący wątek (program) czeka, aż nowo uruchomiony proces zakończy swoje działanie. Zwraca kod zakończenia procesu.
//        exitCode: Kod zakończenia procesu. Zwykle 0 oznacza sukces, a inne wartości oznaczają błędy.

        // Debugowanie: odczyt wyjścia procesu
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
        }
//          getErrorStream(): Pobiera strumień błędów procesu. Możemy użyć tego strumienia do odczytywania komunikatów o błędach, które proces może generować.
//          BufferedReader: Używany do odczytu tekstu ze strumienia błędów procesów w sposób buforowany.

        // Sprawdzenie czy proces zakończył się powodzeniem
        if (exitCode != 0){
            throw new RuntimeException("Błąd podczas generowania diagramu UML. Kod zakończenia: " + exitCode);
        }

        // Usunięcie tymczasowego pliku UML
        if (tmpUmlFile.exists()){
            tmpUmlFile.delete();
        }
    }
}
