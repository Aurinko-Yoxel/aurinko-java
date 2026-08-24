package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurEmailDraftCreateTrackingResponse extends GenericJson {

    @Key
    Boolean trackReplies;

    @Key
    String htmlBody;

    @Key
    String trackingCode;

    @Key
    Integer trackOpensAfterSendDelay;

    @Key
    String context;

    @Key
    Long trackingId;
}
