package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurScheduledDraft extends GenericJson {

    @Key
    private Long jobId;

    @Key
    private DateTime sendTime;

    @Key
    private String status;

    @Key
    private String errorMessage;
}
