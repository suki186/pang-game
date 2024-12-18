package client.gui;
import javax.swing.*;

import client.util.User;
import client.util.Score;

import java.awt.*;
import java.awt.event.*;

public class GameOver extends JFrame {
	private String result;
	
    private String nickname1;
    private String nickname2 = "Player2";
    private int score1;
    private int score2 = 1800;
    private int coin1 = 1800;
    private int coin2 = 1800;

    public GameOver(String result) {
    	this.result = result;
    	
    	nickname1 = User.getNickname();
    	score1 = Score.getScore();

    	
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(true); // 프레임 테두리 제거
        setLayout(new BorderLayout());

        // 패널 추가
        GameOverPanel panel = new GameOverPanel();
        add(panel, BorderLayout.CENTER);

        pack(); // 패널 크기에 맞게 프레임 크기 설정
        setLocationRelativeTo(null); // 화면 중앙에 배치
        setVisible(true);
    }

    // 내부 패널 클래스
    private class GameOverPanel extends JPanel {
        public GameOverPanel() {
            setPreferredSize(new Dimension(500, 300));
            setBackground(new Color(240, 200, 200)); // 배경색 (핑크)
            setLayout(null);
            
            // GAME OVER 표시
            JLabel gameoverLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
            gameoverLabel.setFont(new Font("D2coding", Font.BOLD, 30));
            gameoverLabel.setBounds(150, 20, 200, 30);
            add(gameoverLabel);


            // Player 1 정보
            addPlayerInfo(nickname1, score1, coin1, 100, 80);

            // Player 2 정보
            addPlayerInfo(nickname2, score2, score2, 300, 80);

            // 메인으로 버튼
            JButton mainButton = new JButton("메인으로");
            mainButton.setBounds(400, 10, 80, 30);
            mainButton.setBackground(Color.DARK_GRAY);
            mainButton.setForeground(Color.WHITE);
            mainButton.setFocusPainted(false);

            mainButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // 현재 창 닫기
                    System.out.println("메인으로 버튼이 클릭되었습니다.");
                }
            });

            add(mainButton);
        }

        private void addPlayerInfo(String nickname, int score, int coin, int x, int y) {
        	// 닉네임
            JLabel nameLabel = new JLabel(nickname, SwingConstants.CENTER);
            nameLabel.setOpaque(true);
            nameLabel.setBackground(Color.DARK_GRAY);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
            nameLabel.setBounds(x, y, 150, 30);
            add(nameLabel);

            // 점수
            JLabel scoreLabel = new JLabel("점수: " + score, SwingConstants.LEFT);
            scoreLabel.setFont(new Font("D2coding", Font.PLAIN, 14));
            scoreLabel.setBounds(x, y + 50, 150, 20);
            add(scoreLabel);

            // 코인
            JLabel coinLabel = new JLabel("  🪙   " + coin, SwingConstants.LEFT);
            coinLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
            coinLabel.setBounds(x, y + 80, 150, 20);
            add(coinLabel);
        }
    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(GameOver::new);
//    }
}

