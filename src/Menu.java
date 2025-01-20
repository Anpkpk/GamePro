import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {
    JButton playButton = new JButton("Play");
    JButton selectButton = new JButton("Select Map");

    public int key = 0;
    public int map = 0;

    JLabel label = new JLabel("Snake Game");

    JComboBox<String> comboBox;
    String[] select = { "Easy", "Medium", "Hard" };

    // Create menu
    Menu(int Width, int Height) {
        this.setSize(Width, Height);
        this.setBackground(new Color(0x9BD300));
        this.setLayout(null);

        comboBox = new JComboBox<>(select);
        setSelect(comboBox);
        this.add(comboBox);

        setPlayButton();
        setSelectButton();
        setLabel();
    }

    public void setSelect(JComboBox<String> comboBox) {
        comboBox.setBounds(225, 430, 150, 30);
        comboBox.setFont(new Font("Arial", Font.BOLD,13));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBackground(new Color(0x9DB300));
        comboBox.setForeground(new Color(0x2F3A5D));
    }

    // Create Snake Game Label
    public void setLabel() {
        this.add(label);
        label.setBounds(205, 150,300, 100);
        label.setBackground(new Color(0xE2F1A0));
        label.setForeground(new Color(0xE2F1A0));
        label.setFont(new Font("Arial", Font.BOLD, 35));
    }

    // Create Play button
    public void setPlayButton() {
        playButton.addActionListener(this);
        playButton.setFocusable(false);
        playButton.setBounds(200, 250, 200, 100);
        playButton.setFont(new Font("Arial", Font.BOLD, 25));
        playButton.setForeground(new Color(0xFFFFFF));
        playButton.setVerticalAlignment(JButton.CENTER);
        playButton.setHorizontalAlignment(JButton.CENTER);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setOpaque(false);
        this.add(playButton);
    }

    // Create
    public void setSelectButton() {
        selectButton.addActionListener(this);
        selectButton.setFocusable(false);
        selectButton.setBounds(200, 350, 200, 100);
        selectButton.setFont(new Font("Arial", Font.BOLD, 25));
        selectButton.setForeground(new Color(0xFFFFFF));
        selectButton.setVerticalAlignment(JButton.CENTER);
        selectButton.setHorizontalAlignment(JButton.CENTER);
        selectButton.setFocusPainted(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setBorderPainted(false);
        selectButton.setOpaque(false);
        this.add(selectButton);
    }

    public int toBoard() {
        return key;
    }

    public int toSelect() {
        return this.map;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            key = 1;
            toBoard();
        }
        String tmp = (String) comboBox.getSelectedItem();
        if (tmp == "Easy") {
            this.map = 0;
        }
        if (tmp == "Medium") {
            this.map = 1;
        }
        if (tmp == "Hard") {
            this.map = 2;
        }
    }

}
