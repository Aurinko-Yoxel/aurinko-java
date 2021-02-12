package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public abstract class AurLiveIdEntity extends AurIdEntity {

    @Key
    private String etag;

    @Key
    private DateTime createdTime;

    @Key
    private DateTime lastModifiedTime;

}
