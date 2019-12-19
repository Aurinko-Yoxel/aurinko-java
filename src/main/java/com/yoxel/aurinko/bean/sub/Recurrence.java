package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class Recurrence extends GenericJson {
    @Key
    String original;

//    ical: Option[ICalRecurrence] = None,
//    simple: Option[SimpleRecurrence] = None,
}
