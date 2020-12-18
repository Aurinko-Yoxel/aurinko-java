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
        this.partner = partner;
        this.syncStore = syncStore;
    }

    private String partner;

    private SecretStoreAccess syncStore;

    public static synchronized ОAuth2ClientRegs forPartner(AurinkoPartnerToken partnerToken, SecretStoreAccess syncStore) {
        if (partnerToken != null && partnerToken.getAurinkoClientId() != null) {
            List<AurOAuthClientReg> rl = appRegs.get(partnerToken.getSyncPartner());
            if (rl == null) {
                try {
                    appRegs.put(partnerToken.getSyncPartner(),
                            Arrays.asList(AurinkoService
                                    .createWithAppAuth(
                                            partnerToken.getAurinkoClientId(),
                                            partnerToken
                                                    .getAurinkoClientSecret())
                                    .accounts.getOAuthClientRegs().getRecords()));
                } catch (IOException e) {
                    log.error(e.getMessage());
                    appRegs.put(partnerToken.getSyncPartner(), null);
                }
            }
        }

        return new ОAuth2ClientRegs(partnerToken == null ? null : partnerToken.getSyncPartner(), syncStore);
    }

    public String getOAuthClientReg(String key) {
//        if (!key.startsWith("daemon.office365.oauth2.")
//                && (partner == null || "yoxel".equals(partner) || "teamworkpm".equals(partner)
//                || !key.startsWith("google.oauth2.") && !key.startsWith("office365.oauth2.")
//                && !key.startsWith("office.oauth2."))) {
//            return syncStore.getSecret(key);
//        }

        String value = null;

        List<AurOAuthClientReg> rl = appRegs.get(partner == null ? "yoxel" : partner);
        if (rl != null) {
            if (key.startsWith("google.oauth2.")) {

                AurOAuthClientReg
                        reg =
                        rl.stream().filter(r -> "Google".equalsIgnoreCase(r.getServiceType())).findFirst()
                                .get();

                if ("google.oauth2.client".equals(key)) {
                    value = reg.getClientId();
                } else if ("google.oauth2.secret".equals(key)) {
                    value = reg.getClientSecret();
                }
            } else if (key.startsWith("office365.oauth2.") || key.startsWith("daemon.office365.oauth2.")
                    || key.startsWith("office.oauth2.") || key.startsWith("daemon.office.oauth2.")) {

                final String svcType = key.contains("office365.oauth2.") ? "EWS365" : "Office365";

                AurOAuthClientReg
                        reg =
                        rl.stream().filter(r -> svcType.equalsIgnoreCase(r.getServiceType())
                                && r.isDaemon() == key.startsWith("daemon.")).findFirst().get();

                if (key.endsWith("oauth2.client")) {
                    value = reg.getClientId();
                } else if (key.endsWith("oauth2.secret")) {
                    value = reg.getClientSecret();
                }
            }
        }

        if (value == null) {
            value = syncStore.getSecret(key);
        }

//        if (value == null)
//            throw new IllegalArgumentException("partner: " + partner + ", key: " + key);

        return value;
    }
}
