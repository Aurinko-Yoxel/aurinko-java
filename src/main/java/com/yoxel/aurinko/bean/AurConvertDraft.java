package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurConvertDraft extends GenericJson {

    @Key
    String threadId;

    @Key
    String messageId;

    @Key
    String internetMessageId;
}
