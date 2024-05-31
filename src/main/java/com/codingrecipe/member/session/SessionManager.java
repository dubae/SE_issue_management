package com.codingrecipe.member.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, String> sessionMap = new HashMap<>();

    public static String generateKey() {
        String key;
        do {
            key = UUID.randomUUID().toString().substring(0, 20); // 랜덤 문자열 생성
        } while (sessionMap.containsKey(key)); // 키가 이미 존재하는지 확인

        return key;
    }

    public static void setSession(String key, String username) {
        sessionMap.put(key, username);
    }

    public static Object getSession(String key) {
        return sessionMap.get(key);
    }

    public static void removeSession(String key) {
        sessionMap.remove(key);
    }

    public static void test() {
        sessionMap.put("testtesttesttesttest", "admin");
    }
}
