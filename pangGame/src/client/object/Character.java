package client.object;

import java.awt.*;
import javax.swing.*;

public class Character {
    private int x, y; // 위치
    private int speed; // 속도
    private int initSpeed; // 기본 속도
    private int width = 50, height = 60; // 크기
    //private Image characterImage;
    
    private String state;
    private Image idleImage, leftImage, rightImage;

    public Character(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5;
        this.initSpeed = speed;
        this.state = "IDLE";
        
        // 캐릭터 이미지 로드
        //characterImage = new ImageIcon("resources/images/char_idle.gif").getImage();
        idleImage = new ImageIcon("resources/images/char_idle.gif").getImage();
        leftImage = new ImageIcon("resources/images/char_left.gif").getImage();
        rightImage = new ImageIcon("resources/images/char_right.gif").getImage();

    }
    
    public void draw(Graphics g) {
        Image currentImage;
        switch (state) {
            case "LEFT": currentImage = leftImage; break;
            case "RIGHT": currentImage = rightImage; break;
            default: currentImage = idleImage; // IDLE 상태
        }

        g.drawImage(currentImage, x, y, width, height, null);
    }

//    public void draw(Graphics g) {
//        if (characterImage != null) {
//            g.drawImage(characterImage, x, y, width, height, null); // 캐릭터 그리기
//        } else {
//            g.setColor(Color.GREEN); // 이미지 로드 실패 시 기본 색상
//            g.fillRect(x, y, width, height); // 기본 캐릭터 크기
//        }
//    }
    
    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
    
    // 속도 증가
    public void increaseSpeed(int increment) {
        speed += increment;
        new Timer(5000, e -> {
            speed = initSpeed; // 일정 시간 후 원래 속도로 복구
            ((Timer) e.getSource()).stop();
        }).start();
    }

    public void moveLeft() { // 왼쪽으로
        if (x > 10) {
            x -= speed;
        }
        state = "LEFT";
    }

    public void moveRight() { // 오른쪽으로
        if (x + width < 720) {
            x += speed;
        }
        state = "RIGHT";
    }
    
    public void stop() {
        state = "IDLE"; // 가만히 있는 상태
    }

    public Bullet shoot() {
        return new Bullet(x + width / 2 - width/2, y); // 캐릭터 중심에서 총알 발사
    }
}
