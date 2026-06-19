package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.bean.AurOAuthClientRegsPage;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 * Accounts API
 */
public class Accounts extends HttpApiSupport implements ListSupport_OffsetBased<AurAccount, Long, AurAccount.Page> {

    public Accounts(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurAccount.Page> entityPageClass() {
        return AurAccount.Page.class;
    }

    @Override
    public String entityPath() {
        return "/am/accounts";
    }

    public AurAccount getMe(
            boolean pingProvider,
            boolean includeProviderAuth
    ) throws IOException {
        return httpGet(
                "/account",
                QueryParams.of(
                        qp("pingProvider", pingProvider),
                        qp("includeProviderAuth", includeProviderAuth)
                )
        ).parseAs(AurAccount.class);
    }

    public AurAccount getMe() throws IOException {
        return getMe(false, false);
    }

    public void deleteMe() throws IOException {
        httpDelete("/account").parseAs(GenericJson.class);
    }

    public AurOAuthClientRegsPage getOAuthClientRegs() throws IOException {
        return httpGet("/am/oauth_regs").parseAs(AurOAuthClientRegsPage.class);
    }

    public AurAccountToken upsertPersonal(AurAccountDto acc) throws IOException {
        return httpPost(
                "/am/accounts",
                QueryParams.of("recycleKeys", "email,clientOrgId"),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), acc)
        ).parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertService(AurAccountDto svcAcc) throws IOException {
        return httpPost(
                "/am/svc_accounts",
                QueryParams.of("recycleKeys", "clientOrgId"),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc)
        ).parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertGoogleService(AurAccountDto svcAcc) throws IOException {
        return httpPost(
                "/am/svc_accounts",
                QueryParams.of("recycleKeys", "userId,clientOrgId"),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc)
        ).parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertManaged(AurAccountDto acc, long svcAccountId)
            throws IOException {
        return httpPost(
                "/am/svc_accounts/" + svcAccountId + "/accounts",
                QueryParams.of("recycleKeys", "email"),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), acc)
        ).parseAs(AurAccountToken.class);
    }
}
