package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurOAuthClientReg extends GenericJson {

    @Key
    private String name;

    @Key
    private boolean daemon;

    @Key
    private String serviceType;

    @Key
    private String[] scopes;

    @Key
    private String clientId;

    @Key
    private String clientSecret;

    @Key
    private String clientSecret2;
}
