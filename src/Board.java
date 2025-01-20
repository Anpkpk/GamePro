import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 600;
    int boardHeight = 600;
    int tileSize = 25;
    int map;
    boolean isPaused = false;
    Random random;

    Snake snake;
    Pixels food;
    ArrayList<Pixels> rock;
    Pixels boom;

    Game game;

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton pauseButton = new JButton("■");
    JButton restartButton = new JButton("↻");

    Board(int map) {
        this.map = map;
        this.setBackground(new Color(0xBBB8B8));
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.addKeyListener(this);
        setFocusable(true);

        game = new Game(this);

        snake = new Snake(5, 5);
        food = new Pixels(10, 10);
        rock = new ArrayList<Pixels>(6);
        boom = new Pixels(12, 12);

        random = new Random();
        placeFoodBoom();
        addPauseButton();
        addRestartButton();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Create board
    public void draw(Graphics g) {
        // Draw Grid
        g.setColor(new Color(0x9D9D9D));
        for (int i = 0; i <= boardWidth / tileSize; i++) {
            // (x1, y1, x2, y2)
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Draw Food
        g.setColor(Color.red);
        g.fillOval(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

         // Set Coordinates of Rocks
        setRocks();

        g.setColor(new Color(107, 103, 103, 223));
        // Draw rocks
        for (Pixels r : rock) {
            g.fillRect(r.x * tileSize, r.y * tileSize, tileSize, tileSize);
        }

        //Draw Boom
        g.setColor(new Color(0x1F1F2A));
        g.fillOval(boom.x * tileSize, boom.y * tileSize, tileSize, tileSize);

        // Draw Snake
        snake.drawSnake(g);

        // Show Score
        g.setFont(new Font("Arial", Font.BOLD, 16));
        if (game.gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + (snake.body.size() - 2), tileSize - 16, tileSize);
        }
        else {
            g.setColor(Color.green);
            g.drawString("Score: " + (snake.body.size() - 2), tileSize - 16, tileSize);
            g.drawString("Levels: " + game.level, tileSize - 16, tileSize + 16);
        }
    }

    // Random boom and food
    public void placeFoodBoom() {
        do {
            food.x = random.nextInt(boardWidth / tileSize);
            food.y = random.nextInt(boardHeight / tileSize);

            boom.x = random.nextInt(boardWidth / tileSize);
            boom.y = random.nextInt(boardHeight / tileSize);
        } while (collision(food, boom) || collision(food, rock) || collision(boom, rock));
    }

    public void setRocks() {
        if (this.map == 0) {
            rock.add(new Pixels(11, 11));
        }
        if (this.map == 1) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    if (i == 0 || i == 5) {
                        rock.add(new Pixels(j + 8, i + 8));
                    } else {
                        rock.add(new Pixels(8, i + 8));
                    }
                }
            }
        }
        if (this.map == 2) {
            for (int i = 6; i <= 17; i++) {
                rock.add(new Pixels(i, 2));
                rock.add(new Pixels(i, 21));
                rock.add(new Pixels(2, i));
                rock.add(new Pixels(21, i));
            }

            for (int i = 8; i <= 11; i++) {
                rock.add(new Pixels(6, i));
                rock.add(new Pixels(17, i));
            }

            rock.add(new Pixels(7, 17));
            rock.add(new Pixels(16, 17));

            for (int i = 8; i <= 15; i++) {
                rock.add(new Pixels(i, 18));
            }

            for (int i = 0; i <= 3; i++) {
                rock.add(new Pixels(i + 10, i + 4));
                rock.add(new Pixels((3 - i) + 10, i + 4));
            }
        }
    }

    // Move Snake
    public void move() {
        // Eat food
        if (collision(snake.head, food)) {
            snake.body.add(new Pixels(food.x, food.y));
            placeFoodBoom();
        }

        // Move snake body
        for (int i = snake.body.size()-1; i >= 0; i--) {
            Pixels snakePart = snake.body.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snake.head.x;
                snakePart.y = snake.head.y;
            }
            else {
                Pixels prevSnakePart = snake.body.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move Snake head
        snake.head.x += game.velocityX;
        snake.head.y += game.velocityY;

        // Game over conditions
        for (int i = 0; i < snake.body.size(); i++) {
            Pixels snakePart = snake.body.get(i);

            //collide with snake head
            if (collision(snake.head, snakePart)) {
                game.gameOver = true;
            }
        }

        // Collide with rock
        for (Pixels pixels : rock) {
            if (collision(snake.head, pixels)) {
                game.gameOver = true;
            }
        }

        // Collide with boom
        if (collision(snake.head, boom)) {
            game.gameOver = true;
        }

        // Collide with board
        if (snake.head.x*tileSize < 0 || snake.head.x*tileSize > boardWidth || //passed left border or right border
            snake.head.y*tileSize < 0 || snake.head.y*tileSize > boardHeight ) { //passed top border or bottom border
            game.gameOver = true;
        }

        increaseLevel();
    }

    // Check two Pixels collide
    public boolean collision(Pixels pixels1, Pixels pixels2) {
        return pixels1.x == pixels2.x && pixels1.y == pixels2.y;
    }

    // Check Pixel with Pixels
    public boolean collision(Pixels pixels1, ArrayList<Pixels> pixels2) {
        for (Pixels pixel : pixels2) {
            if (collision(pixels1, pixel)) {
                return true;
            }
        }
        return false;
    }

    // Add pause button
    public void addPauseButton() {
        pauseButton.addActionListener(this);
        pauseButton.setFocusable(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setForeground(Color.WHITE);
        buttonPanel.setOpaque(false);
        buttonPanel.add(pauseButton);
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.NORTH);
    }

    // Add restart button
    public  void addRestartButton() {
        restartButton.addActionListener(this);
        restartButton.setFocusable(false);
        restartButton.setFocusPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setBorderPainted(false);
        restartButton.setForeground(Color.WHITE);
        buttonPanel.setOpaque(false);
        buttonPanel.add(restartButton);
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.NORTH);
    }

    public void increaseLevel() {
        if (snake.body.size() <= 5) {
            game.gameLoop.setDelay(180);
            game.level = 1;
        }
        if (snake.body.size() > 5) {
            game.gameLoop.setDelay(150);
            game.level = 2;
        }
        if (snake.body.size() > 10) {
            game.gameLoop.setDelay(120);
            game.level = 3;
        }
        if (snake.body.size() > 15) {
            game.gameLoop.setDelay(100);
            game.level = 4;
        }
        if (snake.body.size() >= 20) {
            game.gameLoop.setDelay(10);
            game.level = 5;
        }
    }

    public void restartGame() {
        snake = null;
        snake = new Snake(5,5);
        game.gameOver = false;
        placeFoodBoom();
        game.velocityY = 0;
        game.velocityX = 1;
        game.gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        if (game.gameOver) {
            game.gameLoop.stop();
            int optionPane = JOptionPane.showConfirmDialog(this,
                    "Bạn đã thua:" +
                            "\nĐiểm: " + (snake.body.size() - 2) +
                            "\nChơi lại", "Thông báo", JOptionPane.OK_CANCEL_OPTION);

            if (optionPane == JOptionPane.OK_OPTION) {
                restartGame();
            }
            else {}
        }
        else {
            if (e.getSource() == pauseButton && !isPaused) {
                game.gameLoop.stop();
                pauseButton.setText("▶");
                isPaused = true;
            }
            else if (e.getSource() == pauseButton && isPaused){
                game.gameLoop.start();
                pauseButton.setText("■");
                isPaused = false;
            }
        }
        if (e.getSource() == restartButton) {
            restartGame();
        }
        move();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && game.velocityY != 1) {
            game.velocityX = 0;
            game.velocityY = -1;
        }
        else if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && game.velocityY != -1) {
            game.velocityX = 0;
            game.velocityY = 1;
        }
        else if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && game.velocityX != 1) {
            game.velocityX = -1;
            game.velocityY = 0;
        }
        else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && game.velocityX != -1) {
            game.velocityX = 1;
            game.velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
