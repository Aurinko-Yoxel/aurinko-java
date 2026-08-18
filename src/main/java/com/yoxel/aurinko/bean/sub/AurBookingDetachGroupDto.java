package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingDetachGroupDto extends GenericJson {

    @Key
    String extId;

    @Key
    List<Long> accountIds;
}
