package client.object;

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
    private int level; // 공 레벨 (1: 가장 큼, 4: 가장 작음)
    private BufferedImage ballImage; // 공 이미지

    public Ball(int startX, int startY, int radius, double dx, double dy, int level, int panelWidth, int panelHeight) {
        this.x = startX;
        this.y = startY;
        this.radius = radius;
        this.dx = dx * 1.0; // 초기 속도 가속
        this.dy = dy * 1.0; // 초기 속도 가속
        this.level = level;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        try {
            ballImage = ImageIO.read(new File("resources/images/ball" + level + ".png"));
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
        } else if (y + radius > panelHeight -30) {
            y = panelHeight - radius-30;
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
    
    // 총알과 공 충돌 감지
    public boolean isHit(int bulletX, int bulletY) {
        return Math.hypot(bulletX - x, bulletY - y) <= radius;
    }
    
    public Ball[] split() {
        if (level >= 4) return null; // 레벨4 공은 더 이상 쪼개지지 않음
        int newRadius = radius - 12; // 공의 크기 줄임
        return new Ball[] {
            new Ball(x, y, newRadius, dx, -Math.abs(dy), level + 1, panelWidth, panelHeight),
            new Ball(x, y, newRadius, -dx, -Math.abs(dy), level + 1, panelWidth, panelHeight)
        };
    }

    public int getLevel() {
        return level;
    }


    public void setPanelSize(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
    }
}

