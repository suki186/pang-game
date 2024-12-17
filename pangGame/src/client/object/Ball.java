package client.object;

import java.awt.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ball {
    private int x, y; // 현재 위치
    private int radius; // 반지름
    private double dx, dy; // 속도
    private int panelWidth, panelHeight; // 패널 크기
    private BufferedImage ballImage; // 공 이미지

    public Ball(int startX, int startY, int radius, double dx, double dy, int panelWidth, int panelHeight) {
        this.x = startX;
        this.y = startY;
        this.radius = radius;
        this.dx = dx * 1.2; // 초기 속도 가속
        this.dy = dy * 1.2; // 초기 속도 가속
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        try {
            ballImage = ImageIO.read(new File("resources/images/ball1.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ball Image Error");
        }
    }

    public void move() {
        // 위치 업데이트
        x += dx;
        y += dy;

        // 벽 충돌 검사 및 반대 방향으로 튕기기
        // X축 경계
        if (x - radius < 0) {
            x = radius;
            dx = -dx; // 반대 방향으로 튕기기
        } else if (x + radius > panelWidth) {
            x = panelWidth - radius;
            dx = -dx;
        }

        // Y축 경계
        if (y - radius < 0) {
            y = radius;
            dy = -dy; // 반대 방향으로 튕기기
        } else if (y + radius > panelHeight) {
            y = panelHeight - radius;
            dy = -dy;
        }
    }

    public void draw(Graphics g) {
        if (ballImage != null) {
            g.drawImage(ballImage, x - radius, y - radius, radius * 2, radius * 2, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    public void setPanelSize(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
    }
}

