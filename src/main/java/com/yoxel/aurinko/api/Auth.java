package com.yoxel.aurinko.api;

import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

/**
 * Auth API
 */
public class Auth extends HttpApiSupport {

    public Auth(HttpImpl httpImpl) {
        super(httpImpl);
    }

    public AurAccountToken getToken(String code) throws IOException {
        return httpGet("/auth/token/" + code).parseAs(AurAccountToken.class);
    }
}
