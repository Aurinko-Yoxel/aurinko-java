package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class SimpleRecurrence extends GenericJson {

    @Key
    private RecurrencePattern pattern;

    @Key
    private RecurrenceRange range;
}
