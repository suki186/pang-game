package client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChatLobby extends JFrame {
	private static final int BUF_LEN = 128;
	
    private String UserName; // 닉네임
    
    private JLabel lblUserName; // 닉네임 표시
    private JTextArea chatArea; // 채팅 화면
    private JTextField chatField; // 메세지 입력창
    private JButton sendButton; // 전송 버튼
    private JTextArea clientListArea; // 사용자 목록
    private JButton startGameButton; // 게임 시작 버튼
    
    private Socket socket; // 소켓
    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;
    
	public ChatLobby(String username, String ip_addr, String port_no) {
		UserName = username;
		
		// 기본 프레임 설정
        setTitle("PANG-GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
		setBounds(100, 100, 732, 500);

		// 왼쪽 TextArea (채팅 영역)
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFocusable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBounds(26, 45, 273, 345);
        add(chatScrollPane);
        
        // 채팅 입력 필드 (텍스트 필드)
        chatField = new JTextField();
        chatField.setBounds(26, 400, 217, 25);
        add(chatField);

        // 보내기 버튼
        sendButton = new JButton("→");
        sendButton.setBounds(249, 400, 50, 25);
        add(sendButton);
		
        // 사용자 닉네임 Label
		lblUserName = new JLabel(UserName);
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(110, 23, 100, 25);
		add(lblUserName);
		
		// 오른쪽 TextArea (닉네임 목록)
        clientListArea = new JTextArea();
        clientListArea.setEditable(false);
        clientListArea.setFocusable(false);
        clientListArea.setBounds(484, 274, 211, 77);
        add(clientListArea);
        
        // 게임 시작 버튼
        startGameButton = new JButton("START");
        startGameButton.setBounds(484, 373, 211, 57);
        add(startGameButton);
		
		
		setVisible(true);

        try {
            socket = new Socket(ip_addr, Integer.parseInt(port_no));
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

            SendMessage("/login " + UserName);
            ListenNetwork net = new ListenNetwork();
            net.start();
            
            Myaction action = new Myaction();
            sendButton.addActionListener(action); // 액션 리스너 설정
            chatField.addActionListener(action);
            chatField.requestFocus();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            AppendText("connect error");
        }
    }

    // 서버의 메세지를 화면에 표시
    class ListenNetwork extends Thread {
        public void run() {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    
                    if (msg.startsWith("/userlist")) {
                        UpdateClientList(msg);
                    } else {
                        AppendText(msg);
                    }
                } catch (IOException e) {
                    AppendText("dis.read() error");
                    try {
                        dos.close();
                        dis.close();
                        socket.close();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
    
    // 사용자 목록을 clientListArea에 업데이트
    public void UpdateClientList(String msg) {
        String[] parts = msg.split(" ", 2);
        if (parts.length > 1) {
            String[] users = parts[1].split(",");
            clientListArea.setText(""); // 기존 목록 초기화
            for (String user : users) {
                if (!user.trim().isEmpty()) {
                    clientListArea.append(user + "\n");
                }
            }
        }
    }

	// 입력창에 쓴 메세지를 서버로 전송
	class Myaction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// 전송버튼을 누르거나 Enter key 치면
			if (e.getSource() == sendButton || e.getSource() == chatField) {
				String msg = null;
				msg = String.format("[%s] %s\n", UserName, chatField.getText());
				SendMessage(msg);// 입력한 사람과 내용 전송
				
				chatField.setText(""); // 입력창 비우기
				chatField.requestFocus(); // 커서를 입력창으로
				
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

    // 채팅화면에 출력
    public void AppendText(String msg) {
    	chatArea.append(msg);
    	chatArea.setCaretPosition(chatArea.getText().length());
    }

    // Server에게 network으로 전송
    public void SendMessage(String msg) {
        try {
            // Use writeUTF to send messages
            dos.writeUTF(msg);
        } catch (IOException e) {
            AppendText("dos.write() error");
            try {
                dos.close();
                dis.close();
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }
}
