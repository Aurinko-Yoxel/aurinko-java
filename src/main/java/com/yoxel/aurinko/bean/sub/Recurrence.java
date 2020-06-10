package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class Recurrence extends GenericJson {
    @Key
    private String original;

    @Key("ical")
    private ICalRecurrence iCal;

    @Key
    private SimpleRecurrence simple;
}
