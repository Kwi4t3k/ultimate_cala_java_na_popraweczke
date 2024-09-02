package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameCanvas extends Canvas {
    private GraphicsContext graphicsContext;
    private Paddle paddle;
    private Ball ball;
    private boolean gameRunning;
    private long lastFrameTime;

    public GameCanvas(double width, double height) {
        super(width, height); // Ustawienie rozmiarów kanwy

        // Ustawiamy statyczne pola szerokości i wysokości kanwy
        GraphicsItem.setCanvasWidthHeight(width, height);

        // Pobieramy kontekst graficzny dla kanwy
        graphicsContext = this.getGraphicsContext2D();

        // Tworzymy nową platformę i umieszczamy ją w odpowiednim miejscu
        paddle = new Paddle();

        // Tworzymy nową piłkę
        ball = new Ball();

        // Ustawiamy początkową pozycję piłki na środku platformy
        ball.setPosition(new Point2D(paddle.getX() + paddle.getWidth() / 2, paddle.getY() + paddle.getHeight()));

        // Początkowo gra jest zatrzymana
        gameRunning = false;

        // Rysujemy początkowy stan gry (czarne tło i platforma)
        draw();

        // Przechwytywanie zdarzeń ruchu myszy
        this.setOnMouseMoved(this::handleMouseMoved);

        // Przechwytywanie kliknięcia myszy, aby rozpocząć grę
        this.setOnMouseClicked(event -> handleMouseClicked(event));

        // Uruchomienie pętli gry
        startGameLoop();
    }

    // Rysowanie tła, platformy i piłki
    public void draw() {
        // Wypełnienie tła kolorem czarnym
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        // Rysowanie platformy
        paddle.draw(graphicsContext);

        //Rysowanie piłki
        ball.draw(graphicsContext);
    }

    // Metoda obsługująca ruch myszy
    public void handleMouseMoved(MouseEvent event) {
        // Przesunięcie platformy w zależności od pozycji myszy
        paddle.move(event.getX());

        if (!gameRunning) {
            // Ustawienie piłki nad platformą, jeśli gra jeszcze nie rozpoczęta
            ball.setPosition(new Point2D(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - ball.getHeight()));
        }

        // Odświeżenie ekranu
        draw();
    }

    // Metoda obsługująca kliknięcie myszy, rozpoczynająca grę
    private void handleMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && !gameRunning) {
            // Rozpoczęcie gry po kliknięciu
            gameRunning = true;
            lastFrameTime = System.nanoTime();
        }
    }

    // Pętla gry, aktualizuje pozycję piłki w każdym kroku
    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameRunning) {
                    // Obliczanie czasu, który upłynął od ostatniej klatki (w sekundach)
                    double seconds = (now - lastFrameTime) / 1_000_000_000.0;
                    lastFrameTime = now;

                    // Aktualizacja pozycji piłki
                    ball.updatePosition(seconds);
                }
                // Rysowanie wszystkiego
                draw();
            }
        };
        // Start pętli gry
        gameLoop.start();
    }
}
