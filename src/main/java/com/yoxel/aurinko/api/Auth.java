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

    @Override
    protected String basePath() {
        return "/auth";
    }

    /**
     * Exchanges an authorization code for an account token.
     * <p>
     * This operation requires application-level authentication.
     *
     * @deprecated This method is deprecated because it uses an older OAuth flow.
     * Use {@link #exchangeCode(String code)} instead.
     */
    @Deprecated
    public AurAccountToken getToken(String code) throws IOException {
        return httpGet("/token/" + code).parseAs(AurAccountToken.class);
    }

    /**
     * Exchanges an authorization code for an account token.
     * <p>
     * This operation requires application-level authentication.
     */
    public AurAccountToken exchangeCode(String code) throws IOException {
        return httpPost("/token/" + code).parseAs(AurAccountToken.class);
    }
}
