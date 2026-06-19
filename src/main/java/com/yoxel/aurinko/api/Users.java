package com.yoxel.aurinko.api;

import com.yoxel.aurinko.http.HttpImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Users {

    private final HttpImpl httpImpl;

    public UserAccounts accounts(String userId) {
        return new UserAccounts(userId, httpImpl);
    }
}
