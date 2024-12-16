package client.gui;
import client.object.Character;
import client.object.Bullet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Timer timer; // 타이머
    private Character character; // 캐릭터
    private ArrayList<Bullet> bullets; // 발사된 총알들
    
    private boolean[] keyStates = new boolean[256];

    public GamePanel() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyStates[e.getKeyCode()] = true;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) { // 스페이스바
                    bullets.add(character.shoot());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyStates[e.getKeyCode()] = false; // 키 해제 상태 저장
            }
        });

        character = new Character(300, 400); // 초기 캐릭터 위치
        bullets = new ArrayList<>();

        timer = new Timer(10, this); // 10ms 간격으로 화면 갱신
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //setBackground(Color.BLACK); // 배경색

        // 캐릭터 그리기
        character.draw(g);

        // 총알 그리기
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 이동 처리
        if (keyStates[KeyEvent.VK_LEFT]) { // 왼쪽 방향키
            character.moveLeft();
        }
        if (keyStates[KeyEvent.VK_RIGHT]) { // 오른쪽 방향키
            character.moveRight();
        }

        // 총알 움직임 갱신
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();
            if (bullet.getY() < 0) {
                bullets.remove(i); // 화면 위로 벗어난 총알 제거
                i--;
            }
        }

        repaint(); // 화면 갱신
    }

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}

}
