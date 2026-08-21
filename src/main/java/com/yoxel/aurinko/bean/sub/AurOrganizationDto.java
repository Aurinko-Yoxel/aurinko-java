package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurOrganizationDto extends GenericJson {

    @Key
    private Long id;

    @Key
    private String serviceProvider;

    @Key
    private String xid;

    @Key
    private String domain;

    @Key
    private String name;

    @Key
    private DateTime createdAt;
}
