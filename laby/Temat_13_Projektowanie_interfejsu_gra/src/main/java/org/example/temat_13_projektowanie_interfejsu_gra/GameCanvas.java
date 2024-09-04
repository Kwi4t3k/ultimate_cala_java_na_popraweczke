package org.example.temat_13_projektowanie_interfejsu_gra;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameCanvas extends Canvas {
    private GraphicsContext graphicsContext;
    private Paddle paddle;
    private Ball ball;
    private boolean gameRunning;
    private long lastFrameTime;

    private List<Brick> bricks;

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

        // Inicjalizacja listy cegieł
        bricks = new ArrayList<>();

        // Ładowanie poziomu z cegłami
        loadlevel();

        // Rysujemy początkowy stan gry (czarne tło i platforma)
        draw();

        // Przechwytywanie zdarzeń ruchu myszy
        this.setOnMouseMoved(this::handleMouseMoved);

        // Przechwytywanie kliknięcia myszy, aby rozpocząć grę
        this.setOnMouseClicked(event -> handleMouseClicked(event));

        // Uruchomienie pętli gry
        startGameLoop();
    }

    // Rysowanie tła, platformy, piłki i cegieł
    public void draw() {
        // Wypełnienie tła kolorem czarnym
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        // Rysowanie platformy
        paddle.draw(graphicsContext);

        //Rysowanie piłki
        ball.draw(graphicsContext);

        //Rysowanie cegieł
        for (Brick brick : bricks) {
            brick.draw(graphicsContext);
        }
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

                    // Sprawdzanie warunków odbicia
                    if (shouldBallBounceHorizontally()) {
                        ball.bounceHorizontally();
                    }
                    if (shouldBallBounceVertically()) {
                        ball.bounceVertically();
                    }
                    if (shouldBallBounceFromPaddle()) {
                        ball.bounceVertically();
                    }

                    // Sprawdzanie kolizji z cegłami
                    checkCollisionsWithBricks();
                }
                // Rysowanie wszystkiego
                draw();
            }
        };
        // Start pętli gry
        gameLoop.start();
    }

    private boolean shouldBallBounceHorizontally() {
        // Odbicie od lewej lub prawej krawędzi ekranu
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= getWidth()) {
            return true;
        }

        return false;
    }

    private boolean shouldBallBounceVertically() {
        // Odbicie od górnej krawędzi ekranu
        if (ball.getY() <= 0) {
            return true;
        }

        // Odbicie od dolnej krawędzi ekranu
        if (ball.getY() + ball.getHeight() >= getHeight()) {
            // Tu możemy dodatkowo zakończyć grę, jeśli piłka spadnie na dół
            gameRunning = false;
            return true;
        }

        return false;
    }

    private boolean shouldBallBounceFromPaddle() {
        // Odbicie od platformy
        if (ball.getY() + ball.getHeight() >= paddle.getY() &&
                ball.getY() + ball.getHeight() <= paddle.getY() + paddle.getHeight() &&
                ball.getX() + ball.getWidth() > paddle.getX() &&
                ball.getX() < paddle.getX() + paddle.getWidth()) {
            return true;
        }
        return false;
    }

    // Metoda ładująca poziom z cegłami
    public void loadlevel() {
        // Ustawienie siatki na 20 wierszy i 10 kolumn
        Brick.setGrid(20, 10);

        // Dodanie cegieł do poziomu
        for (int row = 2; row <= 7 ; row++) {
            // Kolory w zależności od wiersza
            Color color = Color.hsb(360.0 * (row - 2) / 6, 1.0, 1.0);

            for (int col = 0; col < 10; col++) {
                bricks.add(new Brick(col, row, color));
            }
        }
    }

    private void checkCollisionsWithBricks() {
        Iterator<Brick> iterator = bricks.iterator();

        while (iterator.hasNext()) {
            Brick brick = iterator.next();
            Brick.CrushType crushType = brick.checkCollision(ball.getTop(), ball.getBottom(), ball.getLeft(), ball.getRight());

            if (crushType != Brick.CrushType.NoCrush) {
                if (crushType == Brick.CrushType.HorizontalCrush) {
                    ball.bounceHorizontally();
                } else if (crushType == Brick.CrushType.VerticalCrush) {
                    ball.bounceVertically();
                }

                // Usunięcie cegły po uderzeniu
                iterator.remove();
                break;
            }
        }
    }
}