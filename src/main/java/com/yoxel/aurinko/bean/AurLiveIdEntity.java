package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurMeta;
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

    // TODO: this is a workaround
    @Key
    private DateTime timestamp;

    @Key
    private List<String> omitted;

    @Key("__meta")
    private AurMeta meta;
}
