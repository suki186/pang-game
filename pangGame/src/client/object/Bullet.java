package client.object;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet {
    private int x, y; // 위치
    private int speed = 7; // 속도
    private int width = 15, height = 20; // 크기
    private BufferedImage bulletImage;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            // 총알 이미지 로드
            bulletImage = ImageIO.read(new File("resources/images/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, width, height, null); // 총알 그리기
        } else {
            g.setColor(Color.RED); // 이미지 로드 실패 시 기본 색상
            g.fillRect(x, y, width, height); // 기본 총알 크기
        }
    }

    public void move() {
        y -= speed; // 위로 이동
    }

    public int getY() {
        return y;
    }
}
