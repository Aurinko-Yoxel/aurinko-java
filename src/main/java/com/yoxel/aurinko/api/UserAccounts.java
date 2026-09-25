package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class UserAccounts extends HttpApiSupport
        implements ListSupport_OffsetBased<AurAccount, Long, AurAccount.Page>,
        DeleteSupport<Long> {

    private final String parentBasePath;

    UserAccounts(String basePath, HttpImpl httpImpl) {
        super(httpImpl);
        this.parentBasePath = basePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public Class<AurAccount.Page> entityPageClass() {
        return AurAccount.Page.class;
    }

    @Override
    public String entityPath() {
        return "/accounts";
    }
}
