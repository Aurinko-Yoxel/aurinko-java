package com.yoxel.aurinko.api.user;

import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurUserSessionAccountDto;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Accounts extends HttpApiSupport
        implements
        ReadSupport<AurUserSessionAccountDto, Long>,
        DeleteSupport<Long>,
        ListSupport_TokenBased<AurUserSessionAccountDto, Long, AurUserSessionAccountDto.Page> {

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
    public Class<AurUserSessionAccountDto.Page> entityPageClass() {
        return AurUserSessionAccountDto.Page.class;
    }

    @Override
    public Class<AurUserSessionAccountDto> entityClass() {
        return AurUserSessionAccountDto.class;
    }

    public AurUserSessionAccountDto makeManaged(Long id, QueryParams query) throws IOException {
        return httpPost(
                entityPath() + "/" + id + "/managed",
                query
        ).parseAs(AurUserSessionAccountDto.class);
    }
}
