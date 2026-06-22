package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class ServiceAccounts
        extends HttpApiSupport
        implements ListSupport_OffsetBased<AurAccount, Long, AurAccount.Page>,
        CreateSupport<AurAccountDto, Long, AurAccountToken> {

    public ServiceAccounts(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurAccount.Page> entityPageClass() {
        return AurAccount.Page.class;
    }

    @Override
    public Class<AurAccountToken> entitySaveResultClass() {
        return AurAccountToken.class;
    }

    @Override
    public String entityPath() {
        return "/am/svc_accounts";
    }

    /**
     * Upserts a managed account using default options.
     * <p>
     * This is a convenience method that automatically sets the predefined {@code recycleKeys}
     * to {@code "email"} and applies default values for all other parameters.
     */
    public AurAccountToken upsertManaged(AurAccountDto acc, long svcAccountId)
            throws IOException {
        return httpPost(
                entityPath() + "/" + svcAccountId + "/accounts",
                QueryParams.of("recycleKeys", "email"),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), acc)
        ).parseAs(AurAccountToken.class);
    }
}
