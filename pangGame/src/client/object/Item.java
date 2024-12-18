package client.object;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Item {
    private int x, y; // 아이템 위치
    private int speed; // 아이템 낙하 속도
    private String type; // 아이템 종류
    private Image itemImage; // 아이템 이미지
    private int panelHeight;

    private boolean onGround; // 바닥에 닿았는지 여부
    private int remainingTime; // 바닥에 닿은 후 남은 시간(ms)

    public Item(int startX, int panelHeight, String type) {
        this.x = startX;
        this.y = 0;
        this.speed = 1;
        this.type = type;
        this.panelHeight = panelHeight;
        this.onGround = false;
        this.remainingTime = 5000; // 5초 유지

        try {
            this.itemImage = ImageIO.read(new File("resources/images/" + type + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Item Image Error: " + type);
        }
    }

    public void move() {
        if (!onGround) {
            y += speed; // 아이템이 바닥에 도달하지 않았으면 이동
            if (y >= panelHeight-70) {
                onGround = true; // 바닥에 도달
                y = panelHeight-70; // 바닥에 잠시 있기
            }
        } else {
            remainingTime -= 10; // 유지 시간 감소
        }
    }

    public boolean isExpired() { // 바닥에 있고 유지 시간이 초과된 경우
        return onGround && remainingTime <= 0;
    }

    public boolean isCaught(int characterX, int characterY) {
        // 충돌 감지
        return x < characterX + 50 &&
               x + 32 > characterX &&
               y < characterY + 60 &&
               y + 32 > characterY;
    }

    public void draw(Graphics g) {
        if (itemImage != null) {
            g.drawImage(itemImage, x, y, 26, 26, null);
        }
    }

    public String getType() {
        return type;
    }
}
