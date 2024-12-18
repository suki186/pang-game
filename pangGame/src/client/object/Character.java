package client.object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Character {
    private int x, y; // 위치
    private int speed; // 속도
    private int initSpeed; // 기본 속도
    private int width = 50, height = 60; // 크기
    private BufferedImage characterImage;

    public Character(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5;
        this.initSpeed = speed;
        
        try {
            // 캐릭터 이미지 로드
            characterImage = ImageIO.read(new File("resources/images/char.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (characterImage != null) {
            g.drawImage(characterImage, x, y, width, height, null); // 캐릭터 그리기
        } else {
            g.setColor(Color.GREEN); // 이미지 로드 실패 시 기본 색상
            g.fillRect(x, y, width, height); // 기본 캐릭터 크기
        }
    }
    
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

    public void moveLeft() {
        if (x > 20) {
            x -= speed;
        }
    }

    public void moveRight() {
        if (x + characterImage.getWidth() < 750) { // 오른쪽 경계 체크
            x += speed;
        }
    }

    public Bullet shoot() {
        return new Bullet(x + characterImage.getWidth() / 2 - width/2, y); // 캐릭터 중심에서 총알 발사
    }
}
