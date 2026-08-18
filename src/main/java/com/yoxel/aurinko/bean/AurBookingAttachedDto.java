package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingAttachedDto extends GenericJson {

    @Key
    String extId;

    @Key
    List<Long> accountIds;

    @Key
    String required;

    public static class Page extends AurOffsetPage<AurBookingAttachedDto> {

    }
}
