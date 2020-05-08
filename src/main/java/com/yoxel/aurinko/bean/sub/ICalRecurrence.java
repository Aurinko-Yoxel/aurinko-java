package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public final class ICalRecurrence extends GenericJson {

    @Key
    private List<String> rules;

    @Key
    private EventDateTime recurrenceStart;
}
