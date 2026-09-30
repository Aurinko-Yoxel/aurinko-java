package com.yoxel.aurinko.api.user;

import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurUserSessionAccount;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Accounts extends HttpApiSupport
        implements
        ReadSupport<AurUserSessionAccount, Long>,
        DeleteSupport<Long>,
        ListSupport_TokenBased<AurUserSessionAccount, Long, AurUserSessionAccount.Page> {

    private final String parentBasePath;

    public Accounts(HttpImpl httpImpl, String parentBasePath) {
        super(httpImpl);
        this.parentBasePath = parentBasePath;
    }

    @Override
    protected String basePath() {
        return parentBasePath;
    }

    @Override
    public String entityPath() {
        return "/accounts";
    }

    @Override
    public Class<AurUserSessionAccount.Page> entityPageClass() {
        return AurUserSessionAccount.Page.class;
    }

    @Override
    public Class<AurUserSessionAccount> entityClass() {
        return AurUserSessionAccount.class;
    }

    public AurUserSessionAccount makeManaged(Long id, QueryParams query) throws IOException {
        return httpPost(
                entityPath() + "/" + id + "/managed",
                query
        ).parseAs(AurUserSessionAccount.class);
    }
}
