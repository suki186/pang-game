package client.util;

import java.awt.*;

public class CountDown {
    private int remainingTime; // 남은 시간 (s)
    private boolean isRunning; // 타이머 상태
    private int elapsedTime;   // 경과 시간 누적 (ms)

    public CountDown() {
        this.remainingTime = 5;
        this.isRunning = true;
        this.elapsedTime = 0;
    }

    // GamePanel의 Timer와 충돌 방지
    public void update(int deltaTime) { // deltaTime: 타이머 호출 주기(ms)
        if (isRunning && remainingTime > 0) {
            elapsedTime += deltaTime; // 경과 시간 누적
            if (elapsedTime >= 1000) { // 1초(1000ms)마다 시간 감소
                remainingTime--;
                elapsedTime = 0; // 경과 시간 초기화
            }
        }
    }

    public void stop() {
        isRunning = false;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
    
    public boolean isFinished() {
        return remainingTime <= 0; // 남은 시간이 0 이하일 때 true
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("DOSIyagiBoldface", Font.BOLD, 30));
        String timeText = "TIME:" + String.format("%03d", remainingTime) + "s";
        g.drawString(timeText, panelWidth - 160, 30); // 우측상단에 표시
    }
}
