package client.gui;

import javax.swing.*;
import java.awt.*;


public class LoginScreen extends JFrame {
    
    public LoginScreen() {

        super("Welcome!");

        getContentPane().setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(310, 139);
        
        // Create components
        JLabel titleLabel = new JLabel("닉네임을 입력하세요", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Gothic", Font.BOLD, 12));
        
        JTextField nicknameField = new JTextField(20);
        JButton submitButton = new JButton("입장");
        
        // Add components to the frame
        getContentPane().add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(nicknameField);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}

