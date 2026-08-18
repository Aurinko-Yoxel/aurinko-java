package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AurBookingAvailableProfilesInDto extends GenericJson {
    @Key
    Instant timeMin;

    @Key
    Instant timeMax;

    @Key
    List<String> profileNames;

    @Key
    List<Long> profileIds;

    @Key
    String required;
}
