package com.company;

import javax.swing.*;

public class GameFrame extends JFrame {

    ImageIcon image = new ImageIcon("assets/Mascot.png");

    GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Piethon");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(image.getImage());
    }

}

