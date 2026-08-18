package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurBookingDetachGroupDto;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingDetachGroupsDto extends GenericJson {

    @Key
    List<AurBookingDetachGroupDto> groups;
}
