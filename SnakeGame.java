import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int UNIT = 25;
    private final int TOTAL_UNITS = (WIDTH * HEIGHT) / (UNIT * UNIT);

    private final int[] x = new int[TOTAL_UNITS];
    private final int[] y = new int[TOTAL_UNITS];

    private int bodyParts = 5;
    private int applesEaten = 0;
    private int appleX;
    private int appleY;

    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private int delay = 150;

    Random random;

    SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void restart() {
        bodyParts = 5;
        applesEaten = 0;
        delay = 150;
        direction = 'R';
        running = true;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        newApple();
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {

            // Apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT, UNIT);

            // Snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT, UNIT);
            }

            // Score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + applesEaten, 10, 25);

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(WIDTH / UNIT)) * UNIT;
        appleY = random.nextInt((int)(HEIGHT / UNIT)) * UNIT;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT; break;
            case 'D': y[0] += UNIT; break;
            case 'L': x[0] -= UNIT; break;
            case 'R': x[0] += UNIT; break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();

            // Speed increase
            if (delay > 50) {
                delay -= 5;
                timer.setDelay(delay);
            }
        }
    }

    public void checkCollisions() {
        // Self collision
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        // Wall collision
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 3, HEIGHT / 2);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + applesEaten, WIDTH / 3, HEIGHT / 2 + 40);
        g.drawString("Press SPACE to restart", WIDTH / 3, HEIGHT / 2 + 70);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
        if (!running && e.getKeyCode() == KeyEvent.VK_SPACE) {
            restart();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();

        frame.add(gamePanel);
        frame.setTitle("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}