package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurOutEmailTrack extends GenericJson {

    @Key
    Boolean threadReplies;

    @Key
    Boolean opens;

    @Key
    Boolean links;

    @Key
    Integer trackOpensAfterSendDelay;

    @Key
    String context;

    @Key
    String customDomainAlias;
}
