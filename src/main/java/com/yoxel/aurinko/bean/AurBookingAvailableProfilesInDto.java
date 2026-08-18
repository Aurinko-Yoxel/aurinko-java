package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingAvailableProfilesInDto extends GenericJson {
    @Key
    DateTime timeMin;

    @Key
    DateTime timeMax;

    @Key
    List<String> profileNames;

    @Key
    List<Long> profileIds;

    @Key
    String required;
}
