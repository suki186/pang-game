package client.util;

public class User {
	private static String nickname;

    public static void setNickname(String name) {
        nickname = name; // 닉네임 저장
    }

    public static String getNickname() {
        return nickname; // 닉네임 반환
    }
}
