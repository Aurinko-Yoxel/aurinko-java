package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.aurinko.dto.AurTokenDto;
import com.yoxel.model2.ClientCompany;
import com.yoxel.model2.ClientUser;
import com.yoxel.model2.ServiceTemplate;
import com.yoxel.model2.user.AbsService;
import com.yoxel.model2.user.Account;
import com.yoxel.model2.user.SyncData;
import com.yoxel.models.UserModel;
import com.yoxel.oauth.common.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ServiceUtils {

  public interface SecretAccess {

    String getSecret(String alias);
  }

  public interface UserModelAccess {

    ClientCompany getClientCompany();

    ClientUser getClientUser();

    ServiceTemplate getServiceTemplate(long templId);

    SyncData getSyncData(Account acc);
  }

  private static AurAccountDto fromAccount(Account acc, String userName, SyncData sd, SecretAccess sa) {
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
        accDto.setOauthClientId(sa.getSecret(sd.getAppKeyPrefix() + ".client"));
      }

      accDto.setAuthString1(sd.getAccessToken());
      accDto.setAuthString2(sd.getRefreshToken());
      if (sd.getTokenExpireAt() != null)
        accDto.setAuthExpiresAt(new DateTime(sd.getTokenExpireAt()));
    }

    if (acc.getProtocol() == AbsService.Protocol.GMAIL) {
      accDto.setServiceType("Google");
      if (sd != null && sd.getAppKeyPrefix() == null) {
        accDto.setOauthClientId(sa.getSecret("google.oauth2.client"));
      }
    } else if (acc.getProtocol() == AbsService.Protocol.OFFICE365) {
      accDto.setServiceType("EWS365");
      if (sd != null && sd.getAppKeyPrefix() == null) {
        accDto.setOauthClientId(sa.getSecret("office365.oauth2.client"));
      }
    } else if (acc.getProtocol() == AbsService.Protocol.EWS) {
      accDto.setServiceType("EWS");
    }

    return accDto;
  }

  private static AurAccountDto fromTemplate(ServiceTemplate templ, AuthAccess authAccess) throws IOException {

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

    final AurAccountDto accDto = new AurAccountDto();

    accDto.setActive(true);
    accDto.setServerUrl(templ.getInstUrl());
    accDto.setAuthScopes(scopes.toArray(new String[0]));
//        authObtainedAt;
//        authExpiresAt;

    if (templ.getProtocol() == AbsService.Protocol.GMAIL) {
      accDto.setServiceType("GOOGLE");
      accDto.setAuthString2(templ.getPassword());

      // Strings.splitByComma(templ.getAuthDomains())[0].trim()

      try {
        final GenericJson sdData = Utils.getDefaultJsonFactory().createJsonParser(templ.getPassword()).parse(GenericJson.class);

        accDto.setAuthUserId((String) sdData.get("client_id"));
        accDto.setAuthOrgId((String) sdData.get("project_id"));
        accDto.setEmail((String) sdData.get("client_email"));
        accDto.setLoginString((String) sdData.get("private_key_id"));
        accDto.setAuthString1(SecurityUtils.extractContentsFromPkFile((String) sdData.get("private_key")));
      } catch (IOException e) {
        throw new IllegalArgumentException("Bad json");
      }
    } else if (templ.getProtocol() == AbsService.Protocol.OFFICE365) {
      accDto.setServiceType("EWS365");
      accDto.setOauthClientId("83f46668-ec23-405f-a0be-21ec17d475b3");
      accDto.setAuthOrgId(templ.getExtId());
      //accDto.setLoginString(templ.getExtId());
      accDto.setAuthString1(authAccess.getAuthString(Long.parseLong(templ.getPassword())));
    } else if (templ.getProtocol() == AbsService.Protocol.EWS) {
      accDto.setServiceType("EWS");
      accDto.setAuthString1(templ.getUsername());
      accDto.setAuthString2(templ.getPassword());
      accDto.setLoginString(templ.getUsername());
    }

    return accDto;
  }

  public static AurTokenDto syncAccount(AurinkoService aurinko, UserModelAccess uma, Account acc,
                                        SyncData sd, SecretAccess acsec) {
    if (!acc.getProtocol().isReadEmail()) {
      return null;
    }

    if (sd == null) {
      sd = uma.getSyncData(acc);
    }

    final AurAccountDto
            aurAcc = fromAccount(acc, uma.getClientUser().getName(), sd, acsec);

    AurTokenDto aurToken = null;
    if (acc.isTrustServer() && acc.getTemplId() > 0) {
      ServiceTemplate svcTempl = uma.getServiceTemplate(acc.getTemplId());

      if (svcTempl.getAurinkoToken() != null) {
        System.out.println(
                "Upserting managed account " + acc.getId() + ", " + acc.getName() + ", " + acc
                        .getEmailAddress());

        try {
          aurToken =
                  aurinko.upsertDaemonFlowAccount(aurAcc, svcTempl.getAurinkoToken(),
                          uma.getClientCompany().getExtId());
        } catch (IOException e) {
          log.warn("Failed to upsert Aurinko managed account " + e.getMessage());
        }
      }
    } else {
      System.out.println(
              "Upserting account " + acc.getId() + ", " + acc.getName() + ", " + acc.getEmailAddress());

      try {
        aurToken =
                aurinko.upsertAccountByEmail(aurAcc, uma.getClientCompany().getExtId());
      } catch (IOException e) {
        log.warn("Failed to upsert Aurinko account " + e.getMessage());
      }
    }

    return aurToken;
  }

  public interface AuthAccess {

    String getAuthString(long sid) throws IOException;
  }

  public static AurTokenDto syncTemplate(AurinkoService aurinko, String clientOrgId,
                                         ServiceTemplate svcTempl, AuthAccess authAccess)
      throws IOException {
    if (!svcTempl.getProtocol().isReadEmail()) {
      return null;
    }

    final AurAccountDto aurAcc = fromTemplate(svcTempl, authAccess);

    System.out.println("Upserting service account " + svcTempl.getId() + ", " + svcTempl.getName());

    AurTokenDto aurToken = null;
    try {
      return aurinko
          .upsertSvcAccount(aurAcc, clientOrgId,
                            // do we want to re-cycle token?
                            svcTempl.getAurinkoToken());
    } catch (IOException e) {
      log.warn("Failed to upsert Aurinko service account " + e.getMessage());
    }

    return null;
  }

}
