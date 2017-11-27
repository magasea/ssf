package com.shellshellfish.aaas.userinfo.model.dto;

import java.util.List;

public class UserFriends {
    Long userId;
    List<Long> friends;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }
}
