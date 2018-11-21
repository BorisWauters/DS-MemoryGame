package com.henri.server;

import com.henri.model.UserEntity;

import java.util.Comparator;

public class UserComparator implements Comparator<UserEntity> {

    @Override
    public int compare(UserEntity firstPlayer, UserEntity secondPlayer) {
        return (firstPlayer.getScore() - secondPlayer.getScore());
    }
}
