package com.yoxel.aurinko.bean;


import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurCalendar extends AurIdEntity {
    @Key
    String name;

    @Key
    String color;

    @Key
    String accessRole;

    @Key("default")
    Boolean primary;
}
