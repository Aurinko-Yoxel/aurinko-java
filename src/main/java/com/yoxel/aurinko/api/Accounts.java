package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.yoxel.aurinko.apis.*;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.bean.AurOAuthClientRegsPage;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 * Accounts API
 */
public class Accounts
        extends HttpApiSupport
        implements ListSupport_OffsetBased<AurAccount, Long, AurAccount.Page>,
        CreateSupport<AurAccountDto, Long, AurAccountToken>,
        ReadSupport<AurAccount, Long>,
        DeleteSupport<Long> {

    public final ServiceAccounts serviceAccounts;

    public Accounts(HttpImpl httpImpl) {
        super(httpImpl);
        this.serviceAccounts = new ServiceAccounts(httpImpl);
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
    public Class<AurAccount> entityClass() {
        return AurAccount.class;
    }

    @Override
    public String entityPath() {
        return "/am/accounts";
    }

    /**
     * Retrieves OAuth registrations related to the application.
     * <p>
     * This operation requires application-level authentication.
     */
    public AurOAuthClientRegsPage getOAuthClientRegs() throws IOException {
        return httpGet("/am/oauth_regs").parseAs(AurOAuthClientRegsPage.class);
    }

    /**
     * Upserts a personal account using default options.
     * <p>
     * This is a convenience method that automatically sets the predefined {@code recycleKeys}
     * to {@code "email,clientOrgId"} and applies default values for all other parameters.
     */
    public AurAccountToken upsertPersonal(AurAccountDto acc) throws IOException {
        return create(
                acc,
                QueryParams.of(qp("recycleKeys", "email,clientOrgId"), qp("ensureAccess", true))
        );
    }

     /**
      * Validates connection credentials before account creation.
      * <p>
      * This is a pre-validation method that tests if a connection can be successfully
      * established using the provided configuration data.
      * <p>
     * This operation requires application-level authentication.
     */
    public AurStatus connect(AurAccountDto entity) throws IOException {
        return httpPost(
                entityPath() + "/connect",
                new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
        ).parseAs(AurStatus.class);
    }

    /**
     * Upserts a service account using default options.
     * <p>
     * This is a convenience method that automatically sets the predefined {@code recycleKeys}
     * to {@code "clientOrgId"} and applies default values for all other parameters.
     */
    public AurAccountToken upsertService(AurAccountDto svcAcc) throws IOException {
        return serviceAccounts.create(
                svcAcc,
                QueryParams.of("recycleKeys", "clientOrgId")
        );
    }

    /**
     * Upserts a Google service account using default options.
     * <p>
     * This is a convenience method that automatically sets the predefined {@code recycleKeys}
     * to {@code "userId,clientOrgId"} and applies default values for all other parameters.
     */
    public AurAccountToken upsertGoogleService(AurAccountDto svcAcc) throws IOException {
        return serviceAccounts.create(
                svcAcc,
                QueryParams.of("recycleKeys", "userId,clientOrgId")
        );
    }

    /**
     * Upserts a managed account using default options.
     * <p>
     * This is a convenience method that automatically sets the predefined {@code recycleKeys}
     * to {@code "email"} and applies default values for all other parameters.
     */
    public AurAccountToken upsertManaged(AurAccountDto acc, long svcAccountId)
            throws IOException {
        return serviceAccounts.upsertManaged(acc, svcAccountId);
    }

    // ---------- Account ----------

    /**
     * Retrieves the current account status.
     * @param pingProvider Specifies whether to ping the provider to verify token validity.
     * @param includeProviderAuth Specifies whether to include provider authentication data in the result.
     */
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

    /**
     * Retrieves the account status using default options.
     * <p>
     * This is a convenience method that disables provider pinging and excludes
     * provider authentication details by default.
     */
    public AurAccount getMe() throws IOException {
        return getMe(false, false);
    }

    /**
     * Deletes the current account.
     * <p>
     * This operation is irreversible and will remove all data associated with the account.
     */
    public void deleteMe() throws IOException {
        httpDelete("/account").parseAs(GenericJson.class);
    }

    /**
     * Deletes the current account token and revokes all associated provider authentication data.
     */
    public void deleteToken() throws IOException {
        httpDelete("/account/token").parseAs(GenericJson.class);
    }
}
