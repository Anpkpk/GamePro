import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        int width = 600;
        int height = 600;

        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setVisible(true);

        Menu menu = new Menu(width, height);
        frame.add(menu);
        checkMenu(menu, frame);

        frame.pack();
    }

    public static void checkMenu(Menu menu, JFrame frame) {
        while (menu.isShowing()) {
            if (menu.toBoard() == 1) {
                Board board = new Board(menu.toSelect());
                frame.add(board);
                board.requestFocus();
                menu.setVisible(false);
            }
        }
    }
}