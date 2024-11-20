package client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LobbyScreen extends JFrame {

    public LobbyScreen() {
        // 기본 프레임 설정
        setTitle("로비 화면");
        setSize(732, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null); // null 레이아웃 사용

        // 왼쪽 TextArea (채팅 영역)
        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFocusable(false);
        chatArea.setBounds(26, 45, 273, 345); // 위치(x, y)와 크기(width, height)
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBounds(26, 45, 273, 345); // TextArea에 스크롤 추가
        getContentPane().add(chatScrollPane);

        // 채팅 입력 필드 (텍스트 필드)
        JTextField chatField = new JTextField();
        chatField.setBounds(26, 400, 217, 25); // 채팅 입력 필드 크기 설정
        getContentPane().add(chatField);

        // 보내기 버튼
        JButton sendButton = new JButton("→");

        sendButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        sendButton.setBounds(249, 400, 50, 25); // 버튼 위치와 크기 설정
        getContentPane().add(sendButton);

        // 오른쪽 TextArea (닉네임 목록)
        JTextArea clientListArea = new JTextArea();
        clientListArea.setEditable(false);
        clientListArea.setFocusable(false);
        clientListArea.setBounds(484, 274, 211, 77); // 위치(x, y)와 크기
        getContentPane().add(clientListArea);

        // 게임 시작 버튼
        JButton startGameButton = new JButton("게임 시작");
        startGameButton.setBounds(484, 373, 211, 57); // 버튼 위치와 크기 설정
        getContentPane().add(startGameButton);

        // 기본 배경 색 설정 (예: 핑크색)
        getContentPane().setBackground(new Color(255, 192, 203)); // 배경색: 연핑크

        // 더미 데이터 추가
        chatArea.setText("채팅 메시지 1\n채팅 메시지 2\n채팅 메시지 3...");
        clientListArea.setText("[접속자 목록]\n- User1\n- User2");

        // 화면 중앙에 배치
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LobbyScreen();
    }
}
