package client.gui;

import client.object.Character;
import client.object.Bullet;
import client.object.Ball;
import client.object.Item;
import client.util.CountDown;
import client.util.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private Timer timer; // 프레임 속도
    private Character character; // 캐릭터
    private ArrayList<Bullet> bullets; // 발사된 총알들
    private CountDown countDown; // 남은 시간
    private ArrayList<Ball> balls; // 공들
    private Score score; // 점수
    private ArrayList<Item> items; // 아이템들
    private Random random; // 랜덤 객체
    
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
        items = new ArrayList<>();
        random = new Random();
        
        // 초기 공 생성 (가장 큰 공 4개)
        balls.add(new Ball(100, 0, 50, 3, 2, 1, 732, 500));
//        balls.add(new Ball(200, 0, 50, -2, 3, 1, 732, 500));
//        balls.add(new Ball(400, 0, 50, 3, 2, 1, 732, 500));
//        balls.add(new Ball(500, 0, 50, -2, 3, 1, 732, 500));

        timer = new Timer(10, this); // 10ms 간격으로 화면 갱신
        timer.start();
        
        countDown = new CountDown(); // 타이머 시작
        score = new Score();
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
        
        // 아이템 그리기
        for (Item item : items) {
            item.draw(g);
        }
        
        // 남은시간 그리기
        countDown.draw(g, getWidth(), getHeight());
        
        // 점수 그리기
        score.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	countDown.update(10); // 타이머 업데이트
    	
    	// 남은 시간 종료
    	// GameOver 창 띄우기 (실패)
        if (countDown.isFinished()) {
            timer.stop(); // 게임 멈추기
            SwingUtilities.invokeLater(() -> new GameOver("FAIL"));
            return;
        }
    	
        // 캐릭터 이동 처리
        if (keyStates[KeyEvent.VK_LEFT]) { // 왼쪽 방향키
            character.moveLeft();
        }
        if (keyStates[KeyEvent.VK_RIGHT]) { // 오른쪽 방향키
            character.moveRight();
        }
        if (!keyStates[KeyEvent.VK_LEFT] && !keyStates[KeyEvent.VK_RIGHT]) {
            character.stop();
        }
        
        // 랜덤 아이템 생성
        if (random.nextInt(1000) < 3) { // 0.3% 확률
            String[] itemTypes = {"clock", "coin", "speed"};
            String type = itemTypes[random.nextInt(itemTypes.length)];
            int randomX = random.nextInt(getWidth() - 32);
            items.add(new Item(randomX, getHeight(), type));
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
                    
                    // 공 맞추면 점수 추가
                    switch (ball.getLevel()) {
                    case 1: score.addPoints(5); break;
                    case 2: score.addPoints(10); break;
                    case 3: score.addPoints(20); break;
                    case 4: score.addPoints(50); break;
                    }

                    Ball[] newBalls = ball.split(); // 공을 쪼갬
                    if (newBalls != null) {
                        balls.add(newBalls[0]);
                        balls.add(newBalls[1]);
                    }
                    break;
                }
            }
        }
        
        // 아이템 이동 처리, 캐릭터에 적용
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.move();

            if (item.isExpired()) { // 바닥에서 일정시간 경과하면 사라짐
                items.remove(i);
                i--;
            } // 아이템 효과 처리
            else if (item.isCaught(character.getX(), character.getY())) {
                switch (item.getType()) {
                    case "clock": countDown.addTime(10); break; // 시간 추가
                    case "coin": score.addPoints(10); break; // 점수 추가
                    case "speed": character.increaseSpeed(3); break; // 속도 증가
                }
                items.remove(i);
                i--;
            }
        }
        
        // 공 이동 처리
        for (Ball ball : balls) {
            ball.move(); // 각 Ball 움직이기
        }
        
        // 모든 공 없애면 타이머 멈추기
        if (balls.isEmpty()) {
            countDown.stop();
            timer.stop(); // 게임 멈추기
            
            // GameOver 창 띄우기 (성공)
            SwingUtilities.invokeLater(() -> new GameOver("SUCCESS"));
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
