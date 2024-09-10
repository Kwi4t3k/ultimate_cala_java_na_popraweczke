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

public class Client {
    private final static String uriBase = "http://localhost:8080";
    private final static int width = 512;
    private final static int height = 512;

    private final TextField token;
    private final TextField color;
    private final RestClient restClient = RestClient.create();

    private record Pixel(String id, int x, int y, String color) {}

    private class PixelCanvas extends Canvas {
        Color[][] pixels;
        Consumer<Point> pixelClicked;

        public void setPixelClicked(Consumer<Point> setPixel) {
            this.pixelClicked = setPixel;
        }

        public PixelCanvas() {
            setSize(new Dimension(width, height));
            pixels = new Color[width][height];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    pixels[i][j] = new Color(0);
                }
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    int x = event.getX();
                    int y = event.getY();
                    pixels[y][x] = new Color(Integer.parseInt(color.getText(), 16));
                    pixelClicked.accept(new Point(x, y));
                    repaint();
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[i].length; j++) {
                    g.setColor(pixels[i][j]);
                    g.fillRect(j, i, 1, 1);
                }
            }
        }
    }

    public Client() {
        Frame frame = new Frame("Client");
        frame.setSize(600, 640);
        frame.setLayout(new GridBagLayout());

        Button button = new Button("Register");
        token = new TextField(50);
        color = new TextField(50);
        color.setText("FF0000");

        button.addActionListener(actionEvent -> {
            token.setText(register());
        });

        PixelCanvas canvas = new PixelCanvas();

        canvas.setPixelClicked(point -> setPixel(point.x, point.y));

        frame.add(button, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(token, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(new Label("Color"), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(color, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(canvas, new GridBagConstraints(0, 2, 2, 1, 1.0, 100.0, GridBagConstraints.BASELINE, GridBagConstraints.BASELINE, new Insets(0, 0, 0, 0), 0, 0));

        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(0);
            }
        });

    }

    public String register() {
        ResponseEntity<String> response = restClient.post()
                .uri(uriBase + "/register")
                .retrieve()
                .toEntity(String.class);
        return response.getBody();
    }

    public void setPixel(int x, int y) {
        Pixel pixel = new Pixel(token.getText(), x, y, color.getText());
        restClient.post()
                .uri(uriBase + "/pixel")
                .contentType(MediaType.APPLICATION_JSON)
                .body(pixel)
                .retrieve()
                .toBodilessEntity();
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

}
