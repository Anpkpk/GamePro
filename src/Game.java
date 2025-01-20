import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;
    int level;

    Game(Board board) {
        velocityX = 1;
        velocityY = 0;
        level = 1;

        gameLoop = new Timer(200, board);
        gameLoop.start();
    }
}
