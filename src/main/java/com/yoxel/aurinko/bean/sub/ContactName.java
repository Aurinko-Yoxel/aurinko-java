package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ContactName extends GenericJson {

    @Key
    private String displayName;

    @Key
    private String prefix;

    @Key
    private String givenName;

    @Key
    private String middleName;

    @Key
    private String familyName;

    @Key
    private String suffix;

    @Key
    private String yomiGivenName;

    @Key
    private String yomiFamilyName;
}
