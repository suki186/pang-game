package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScreen extends JFrame {

    public GameScreen() {
        // 기본 프레임 설정
        setTitle("PANG-GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 732, 500);
        getContentPane().setLayout(null);

        // 게임 패널 설정
        GamePanel gamePanel = new GamePanel();
        gamePanel.setBounds(0, 0, 732, 500); // GamePanel의 크기 설정
        getContentPane().add(gamePanel); // 패널 추가

        setVisible(true);
    }

    public static void main(String[] args) {
        new GameScreen();
    }
}
