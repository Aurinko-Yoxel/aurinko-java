package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public abstract class AurIdEntity extends GenericJson {

    @Key
    private String id;

    @Key
    private String etag;

    @Key
    private DateTime createdTime;

    @Key
    private DateTime lastModifiedTime;
}
