package client.util;

import java.awt.*;

public class Score {
    private static int score;

    public Score() {
        this.score = 0;
    }

    public void addPoints(int points) {
        score += points;
    }

    public static int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("DOSIyagiBoldface", Font.BOLD, 20));
        g.drawString("SCORE:" + score, 10, 30); // 좌측 상단에 점수 표시
    }
}
