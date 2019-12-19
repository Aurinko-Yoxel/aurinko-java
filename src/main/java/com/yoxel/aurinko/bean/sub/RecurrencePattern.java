package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class RecurrencePattern extends GenericJson {

    @Key
    private String frequency;

    @Key
    private Integer interval;

    @Key
    private String[] daysOfWeek;

    @Key
    private String weekStart;

    @Key
    private Integer dayOfMonth;

    @Key
    private Integer monthOfYear;

    @Key
    private String instance;

}
