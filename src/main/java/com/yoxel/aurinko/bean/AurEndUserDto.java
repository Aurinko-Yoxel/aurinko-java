package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurWeekWorkSchedule;
import lombok.Data;

import java.util.List;

@Data
public class AurEndUserDto extends AurIdEntity {

    @Key
    private Long appId;

    @Key
    private String email;

    @Key
    private String name;

    @Key
    private String authOrgId;

    @Key
    private DateTime createdAt;

    @Key
    private DateTime lastActivity;

    @Key
    private Boolean trustedIdentity;

    @Key
    private String externalIdType;

    @Key
    private AurWeekWorkSchedule workHours;

    @Key
    private List<AurEndUserAccountDto> accounts;
}
