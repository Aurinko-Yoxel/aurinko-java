package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class RecurrenceRange extends GenericJson {

    @Key
    private String type;

    @Key
    private EventDateTime recurrenceStart;

    @Key
    private DateTime recurrenceEnd;

    @Key
    private Integer count;

}
