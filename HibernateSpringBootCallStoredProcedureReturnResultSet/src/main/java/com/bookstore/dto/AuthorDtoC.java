package com.bookstore.dto;

import java.io.Serializable;

public class AuthorDtoC implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String nickname;
    private final int age;

    public AuthorDtoC(String nickname, int age) {
        this.nickname = nickname;
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "AuthorDtoC{" + "nickname=" + nickname + ", age=" + age + '}';
    }   
}
