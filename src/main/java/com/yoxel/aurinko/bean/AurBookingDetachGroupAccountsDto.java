package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingDetachGroupAccountsDto extends GenericJson {
    @Key
    List<Long> accountIds;
}
