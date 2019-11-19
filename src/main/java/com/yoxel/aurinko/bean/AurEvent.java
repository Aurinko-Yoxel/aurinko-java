package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurIdEntity;
import lombok.Data;


@Data
public class AurEvent extends AurIdEntity {
    @Key
    String subject;

    @Key
    DateTime start;

    @Key
    DateTime end;

    @Key
    boolean allDay;

    @Key
    boolean cancelled;

    @Key
    boolean responseRequested;

    @Key
    String recurrenceType;

    @Key
    String globalId;

    @Key
    String showAs;
}
