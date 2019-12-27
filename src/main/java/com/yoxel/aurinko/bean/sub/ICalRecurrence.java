package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class ICalRecurrence extends GenericJson {

    @Key
    private String[] rules;

    @Key
    private EventDateTime recurrenceStart;
}
