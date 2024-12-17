package client.gui;

import client.object.Character;
import client.object.Bullet;
import client.object.Ball;
import client.object.CountDown;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Timer timer; // 프레임 속도
    private Character character; // 캐릭터
    private ArrayList<Bullet> bullets; // 발사된 총알들
    private CountDown countDown; // 남은 시간
    private ArrayList<Ball> balls;
    
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
        balls = new ArrayList<>();
        
        // 초기 공 생성 (가장 큰 공 4개)
        balls.add(new Ball(100, 0, 50, 3, 2, 1, 732, 500));
        balls.add(new Ball(200, 0, 50, -2, 3, 1, 732, 500));
        balls.add(new Ball(400, 0, 50, 3, 2, 1, 732, 500));
        balls.add(new Ball(500, 0, 50, -2, 3, 1, 732, 500));

        timer = new Timer(10, this); // 10ms 간격으로 화면 갱신
        timer.start();
        
        countDown = new CountDown(); // 타이머 시작
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
        
        // 공 그리기
        for (Ball ball : balls) {
            ball.draw(g);
        }
        
        // 남은시간 그리기
        countDown.draw(g, getWidth(), getHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	countDown.update(10); // 타이머 업데이트
    	
        // 캐릭터 이동 처리
        if (keyStates[KeyEvent.VK_LEFT]) { // 왼쪽 방향키
            character.moveLeft();
        }
        if (keyStates[KeyEvent.VK_RIGHT]) { // 오른쪽 방향키
            character.moveRight();
        }

        // 총알 이동 처리, 공 충돌 검사
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.move();

            if (bullet.getY() < 0) {
                bullets.remove(i); // 화면 밖으로 나간 총알 제거
                i--;
                continue;
            }

            // 공과의 충돌 검사
            for (int j = 0; j < balls.size(); j++) {
                Ball ball = balls.get(j);
                if (ball.isHit(bullet.getX(), bullet.getY())) {
                    bullets.remove(i); // 충돌한 총알 제거
                    balls.remove(j);   // 충돌한 공 제거
                    i--;
                    Ball[] newBalls = ball.split(); // 공을 쪼갬
                    if (newBalls != null) {
                        balls.add(newBalls[0]);
                        balls.add(newBalls[1]);
                    }
                    break;
                }
            }
        }
        
        // 공 이동 처리
        for (Ball ball : balls) {
            ball.move(); // 각 Ball 움직이기
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
