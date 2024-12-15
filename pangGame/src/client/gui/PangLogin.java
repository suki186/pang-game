package client.gui;

import java.awt.BorderLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PangLogin extends JFrame {
	
	private static final String IP_ADDRESS = "127.0.0.1";
    private static final String PORT_NUM = "30000";
    
	private JTextField nicknameField; // 닉네임
    private JButton submitButton; // 입장 버튼

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PangLogin frame = new PangLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PangLogin() {
		super("PANG-GAME");
		
		setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
		setBounds(100, 100, 310, 139);
		
		// 닉네임 입력 Label, textField	
		JLabel titleLabel = new JLabel("닉네임 입력", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Gothic", Font.BOLD, 12));
        add(titleLabel, BorderLayout.NORTH);
        
        nicknameField = new JTextField(20);
        
        // 입장 Button -> LobbyScreen으로
        submitButton = new JButton("입장");
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(nicknameField);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
		// 액션 리스너 설정
		Myaction action = new Myaction();
		submitButton.addActionListener(action);
		nicknameField.addActionListener(action);
	}
	
	class Myaction implements ActionListener // 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = nicknameField.getText().trim();
			ChatLobby view = new ChatLobby(username, IP_ADDRESS, PORT_NUM); // Lobby로 이동
			setVisible(false);
		}
	}
}
