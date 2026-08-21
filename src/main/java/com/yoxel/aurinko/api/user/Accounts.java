package com.yoxel.aurinko.api.user;

import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurEndUserAccountDto;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class Accounts extends HttpApiSupport
        implements
        ReadSupport<AurEndUserAccountDto, Long>,
        DeleteSupport<Long>,
        ListSupport_TokenBased<AurEndUserAccountDto, Long, AurEndUserAccountDto.Page> {

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
    public Class<AurEndUserAccountDto.Page> entityPageClass() {
        return AurEndUserAccountDto.Page.class;
    }

    @Override
    public Class<AurEndUserAccountDto> entityClass() {
        return AurEndUserAccountDto.class;
    }

    public AurEndUserAccountDto makeManaged(Long id, QueryParams query) throws IOException {
        return httpPost(
                basePath() + entityPath() + "/" + id + "/managed",
                query
        ).parseAs(AurEndUserAccountDto.class);
    }
}
