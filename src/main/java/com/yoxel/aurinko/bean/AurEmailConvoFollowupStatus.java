package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurEmailConvoFollowupStatus extends GenericJson {

    @Key
    AurEmail lastSentEmail;

    @Key
    AurConvoDripStatus followup;

    @Key
    String mailboxError;

    @Key
    String followupError;
}
