package com.yoxel.aurinko.bean;


import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AurCalendar extends AurIdEntity {
    @Key
    String name;

    @Key
    String color;

    @Key
    String accessRole;

    @Key
    boolean primary;
}
