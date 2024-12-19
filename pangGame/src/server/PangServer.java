package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PangServer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JTextArea textArea;

    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // client 소켓
    private Vector<UserService> UserVec = new Vector<>(); // 연결된 사용자 벡터

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PangServer frame = new PangServer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PangServer() {
        setTitle("PANG-Game Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 386);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 264);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setEnabled(false);
        scrollPane.setViewportView(textArea);

        JButton btnServerStart = new JButton("서버 시작");
        btnServerStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new ServerSocket(30000);
                } catch (NumberFormatException | IOException e1) {
                    e1.printStackTrace();
                }

                AppendText("--- 서버가 시작되었습니다 ---");
                btnServerStart.setText("- - - 서버가 시작되었습니다 - - -");
                btnServerStart.setEnabled(false); // 서버 실행 불가

                AcceptServer accept_server = new AcceptServer(); // 멀티 스레드 객체 생성
                accept_server.start();
            }
        });
        btnServerStart.setBounds(12, 300, 300, 35);
        contentPane.add(btnServerStart);
    }

    // 새로운 참가자 accept(), user thread 새로 생성
    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) {
                try {
                    AppendText("클라이언트 대기 중...");
                    client_socket = socket.accept();
                    AppendText("새로운 참가자: " + client_socket);

                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user); // 새로운 참가자 벡터에 추가
                    AppendText("현재 참가자 수: " + UserVec.size());
                    new_user.start(); // 만든 객체의 스레드 실행

                    // 첫 번째 사용자를 방장으로 설정
                    updateHost();
                } catch (IOException e) {
                    AppendText("! ACCEPT ERROR !");
                }
            }
        }
    }

    private void updateHost() {
        if (!UserVec.isEmpty()) {
            UserService host = UserVec.get(0);
            host.isHost = true;
            broadcastMessage("/host " + host.UserName); // 방장 정보 전송
        }
    }
    
    public void broadcastMessage(String message) {
        for (UserService user : UserVec) {
            user.WriteOne(message);
        }
    }

    //JtextArea에 문자열을 출력
    public void AppendText(String str) {
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());  // 커서 위치 마지막으로
    }

    // User 당 생성되는 Thread
    class UserService extends Thread {
        private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client_socket;
        private Vector<UserService> user_vc;
        private String UserName = ""; // 닉네임
        private boolean isHost = false;

        public UserService(Socket client_socket) {
            this.client_socket = client_socket;
            this.user_vc = UserVec;

            try {
                is = client_socket.getInputStream();
                dis = new DataInputStream(is);
                os = client_socket.getOutputStream();
                dos = new DataOutputStream(os);

                String line1 = dis.readUTF(); // "/login UserName"
                String[] msg = line1.split(" ");
                UserName = msg[1].trim(); //공백 기준으로 분할해 앞뒤의 공백을 제거

                AppendText(UserName + " 입장.");
                WriteOne("***** " + UserName + " 님 환영합니다. *****\n"); // 연결된 사용자에게 정상접속을 알림

                String br_msg = "***** " + UserName + " 님이 입장하였습니다.\n"; // 입장 메세지
                WriteAll(br_msg); // 다른 사용자들에게 전달

                UpdateUserList(); // 사용자 목록 갱신
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        // 사용자 목록 -> clientListArea
        public void UpdateUserList() {
            StringBuilder userList = new StringBuilder("/userlist ");
            for (UserService user : UserVec) {
                userList.append(user.UserName).append(",");
            }

            WriteAll(userList.toString());
        }

        // 사용자가 나간 경우
        public void logout() {
            UserVec.removeElement(this); // 현재 객체를 벡터에서 지움
            String br_msg = "***** " + UserName + " 님이 퇴장하였습니다.\n"; // 퇴장 메세지
            WriteAll(br_msg); // 다른 사용자들에게 전달

            AppendText(UserName + " 퇴장.");
            AppendText("현재 참가자 수 " + UserVec.size());

            UpdateUserList(); // 사용자 목록 갱신
            updateHost();
        }

        // 클라이언트로 메시지 전송
        public void WriteOne(String msg) {
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                AppendText("dos.write() error");
                try {
                    dos.close();
                    dis.close();
                    client_socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                logout();
            }
        }

        //모든 클라이언트에게 채팅 메시지 전달
        public void WriteAll(String str) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = user_vc.get(i);
                user.WriteOne(str);
            }
        }

        public void run() {
            try {
                UpdateUserList();
            } catch (Exception e) {
                AppendText("UpdateUserList Error");
            }

            while (true) {
                try {
                    String msg = dis.readUTF();
                    msg = msg.trim();   //msg의 앞뒤 공백 제거
                    AppendText(msg); // server 화면에 출력

                    String[] args = msg.split(" "); // 명령어와 매개변수 분리

                    
                    // 명령어 형식 검사
                    if (args.length < 2) {
                        AppendText("Invalid message format: " + msg); // 유효하지 않은 메시지 로그
                        continue;
                    }

                    // 명령어 처리
                    switch (args[1]) {
                        case "/startgame": // 게임 시작 명령
                            if (isHost) {
                            	AppendText("Game starting by host: " + UserName);
                                WriteAll("/startgame " + UserName); // 방장 이름 포함
                            }
                            break;

                        case "/exit": // 종료 명령
                            logout(); // 사용자 로그아웃 처리
                            return; // 스레드 종료

                        default: // 일반 메시지 처리
                            WriteAll(msg + "\n"); // 모든 사용자에게 전송
                            break;
                    }

                } catch (IOException e) {
                    try {
                        dos.close();
                        dis.close();
                        client_socket.close();
                        logout();
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }

    }
}
