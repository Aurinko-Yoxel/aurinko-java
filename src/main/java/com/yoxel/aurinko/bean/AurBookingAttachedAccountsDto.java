package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingAttachedAccountsDto extends GenericJson {

    @Key
    List<Long> accountIds;

    public static class Page extends AurOffsetPage<AurBookingAttachedAccountsDto> {

    }
}
