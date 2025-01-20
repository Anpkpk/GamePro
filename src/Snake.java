import java.awt.*;
import java.util.ArrayList;

public class Snake {
    Pixels head;
    ArrayList<Pixels> body;
    int tileSize = 25;

    // Create snake
    Snake(int x, int y) {
        head = new Pixels(x, y);
        body = new ArrayList<Pixels>();
        body.add(new Pixels(4,5));
        body.add(new Pixels(3,5));
    }

    // draw snake
    public void drawSnake(Graphics g) {
        g.setColor(new Color(0x26AD2B));
        g.fillRect(head.x * tileSize, head.y * tileSize, tileSize, tileSize);

        // Snake body
        for (int i = 0; i < body.size(); i++) {
            Pixels snakePart = body.get(i);
            if (i % 2 == 0) {
                g.setColor(new Color(0x87FF6B));
            }
            else {
                g.setColor(new Color(0x41FF17));
            }
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }
    }
}
