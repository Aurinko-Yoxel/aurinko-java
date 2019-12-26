package com.yoxel.aurinko;


import com.yoxel.aurinko.bean.AurOAuthClientReg;
import com.yoxel.model2.AurinkoPartnerToken;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class ОAuth2ClientRegs {

    private static Map<String, List<AurOAuthClientReg>> appRegs = new HashMap<>();

    public interface SecretStoreAccess {

        String getSecret(String alias);
    }

    private ОAuth2ClientRegs(String partner, SecretStoreAccess syncStore) {
        this.syncStore = syncStore;
    }

    private String partner;

    private SecretStoreAccess syncStore;

    public static synchronized ОAuth2ClientRegs forPartner(AurinkoPartnerToken partnerToken, SecretStoreAccess syncStore) {
        if (partnerToken != null) {
            List<AurOAuthClientReg> rl = appRegs.get(partnerToken.getSyncPartner());
            if (rl == null) {
                try {
                    appRegs.put(partnerToken.getSyncPartner(),
                            Arrays.asList(AurinkoService
                                    .createWithAppAuth(
                                            partnerToken.getAurinkoClientId(),
                                            partnerToken
                                                    .getAurinkoClientSecret())
                                    .getOAuthClientRegs().getRecords()));
                } catch (IOException e) {
                    log.error(e.getMessage());
                    appRegs.put(partnerToken.getSyncPartner(), null);
                }
            }
        }

        return new ОAuth2ClientRegs(partnerToken == null ? null : partnerToken.getSyncPartner(), syncStore);
    }

    public String getOAuthClientReg(String key) {
        if (!key.startsWith("daemon.office365.oauth2.")
                && (partner == null || "yoxel".equals(partner) || "teamworkpm".equals(partner)
                || !key.startsWith("google.oauth2.") && !key.startsWith("office365.oauth2."))) {
            return syncStore.getSecret(key);
        }

        // for daemon.office365.oauth2.* or partners with google.*, office365.*
        List<AurOAuthClientReg> rl = appRegs.get(partner == null ? "yoxel" : partner);
        if (rl != null) {
            if (key.startsWith("google.oauth2.")) {
                AurOAuthClientReg
                        reg =
                        rl.stream().filter(r -> "Google".equalsIgnoreCase(r.getServiceType())).findFirst()
                                .get();
                if ("google.oauth2.client".equals(key)) {
                    return reg.getClientId();
                }

                if ("google.oauth2.secret".equals(key)) {
                    return reg.getClientSecret();
                }
            } else if (key.startsWith("office365.oauth2.") || key.startsWith("daemon.office365.oauth2.")) {
                final boolean daemon = key.startsWith("daemon.");

                // TODO: At some point EWS365 >>> Office365
                AurOAuthClientReg
                        reg =
                        rl.stream().filter(r -> "EWS365".equalsIgnoreCase(r.getServiceType())
                                && r.isDaemon() == daemon).findFirst().get();

                if (key.endsWith("office365.oauth2.client")) {
                    return reg.getClientId();
                }

                if (key.endsWith("office365.oauth2.secret")) {
                    return reg.getClientSecret();
                }
            }
        }

        throw new IllegalArgumentException("partner: " + partner + ", key: " + key);
    }
}
