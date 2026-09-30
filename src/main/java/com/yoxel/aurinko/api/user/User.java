package com.yoxel.aurinko.api.user;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.bean.AurUserSession;
import com.yoxel.aurinko.bean.AurUserSessionSettingsDto;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class User extends HttpApiSupport {

    public final Accounts accounts;
    public User(HttpImpl httpImpl) {
        super(httpImpl);
        this.accounts = new Accounts(httpImpl, basePath());
    }

    @Override
    protected String basePath() {
        return "/user";
    }

    public AurUserSession getInfo() throws IOException {
        return httpGet("")
                .parseAs(AurUserSession.class);
    }

    public AurUserSession changeSettings(AurUserSessionSettingsDto entity) throws IOException {
        return httpPatch("/settings", new JsonHttpContent(Utils.getDefaultJsonFactory(), entity))
                .parseAs(AurUserSession.class);
    }

    public AurStatus logout() throws IOException {
        return httpPost("/logout")
                .parseAs(AurStatus.class);
    }
}
