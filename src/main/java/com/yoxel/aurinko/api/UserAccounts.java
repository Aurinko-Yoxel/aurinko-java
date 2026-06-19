package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class UserAccounts extends HttpApiSupport
        implements ListSupport_OffsetBased<AurAccount, Long, AurAccount.Page> {

    private final String userId;

    UserAccounts(String userId, HttpImpl httpImpl) {
        super(httpImpl);
        this.userId = userId;
    }

    @Override
    public Class<AurAccount.Page> entityPageClass() {
        return AurAccount.Page.class;
    }

    @Override
    public String entityPath() {
        return "/users/" + userId + "/accounts";
    }
}
