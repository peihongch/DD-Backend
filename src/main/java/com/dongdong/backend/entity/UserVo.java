package com.dongdong.backend.entity;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserVo {

    private Long userId;

    private String email;

    private String phone;

    private String userName;

    private int gender;

    private int age;

    private boolean isFriend;

    private boolean isBlacked;

    public UserVo() {
    }

    public UserVo(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.userName = user.getUserName();
        this.age = user.getAge();
        this.gender = user.getGender();
    }


}
