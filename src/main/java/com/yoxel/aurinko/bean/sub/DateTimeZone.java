package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class DateTimeZone extends GenericJson {

    @Key
    private com.google.api.client.util.DateTime date;

    @Key
    private com.google.api.client.util.DateTime dateTime;

    @Key
    private java.lang.String timeZone;
}
