package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurUserSession;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class Users extends HttpApiSupport
        implements ListSupport_OffsetBased<AurUserSession, String, AurUserSession.Page>,
        DeleteSupport<String> {

    private final String basePath = "/users";
    private final HttpImpl httpImpl;

    public Users(HttpImpl httpImpl) {
        super(httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    protected String basePath() {
        return basePath;
    }

    @Override
    public String entityPath() {
        return "";
    }

    @Override
    public Class<AurUserSession.Page> entityPageClass() {
        return AurUserSession.Page.class;
    }

    public UserAccounts accounts(String userId) {
        return new UserAccounts(basePath + "/" + userId, httpImpl);
    }
}
