package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.model2.ClientCompany;
import com.yoxel.model2.ClientUser;
import com.yoxel.model2.ServiceTemplate;
import com.yoxel.model2.user.AbsService;
import com.yoxel.model2.user.Account;
import com.yoxel.model2.user.SyncData;
import com.yoxel.oauth.common.SecurityUtils;
import com.yoxel.persist.util.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServiceUtils {

    public interface UserModelAccess {

        ClientCompany getClientCompany();

        ClientUser getClientUser();

        ServiceTemplate getServiceTemplate(long templId);

        SyncData getSyncData(Account acc);
    }

    private static AurAccountDto fromAccount(Account acc, String userName, SyncData sd, ОAuth2ClientRegs appRegs) {

        final List<String> scopes = new ArrayList<>();
        if (acc.isScanEmail()) {
            scopes.add("Mail.Read");
        }
        if (acc.isImportEvents()) {
            scopes.add("Calendar.ReadWrite");
        }
        if (acc.isImportContacts()) {
            scopes.add("Contacts.ReadWrite");
        }
        if (acc.isImportTasks()) {
            scopes.add("Tasks.ReadWrite");
        }

        final AurAccountDto accDto = new AurAccountDto();

        accDto.setActive(!acc.isOffline());
        accDto.setAuthScopes(scopes.toArray(new String[0]));
        accDto.setServerUrl(acc.getServer() == null ? acc.getProxyServer() : acc.getServer());
        accDto.setEmail(acc.getEmailAddress());
        accDto.setName(userName);
        accDto.setLoginString(acc.getUsername());

        if (sd != null) {
            if (sd.getAppKeyPrefix() != null) {
                accDto.setOauthClientId(appRegs.getOAuthClientReg(sd.getAppKeyPrefix() + ".client"));
            }

            accDto.setAuthString1(sd.getAccessToken());
            accDto.setAuthString2(sd.getRefreshToken());
            if (sd.getTokenExpireAt() != null)
                accDto.setAuthExpiresAt(new DateTime(sd.getTokenExpireAt()));
        }

        if (acc.getProtocol() == AbsService.Protocol.GMAIL) {
            accDto.setServiceType("Google");
            if (sd != null && sd.getAppKeyPrefix() == null) {
                accDto.setOauthClientId(appRegs.getOAuthClientReg("google.oauth2.client"));
            }

            if (sd.getAccessToken() == null) {
                if (acc.isImportEvents()) {
                    accDto.setAuthString1(sd.getCalAccessToken());
                    accDto.setAuthString2(sd.getCalRefreshToken());
                } else if (acc.isImportContacts()) {
                    accDto.setAuthString1(sd.getContAccessToken());
                    accDto.setAuthString2(sd.getContRefreshToken());
                } else if (acc.isScanEmail()) {
                    accDto.setAuthString1(sd.getMailAccessToken());
                    accDto.setAuthString2(sd.getMailRefreshToken());
                }
            }
        } else if (acc.getProtocol() == AbsService.Protocol.OFFICE365) {
            accDto.setServiceType("EWS365");
            if (sd != null && sd.getAppKeyPrefix() == null) {
                accDto.setOauthClientId(appRegs.getOAuthClientReg("office365.oauth2.client"));
            }
        } else if (acc.getProtocol() == AbsService.Protocol.EWS) {
            accDto.setServiceType("EWS");
        }

        return accDto;
    }

    private static AurAccountDto fromTemplate(ServiceTemplate templ, ОAuth2ClientRegs appRegs, AuthServiceAccess authAccess) throws IOException {

        final List<String> scopes = new ArrayList<>();
        if (templ.isScanEmail()) {
            scopes.add("Mail.Read");
        }
        if (templ.isImportEvents()) {
            scopes.add("Calendar.ReadWrite");
        }
        if (templ.isImportContacts()) {
            scopes.add("Contacts.ReadWrite");
        }
        if (templ.isImportTasks()) {
            scopes.add("Tasks.ReadWrite");
        }

        final String authDomain = Strings.splitByComma(templ.getAuthDomains())[0].trim();

        final AurAccountDto accDto = new AurAccountDto();

        accDto.setActive(true);
        accDto.setServerUrl(templ.getInstUrl());
        accDto.setAuthScopes(scopes.toArray(new String[0]));
        accDto.setName(authDomain);
//        authObtainedAt;
//        authExpiresAt;

        if (templ.getProtocol() == AbsService.Protocol.GMAIL) {
            accDto.setServiceType("GOOGLE");
            accDto.setAuthString2(templ.getPassword());

            try {
                final GenericJson sdData = Utils.getDefaultJsonFactory().createJsonParser(templ.getPassword()).parse(GenericJson.class);

                accDto.setAuthUserId((String) sdData.get("client_id"));
                accDto.setAuthOrgId(authDomain);
                accDto.setServerInfo((String) sdData.get("project_id"));
                accDto.setEmail((String) sdData.get("client_email"));
                accDto.setLoginString((String) sdData.get("private_key_id"));
                accDto.setAuthString1(SecurityUtils.extractContentsFromPkFile((String) sdData.get("private_key")));
            } catch (IOException e) {
                throw new IllegalArgumentException("Bad json");
            }
        } else if (templ.getProtocol() == AbsService.Protocol.OFFICE365) {
            accDto.setServiceType("EWS365");
            //accDto.setOauthClientId("83f46668-ec23-405f-a0be-21ec17d475b3");
            accDto.setOauthClientId(appRegs.getOAuthClientReg("daemon.office365.oauth2.client"));
            accDto.setAuthOrgId(templ.getExtId());
            accDto.setAuthString1(authAccess.getAuthString(Long.parseLong(templ.getPassword())));
        } else if (templ.getProtocol() == AbsService.Protocol.EWS) {
            accDto.setServiceType("EWS");
            accDto.setAuthString1(templ.getUsername());
            accDto.setAuthString2(templ.getPassword());
            accDto.setLoginString(templ.getUsername());
        }

        return accDto;
    }

    public static AurAccountToken syncAccount(AurinkoService aurinko, UserModelAccess uma, Account acc,
                                              SyncData sd, ОAuth2ClientRegs appRegs) { // ServiceTemplate forceManagedBy
        if (!acc.getProtocol().isReadEmail()) {
            return null;
        }

        if (sd == null) {
            sd = uma.getSyncData(acc);
        }

        final AurAccountDto
                aurAcc = fromAccount(acc, uma.getClientUser().getName(), sd, appRegs);

        aurAcc.setClientOrgId(uma.getClientCompany().getExtId());

        AurAccountToken aurToken = null;
        if (acc.isTrustServer() && acc.getTemplId() > 0 ) { // || forceManagedBy != null

            final ServiceTemplate svcTempl = uma.getServiceTemplate(acc.getTemplId());
            if (svcTempl.getAurinkoToken() != null) {

                try {
                    AurAccount svcAcc = AurinkoService.createWithAccountAuth(svcTempl.getAurinkoToken()).getAccount();
                    log.info("Upserting managed account " + acc.getId() + ", " + acc.getName() + ", " + acc.getEmailAddress() + ", clientOrgId: " + uma.getClientCompany().getExtId());

                    aurToken = aurinko.upsertManagedAccount(aurAcc, svcAcc.getId());
                } catch (IOException e) {
                    log.warn("Failed to upsert Aurinko managed account " + e.getMessage());
                }
            }
        } else {
            log.info("Upserting account " + acc.getId() + ", " + acc.getName() + ", " + acc.getEmailAddress() + ", clientOrgId: " + uma.getClientCompany().getExtId());

            try {
                aurToken = aurinko.upsertUserAccount(aurAcc);
            } catch (IOException e) {
                log.warn("Failed to upsert Aurinko account " + e.getMessage());
            }
        }

        return aurToken;
    }

    public interface AuthServiceAccess {

        String getAuthString(long sid) throws IOException;
    }

    public static AurAccountToken syncTemplate(AurinkoService aurinko, String clientOrgId, ServiceTemplate svcTempl,
                                               ОAuth2ClientRegs appRegs, AuthServiceAccess authAccess) throws IOException {
        if (!svcTempl.getProtocol().isReadEmail()) {
            return null;
        }

        final AurAccountDto aurAcc = fromTemplate(svcTempl, appRegs, authAccess);

        aurAcc.setClientOrgId(clientOrgId + "/" + svcTempl.getGroupId());

        log.info("Upserting service account " + svcTempl.getId() + ", " + svcTempl.getName() + ", clientOrgId: " + clientOrgId + "/" + svcTempl.getGroupId());

        AurAccountToken aurToken = null;
        try {
            return aurinko.upsertServiceAccount(aurAcc);
        } catch (IOException e) {
            log.warn("Failed to upsert Aurinko service account " + e.getMessage());
        }

        return null;
    }

}
