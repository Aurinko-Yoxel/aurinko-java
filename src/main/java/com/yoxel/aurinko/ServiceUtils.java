package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;

import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.model2.ClientCompany;
import com.yoxel.model2.ClientUser;
import com.yoxel.model2.ServiceTemplate;
import com.yoxel.model2.user.AbsService.Protocol;
import com.yoxel.model2.user.Account;
import com.yoxel.model2.user.SyncData;
import com.yoxel.oauth.common.SecurityUtils;
import com.yoxel.persist.util.Strings;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceUtils {

  public interface UserModelAccess {

    ClientCompany getClientCompany();

    ClientUser getClientUser();

    ServiceTemplate getServiceTemplate(long templId);

    SyncData getSyncData(Account acc);
  }

  private static AurAccountDto fromAccount(
      Account acc,
      SyncData sd,
      OAuth2ClientRegs appRegs,
      UserModelAccess uma
  ) {

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

    accDto.setName(uma.getClientUser().getName());
    accDto.setActive(!acc.isOffline());
    accDto.setAuthScopes(scopes.toArray(new String[0]));
    accDto.setEmail(acc.getEmailAddress());

    if (!StringUtils.isBlank(acc.getUsername()) && !"X".equals(acc.getUsername())) {
      accDto.setLoginString(acc.getUsername());
    }

    if (sd != null) {
      if (sd.getAppKeyPrefix() != null) {
        accDto.setOauthClientId(appRegs.getOAuthClientReg(sd.getAppKeyPrefix() + ".client"));
      }

      accDto.setAuthString1(sd.getAccessToken());
      accDto.setAuthString2(sd.getRefreshToken());
      if (sd.getTokenExpireAt() != null) {
        accDto.setAuthExpiresAt(new DateTime(sd.getTokenExpireAt()));
      }
    }

    if (acc.getProtocol() == Protocol.GMAIL) {
      accDto.setServiceType("Google");
      accDto.setServerUrl(acc.getServer());
      if (sd != null) {
        if (sd.getAppKeyPrefix() == null) {
          if ("repfabric".equals(appRegs.getPartner())) {
            accDto.setOauthClientId(appRegs.getOAuthClientReg("repfabric.google.oauth2.client"));
          } else {
            accDto.setOauthClientId(appRegs.getOAuthClientReg("google.oauth2.client"));
          }
        }

        if (sd.getAccessToken() == null || sd.getRefreshToken() == null) {
          if (acc.isImportEvents() && sd.getCalRefreshToken() != null) {
            accDto.setAuthString1(sd.getCalAccessToken());
            accDto.setAuthString2(sd.getCalRefreshToken());
          } else if (acc.isImportContacts() && sd.getContRefreshToken() != null) {
            accDto.setAuthString1(sd.getContAccessToken());
            accDto.setAuthString2(sd.getContRefreshToken());
          } else if (acc.isScanEmail() && sd.getMailRefreshToken() != null) {
            accDto.setAuthString1(sd.getMailAccessToken());
            accDto.setAuthString2(sd.getMailRefreshToken());
          }
        }
      }
    } else if (acc.getProtocol() == Protocol.OFFICE365 && acc.getAuthMethod() == null) {
      if (sd != null && "office.oauth2".equals(sd.getAppKeyPrefix())
          && !"crm.me".equals(appRegs.getPartner())) {
        accDto.setServiceType("Office365");
        accDto.setServerUrl(acc.getServer() == null ? acc.getProxyServer() : acc.getServer());
      } else {
        accDto.setServiceType("EWS365");
        accDto.setServerUrl((acc.getServer() == null ? acc.getProxyServer() : acc.getServer())
                            + "/EWS/Exchange.asmx");
        if (sd == null || sd.getAppKeyPrefix() == null) {
          accDto.setOauthClientId(appRegs.getOAuthClientReg("office365.oauth2.client"));
        }
      }
    } else if (acc.getProtocol() == Protocol.EWS
               || acc.getProtocol() == Protocol.OFFICE365 && acc.getAuthMethod() != null) {
      accDto.setServiceType("EWS");
      accDto.setServerUrl((acc.getServer() == null ? acc.getProxyServer() : acc.getServer())
                          + "/EWS/Exchange.asmx");
      accDto.setAuthString1(acc.getUsername());
      accDto.setAuthString2(acc.getPassword());
    } else if (acc.getProtocol() == Protocol.TEAMWORKPM && "teamwork".equals(appRegs.getPartner())) {
      accDto.setServiceType("Teamwork");
      accDto.setServerUrl(acc.getServer());
      accDto.setAuthString1(acc.getPassword());
      accDto.setAuthString2(null);
      // is it OK?
      accDto.setAuthOrgId(uma.getClientCompany().getExtId());
      accDto.setAuthUserId(uma.getClientUser().getExtId());
    }

    return accDto;
  }

  public static List<String> templateScopes(ServiceTemplate templ) {

    final boolean
        allScopes =
        !templ.isScanEmail() && !templ.isImportEvents() && !templ.isImportContacts()
        && !templ.isImportTasks();

    final List<String> scopes = new ArrayList<>();
    if (templ.isScanEmail() || allScopes) {
      scopes.add(templ.getProtocol() == Protocol.GMAIL ? "Mail.All" : "Mail.Read");
    }
    if (templ.isImportEvents() || allScopes) {
      scopes.add("Calendar.ReadWrite");
    }
    if (templ.isImportContacts() || allScopes) {
      scopes.add("Contacts.ReadWrite");
    }
    if (templ.isImportTasks() || allScopes) {
      scopes.add("Tasks.ReadWrite");
    }

    return scopes;
  }

  private static AurAccountDto fromTemplate(ServiceTemplate templ, OAuth2ClientRegs appRegs,
                                            AuthServiceAccess authAccess) throws IOException {

    final List<String> scopes = templateScopes(templ);

    String[] ss = Strings.splitByComma(templ.getAuthDomains());

    final String authDomain = ss.length > 0 ? ss[0].trim() : null;

    final AurAccountDto accDto = new AurAccountDto();

    accDto.setActive(true);
    accDto.setAuthScopes(scopes.toArray(new String[scopes.size()]));
    accDto.setName(authDomain == null ? templ.getName() : authDomain);
//        authObtainedAt;
//        authExpiresAt;

    if (templ.getProtocol() == Protocol.GMAIL) {
      accDto.setServiceType("Google");
      accDto.setServerUrl(templ.getInstUrl());
      accDto.setAuthString2(templ.getPassword());

//      scopes.replaceAll(s -> "Mail.Read".equalsIgnoreCase(s) ? "Mail.All" : s);

      try {
        final GenericJson
            sdData =
            Utils.getDefaultJsonFactory().createJsonParser(templ.getPassword())
                .parse(GenericJson.class);

        accDto.setAuthUserId((String) sdData.get("client_id"));
        accDto.setAuthOrgId(authDomain);
        accDto.setServerInfo((String) sdData.get("project_id"));
        accDto.setEmail((String) sdData.get("client_email"));
        accDto.setLoginString((String) sdData.get("private_key_id"));
        accDto.setAuthString1(
            SecurityUtils.extractContentsFromPkFile((String) sdData.get("private_key")));
      } catch (IOException e) {
        throw new IllegalArgumentException("Bad json");
      }
    } else if (templ.getProtocol() == Protocol.OFFICE365) {
      // Migrate only old AuthService accounts
      Validate.notNull(templ.getPassword());

      accDto.setServiceType("EWS365");
      accDto.setServerUrl(templ.getInstUrl() + "/EWS/Exchange.asmx");
      //accDto.setOauthClientId("83f46668-ec23-405f-a0be-21ec17d475b3");
      accDto.setOauthClientId(appRegs.getOAuthClientReg("daemon.office365.oauth2.client"));
      accDto.setAuthOrgId(templ.getExtId());
      accDto.setAuthString1(authAccess.getAuthString(Long.parseLong(templ.getPassword())));
    } else if (templ.getProtocol() == Protocol.EWS) {
      accDto.setServiceType("EWS");
      accDto.setServerUrl(templ.getInstUrl() + "/EWS/Exchange.asmx");
      accDto.setAuthOrgId(authDomain);
      accDto.setAuthString1(templ.getUsername());
      accDto.setAuthString2(templ.getPassword());
      accDto.setLoginString(templ.getUsername());
    }

    return accDto;
  }

  private static boolean protocolSupported(Protocol protocol, String partner) {
    return protocol.isReadEmail() ||
           protocol == Protocol.TEAMWORKPM && "teamwork".equals(partner);
  }

  public static AurAccountToken syncAccount(AurinkoService aurinko, UserModelAccess uma,
                                            Account acc, SyncData sd,
                                            OAuth2ClientRegs appRegs) { // ServiceTemplate forceManagedBy
    if (!protocolSupported(acc.getProtocol(), appRegs.getPartner()) || acc.getEmailAddress() == null) {
      return null;
    }

    if (sd == null) {
      sd = uma.getSyncData(acc);
    }

    final AurAccountDto aurAcc = fromAccount(acc, sd, appRegs, uma);

    aurAcc.setClientOrgId(uma.getClientCompany().getExtId());

    AurAccountToken aurToken = null;
    if (acc.isTrustServer() && acc.getTemplId() > 0) { // || forceManagedBy != null

      final ServiceTemplate svcTempl = uma.getServiceTemplate(acc.getTemplId());
      if (svcTempl.getAurinkoToken() == null) {
        log.warn(
            "No Aurinko token for the template " + svcTempl.getId() + ", " + svcTempl.getName());
        return null;
      }

      if (acc.getProtocol() == Protocol.OFFICE365) {
        aurAcc.setServiceType(svcTempl.getPassword() != null ? "EWS365" : "Office365");
      }

      try (final var svc = AurinkoService.createWithAccountAuth(svcTempl.getAurinkoToken())) {
        final var svcAcc = svc.accounts.getMe();
        log.info("Upserting managed account " + acc.getId() + ", " + acc.getName() + ", " + acc
            .getEmailAddress() + ", clientOrgId: " + uma.getClientCompany().getExtId());

        aurToken = aurinko.accounts.upsertManaged(aurAcc, svcAcc.getId());
      } catch (IOException e) {
        log.warn("Failed to upsert Aurinko managed account " + e.getMessage());
      }

    } else {
      if (aurAcc.getAuthString1() == null && aurAcc.getAuthString2() == null) {
        return null;
      }

      log.info(
          "Upserting account " + acc.getId() + ", " + acc.getName() + ", " + acc.getEmailAddress()
          + ", clientOrgId: " + uma.getClientCompany().getExtId());

      try {
        aurToken = aurinko.accounts.upsertPersonal(aurAcc);
      } catch (IOException e) {
        log.warn("Failed to upsert Aurinko account " + e.getMessage());
      }
    }

    return aurToken;
  }

  public interface AuthServiceAccess {

    String getAuthString(long sid) throws IOException;
  }

  public static AurAccountToken syncTemplate(AurinkoService aurinko, String clientOrgId,
                                             ServiceTemplate svcTempl,
                                             OAuth2ClientRegs appRegs, AuthServiceAccess authAccess)
      throws IOException {
    return syncTemplate(aurinko, clientOrgId, svcTempl, Collections.emptyList(), false, appRegs, authAccess);
  }

  public static AurAccountToken syncTemplate(AurinkoService aurinko, String clientOrgId,
                                             ServiceTemplate svcTempl, List<String> withScopes, boolean gmailMode,
                                             OAuth2ClientRegs appRegs, AuthServiceAccess authAccess)
      throws IOException {
    if (!svcTempl.getProtocol().isReadEmail() || svcTempl.getPassword() == null) {
      return null;
    }

    final AurAccountDto aurAcc = fromTemplate(svcTempl, appRegs, authAccess);

    if (withScopes != null && !withScopes.isEmpty()) {
      final List<String> scopes = new ArrayList<>(Arrays.asList(aurAcc.getAuthScopes()));
      withScopes.forEach(s -> {
        if (!scopes.contains(s)) {
          scopes.add(s);
        }
      });

      aurAcc.setAuthScopes(scopes.toArray(new String[scopes.size()]));
    }

    aurAcc.setClientOrgId(clientOrgId + (gmailMode ? "" : ("/" + svcTempl.getGroupId())));

    log.info(
        "Upserting service account " + svcTempl.getId() + ", " + svcTempl.getName()
        + ", clientOrgId: " + clientOrgId + "/" + svcTempl.getGroupId());

    AurAccountToken aurToken = null;
    try {
      return gmailMode ? aurinko.accounts.upsertGoogleService(aurAcc) : aurinko.accounts.upsertService(aurAcc);
    } catch (IOException e) {
      log.warn("Failed to upsert Aurinko service account " + e.getMessage());
    }

    return null;
  }

}
