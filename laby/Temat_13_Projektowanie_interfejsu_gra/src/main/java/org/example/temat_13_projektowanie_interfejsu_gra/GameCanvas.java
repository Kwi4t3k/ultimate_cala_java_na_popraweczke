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
    private GraphicsContext graphicsContext;  // Kontekst graficzny do rysowania na Canvas
    private Paddle paddle;  // Platforma
    private Ball ball;  // Piłka
    private boolean gameRunning;  // Flaga wskazująca, czy gra jest w toku
    private long lastFrameTime;  // Czas ostatniej klatki

    private List<Brick> bricks;  // Lista cegieł w grze

    public GameCanvas(double width, double height) {
        super(width, height);  // Ustawienie rozmiarów Canvas

        // Ustawienie statycznych wymiarów Canvas dla innych obiektów
        GraphicsItem.setCanvasWidthHeight(width, height);

        // Pobranie kontekstu graficznego do rysowania
        graphicsContext = this.getGraphicsContext2D();

        // Inicjalizacja platformy
        paddle = new Paddle();

        // Inicjalizacja piłki
        ball = new Ball();

        // Ustawienie początkowej pozycji piłki nad platformą
        ball.setPosition(new Point2D(paddle.getX() + paddle.getWidth() / 2, paddle.getY() + paddle.getHeight()));

        // Gra nie jest jeszcze rozpoczęta
        gameRunning = false;

        // Inicjalizacja listy cegieł
        bricks = new ArrayList<>();

        // Ładowanie poziomu gry (dodawanie cegieł)
        loadlevel();

        // Rysowanie początkowego stanu gry
        draw();

        // Obsługa zdarzenia ruchu myszy
        this.setOnMouseMoved(this::handleMouseMoved);

        // Obsługa kliknięcia myszy w celu rozpoczęcia gry
        this.setOnMouseClicked(event -> handleMouseClicked(event));

        // Uruchomienie pętli gry
        startGameLoop();
    }

    // Metoda rysująca tło, platformę, piłkę i cegły
    public void draw() {
        // Wypełnienie tła kolorem czarnym
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());

        // Rysowanie platformy
        paddle.draw(graphicsContext);

        // Rysowanie piłki
        ball.draw(graphicsContext);

        // Rysowanie cegieł
        for (Brick brick : bricks) {
            brick.draw(graphicsContext);
        }
    }

    // Metoda obsługująca ruch myszy i przesuwająca platformę
    public void handleMouseMoved(MouseEvent event) {
        // Przesuwanie platformy do pozycji myszy
        paddle.move(event.getX());

        if (!gameRunning) {
            // Ustawienie piłki nad platformą, gdy gra nie jest jeszcze rozpoczęta
            ball.setPosition(new Point2D(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - ball.getHeight()));
        }

        // Odświeżenie ekranu
        draw();
    }

    // Metoda obsługująca kliknięcie myszy i rozpoczynająca grę
    private void handleMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && !gameRunning) {
            // Rozpoczęcie gry po kliknięciu
            gameRunning = true;
            lastFrameTime = System.nanoTime();  // Zapisanie czasu rozpoczęcia gry
        }
    }

    // Pętla gry, aktualizuje pozycję piłki i sprawdza kolizje
    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameRunning) {
                    // Obliczenie czasu, który upłynął od ostatniej klatki (w sekundach)
                    double seconds = (now - lastFrameTime) / 1_000_000_000.0;
                    lastFrameTime = now;

                    // Aktualizacja pozycji piłki
                    ball.updatePosition(seconds);

                    // Sprawdzanie warunków odbicia piłki
                    if (shouldBallBounceHorizontally()) {
                        ball.bounceHorizontally();
                    }
                    if (shouldBallBounceVertically()) {
                        ball.bounceVertically();
                    }
                    if (gameRunning && shouldBallBounceFromPaddle()) {
//                        ball.bounceVertically();
                        // Odbicie piłki od platformy z uwzględnieniem pozycji uderzenia
                        ball.bounceFromPaddle(calculateHitPosition());
                    }

                    // Sprawdzanie kolizji piłki z cegłami
                    checkCollisionsWithBricks();
                }
                // Rysowanie aktualnego stanu gry
                draw();
            }
        };
        // Start pętli gry
        gameLoop.start();
    }

    // Sprawdza, czy piłka powinna odbić się poziomo od krawędzi ekranu
    private boolean shouldBallBounceHorizontally() {
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= getWidth()) {
            return true;  // Odbicie od lewej lub prawej krawędzi
        }
        return false;
    }

    // Sprawdza, czy piłka powinna odbić się pionowo od krawędzi ekranu
    private boolean shouldBallBounceVertically() {
        if (ball.getY() <= 0) {
            return true;  // Odbicie od górnej krawędzi
        }

        if (ball.getY() + ball.getHeight() >= getHeight()) {
            // Jeżeli piłka spadnie poniżej dolnej krawędzi, kończymy grę
            gameRunning = false;
            return true;
        }
        return false;
    }

    // Sprawdza, czy piłka powinna odbić się od platformy
    private boolean shouldBallBounceFromPaddle() {
        return ball.getBottom() >= paddle.getY() &&  // Sprawdzenie, czy piłka jest na wysokości platformy
                ball.getTop() <= paddle.getY() + paddle.getHeight() &&  // Sprawdzenie, czy piłka jest powyżej platformy
                ball.getRight() > paddle.getX() &&  // Sprawdzenie, czy piłka przecina lewą krawędź platformy
                ball.getLeft() < paddle.getX() + paddle.getWidth();  // Sprawdzenie, czy piłka przecina prawą krawędź platformy
    }

    // Oblicza, w którym miejscu piłka uderza w platformę
    private double calculateHitPosition() {
        return (ball.getX() + ball.getWidth() / 2 - (paddle.getX() + paddle.getWidth() / 2)) / (paddle.getWidth() / 2);
    }

    // Metoda ładująca poziom z cegłami
    public void loadlevel() {
        // Ustawienie siatki na 20 wierszy i 10 kolumn
        Brick.setGrid(20, 10);

        // Dodanie cegieł do poziomu
        for (int row = 2; row <= 7; row++) {
            // Kolor cegieł w zależności od wiersza
            Color color = Color.hsb(360.0 * (row - 2) / 6, 1.0, 1.0);

            for (int col = 0; col < 10; col++) {
                // Tworzenie cegieł w siatce
                bricks.add(new Brick(col, row, color));
            }
        }
    }

    // Metoda sprawdzająca kolizje piłki z cegłami
    private void checkCollisionsWithBricks() {
        // Iteracja po cegłach
        Iterator<Brick> iterator = bricks.iterator();

        while (iterator.hasNext()) {
            Brick brick = iterator.next();
            // Sprawdzanie kolizji cegły z piłką
            Brick.CrushType crushType = brick.checkCollision(ball.getTop(), ball.getBottom(), ball.getLeft(), ball.getRight());

            if (crushType != Brick.CrushType.NoCrush) {
                // Odbicie piłki w zależności od typu kolizji
                if (crushType == Brick.CrushType.HorizontalCrush) {
                    ball.bounceHorizontally();
                } else if (crushType == Brick.CrushType.VerticalCrush) {
                    ball.bounceVertically();
                }

                // Usunięcie cegły po kolizji
                iterator.remove();
                break;
            }
        }
    }
}