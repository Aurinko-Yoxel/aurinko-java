package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class OccurrenceInfo extends GenericJson {

    @Key
    private String id;

    @Key
    private String recurringMasterId;

    @Key
    private String type;

    @Key
    private EventDateTime originalStart;

    @Key
    private EventDateTime start;

}
